package com.berge.ratenow.server.resume.controller;

import com.berge.ratenow.Subscriptions.SubscriptionService;
import com.berge.ratenow.common.ErrorSystemControl.SystemInputError;
import com.berge.ratenow.common.ErrorSystemControl.SystemInputErrorDao;
import com.berge.ratenow.sdk.rnclient.ObjectFilter.ObjFilter;
import com.berge.ratenow.sdk.rnclient.ObjectFilter.ObjFilterDao;
import com.berge.ratenow.sdk.rnclient.Person.Person;
import com.berge.ratenow.sdk.rnclient.Person.PersonDao;
import com.berge.ratenow.sdk.rnclient.Report.Report;
import com.berge.ratenow.sdk.rnclient.Report.ReportDao;
import com.berge.ratenow.sdk.rnclient.Utils.SecurityRoleFilter;
import com.berge.ratenow.sdk.rnclient.pdf.Subscriptions.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "subscriptions")
public class SubscriptionsController_Old {

    @Autowired
    PersonDao personDao;
    @Autowired
    SubscriptionDao subscriptionDao;
    @Autowired
    SubscriberDao subscriberDao;
    @Autowired
    private ObjFilterDao objFilterDao;
    @Autowired
    private ReportDao reportDao;
    @Autowired
    private SecurityRoleFilter securityRoleFilter;
    @Autowired
    private SystemInputErrorDao systemInputErrorDao;
    @Autowired
    private SubscriptionService subscriptionService;

    @RequestMapping(value = "getAllByPerson", method = RequestMethod.GET)
    public ResponseEntity<List<Subscription>> getAllSubscription(@RequestParam(name = "personId") int personId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person personAuth = personDao.getPersonByName(auth.getName());

        if (!securityRoleFilter.canPersonAccessPerson(personAuth, personId)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        try {
            List<Subscription> personSubscriptions = subscriptionDao.getSubscriptionsByPersonId(personId);
            return ResponseEntity.status(HttpStatus.OK).body(personSubscriptions);
        } catch (Exception ex) {
            errorGenerator(ex, "getAllSubscriptionByPerson", "personId " + personId, 2);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @RequestMapping(value = "getAllByCustomer", method = RequestMethod.GET)
    public ResponseEntity<List<Subscription>> getAllByCustomer(@RequestParam(name = "customerId") int customerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person personAuth = personDao.getPersonByName(auth.getName());

        if (!securityRoleFilter.canPersonAccessToCustomer(personAuth, customerId)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        try {
            List<Subscription> customerSubscriptions = subscriptionDao.getSubscriptionsByCustomerId(customerId);
            //personDao.getPersonById(personId);
            return ResponseEntity.status(HttpStatus.OK).body(customerSubscriptions);
        } catch (Exception ex) {
            errorGenerator(ex, "getAllByCustomer", "customerId " + customerId, 2);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @RequestMapping(value = "getAllById", method = RequestMethod.GET)
    public ResponseEntity<Subscription> getAllById(@RequestParam(name = "subscriptionId") int subscriptionId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person personAuth = personDao.getPersonByName(auth.getName());

        Subscription subscription = subscriptionDao.getSubscriptionById(subscriptionId);

        if (!securityRoleFilter.canPersonAccessPerson(personAuth, subscription.getIdPersonOwner())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        try {
            return ResponseEntity.status(HttpStatus.OK).body(subscription);
        } catch (Exception ex) {
            errorGenerator(ex, "getAllById", "subscriptionId " + subscriptionId, 2);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @RequestMapping(value = "saveSubscription", method = RequestMethod.POST)
    public ResponseEntity<String> saveSubscription(@RequestBody HttpSubscription httpSubscription) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person personAuth = personDao.getPersonByName(auth.getName());

        System.out.println(httpSubscription.getPeriodicity());
        Subscription subscription = new Subscription();
        subscription.setIdPersonOwner(personAuth.getIdPerson());
        subscription.setPersonOwnerName(personAuth.getPersonName());

        if (!securityRoleFilter.canPersonAccessPerson(personAuth, subscription.getIdPersonOwner())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        try {
            if (!subscription.fromHTTPOBJ(httpSubscription)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NOT A SUBSCRIPTION");

            // SET OBJ FILTER ID FROM REPORT AND WEB
            switch (subscription.getSourceType()) {
                case "re":
                    // IF SOURCE ID IS A REPORT WE TAKE THE FILTER WEB AND OVERRIDE THE FILTER OF THE ORIGINAL REPORT
                    Report report = reportDao.getReportById(Integer.parseInt(subscription.getSourceId()));
                    ObjFilter objFilterSubPdf = objFilterDao.getObjectFilterByIdAndQn(report.getObjFilterReportId(), report.getQnId());
                    subscription.setObjFilterId(subscriptionDao.setObjFilterWeb(objFilterSubPdf, httpSubscription.getFilter()));
                    break;
                case "qn":
                    // IF SOURCE ID IS A QUESTIONNAIRE WE TAKE THE FILTER OF THE PERSON WHO CREATE THE SUB
                    ObjFilter objFilterSubExcel = objFilterDao.getObjectFilterByIdAndQn(personAuth.getObjFilterPersonId(), subscription.getSourceId());
                    subscription.setObjFilterId(subscriptionDao.setObjFilterWeb(objFilterSubExcel, httpSubscription.getFilter()));
                    break;
            }

            int SubID = subscriptionDao.insertSubscription(subscription);
            for (Subscriber s : subscription.getPersonsWhoReceiveIt()) {
                s.setSubscriptionId(SubID);
                subscriberDao.insertSubscriber(s);
            }

            return ResponseEntity.status(HttpStatus.OK).body("ok");
        } catch (Exception ex) {
            errorGenerator(ex, "saveSubscription", "subscriptionOwner " + subscription.getPersonOwnerName(), 2);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @RequestMapping(value = "deleteSubscription", method = RequestMethod.PUT)
    public ResponseEntity<String> deleteSubscription(@RequestParam(name = "subscriptionId") int subscriptionId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person personAuth = personDao.getPersonByName(auth.getName());
        Subscription subscription = subscriptionDao.getSubscriptionById(subscriptionId);

        if (!securityRoleFilter.canPersonAccessPerson(personAuth, subscription.getIdPersonOwner())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        try {
            subscriptionDao.deleteSubscription(subscription);
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        } catch (Exception ex) {
            errorGenerator(ex, "deleteSubscription", "subscriptionId " + subscriptionId, 2);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @RequestMapping(value = "updateSubscription", method = RequestMethod.PUT)
    public ResponseEntity<String> updateSubscription(@RequestBody String jsonSubscriptionObject) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person personAuth = personDao.getPersonByName(auth.getName());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonSubscriptionObject);
        Subscription subscription = subscriptionDao.getSubscriptionById(jsonNode.get("id").asInt());

        if (subscription == null) return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
        if (!securityRoleFilter.canPersonAccessPerson(personAuth, subscription.getIdPersonOwner())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        try {
            subscriptionService.updateSubscription(jsonNode, subscription);
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        } catch (Exception ex) {
            errorGenerator(ex, "updateSubscription", "subscription " + subscription.toString().substring(0, 85), 2);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @RequestMapping(value = "sendSubscriptions", method = RequestMethod.GET)
    public ResponseEntity<String> sendSubscriptions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person personAuth = personDao.getPersonByName(auth.getName());

        if (!securityRoleFilter.onlyAdminAccess(personAuth)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        List<Subscription> subscriptionToSend = new ArrayList<>();
        try {
            subscriptionToSend = subscriptionService.getAllSubscriptionsToSend();
            subscriptionService.generateSubscriptionDocsToSend(subscriptionToSend);

            return ResponseEntity.status(HttpStatus.OK).body("OK");
        } catch (Exception ex) {
            errorGenerator(ex, "sendSubscriptions", "", 2);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @RequestMapping(value = "getSubscriberByPersonId", method = RequestMethod.GET)
    public ResponseEntity<List<Subscriber>> getSubscriberByPersonId(@RequestParam(name = "personId") int personId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person personAuth = personDao.getPersonByName(auth.getName());

        //TODO SECURITY
        //if (securityRoleFilter.onlyAdminAccess(personAuth)) {
        try {
            List<Subscriber> response = subscriberDao.getSubscriptionAndTypeByPersonId(personId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception ex) {
            errorGenerator(ex, "getSubscriberByPersonId", "", 2);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        // }
        //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);//send error 401
    }

    @RequestMapping(value="changeStateSubscription", method = RequestMethod.POST)
    public ResponseEntity<String> changeStateSubscription(@RequestParam(name = "subId") int subId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person personAuth = personDao.getPersonByName(auth.getName());
        Subscription subscription = subscriptionDao.getSubscriptionById(subId);
        if (subscription == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (!securityRoleFilter.canPersonAccessPerson(personAuth, subscription.getIdPersonOwner())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        try {
            subscription.setActive(!subscription.isActive());
            subscriptionDao.updateSubscription(subscription);
            return ResponseEntity.status(HttpStatus.OK).body("ok");

        } catch (Exception ex) {
            errorGenerator(ex, "changeStateSubscription", "subId " + subId, 2);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @RequestMapping(value="updateSubscribers", method = RequestMethod.POST)
    public ResponseEntity<String> updateSubscribers(@RequestParam(name = "subId") int subscriptionId, @RequestParam(name = "personIds") String personIds) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person personAuth = personDao.getPersonByName(auth.getName());
        Subscription subscription = subscriptionDao.getSubscriptionById(subscriptionId);

        if (subscription == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        List<Subscriber> subscribers = subscriberDao.getSubscribersBySubscriptionId(subscriptionId);

        if (!securityRoleFilter.canPersonAccessSubscription(personAuth, subscribers)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        try {
            subscriberDao.deleteSubscriberBySubscriptionId(subscriptionId);
            for (String personId : personIds.split(",")) {
                int subscriberId = Integer.parseInt(personId);
                subscriberDao.insertSubscriber(new Subscriber(subscriptionId, true, true, new Date(), new Date(), subscriberId));
            }

            return ResponseEntity.status(HttpStatus.OK).body("");
        } catch (Exception ex) {
            errorGenerator(ex, "updateSubscribers", "subId " + subscriptionId + ", personIds " + personIds, 2);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    private void errorGenerator(Exception ex, String function, String variables, int severity) {
        SystemInputError systemInputError = new SystemInputError(function, ex.toString(), ex.getMessage(), variables, severity, "SubscriptionsController", "Server");
        systemInputErrorDao.saveException(systemInputError);
    }
}
