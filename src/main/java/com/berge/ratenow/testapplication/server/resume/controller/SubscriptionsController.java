package com.berge.ratenow.testapplication.server.resume.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.berge.ratenow.testapplication.bo.ObjFilter;
import com.berge.ratenow.testapplication.bo.Report;
import com.berge.ratenow.testapplication.bo.Subscriber;
import com.berge.ratenow.testapplication.bo.Subscription;
import com.berge.ratenow.testapplication.constants.RequestMappings;
import com.berge.ratenow.testapplication.dto.comun.NullAllowed;
import com.berge.ratenow.testapplication.entities.security.User;
import com.berge.ratenow.testapplication.server.requests.JSONSubscriptionObject;
import com.berge.ratenow.testapplication.server.requests.SubscriptionCommand;
import com.berge.ratenow.testapplication.server.requests.UpdateSubscribersCommand;
import com.berge.ratenow.testapplication.service.ObjFilterService;
import com.berge.ratenow.testapplication.service.ReportService;
import com.berge.ratenow.testapplication.service.SubscriptionService;
import com.berge.ratenow.testapplication.service.UserService;
import com.berge.ratenow.testapplication.utils.seguridad.SeguridadUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@RestController
@Api(tags = {"Subscriptions Controller"})
@SwaggerDefinition(tags = {@Tag(name = "Subscriptions Resource", description = "Subscriptions functionality")})
@RequestMapping(RequestMappings.SUBSCRIPTIONS)
public class SubscriptionsController extends BaseController{

	private static final Logger LOGGER = LogManager.getLogger(SubscriptionsController.class);


    @Autowired
    private SubscriptionService subscriptionService;
    
    @Autowired
	private UserService userService;
    
    @Autowired
	private ReportService reportService;
    
    @Autowired
	private ObjFilterService oObjFilterService;

    /**
     * Some javadoc here
     *
     * @param personId The filtering information
     * @return List
     */
    @GetMapping(RequestMappings.GET_BY_PERSON)
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('CONSULTANT_TYPE1')")
    @ApiOperation(value = "Subscription list", notes = "List of subscriptions")
	public List<Subscription> getAllSubscription(@NotNull @RequestParam(name = "personId") Long personId) {
    	LOGGER.debug("We can log whatever we need...");
        List<Subscription> personSubscriptions = this.subscriptionService.getAllSubscriptionByPerson(personId);
        LOGGER.debug("If it's necessary to pick apart the business objects from the response objects we could deal with a mapper here. I'll will not repeat this or implement on the other endpoints, is just an example.");    
        //return Utils.transformList(personSubscriptions, FunctionFulanito)
        return personSubscriptions;

    }
    
     
    /**
     * Some javadoc here
     *
     * @param customerId The filtering information
     * @return List
     */
    @GetMapping(RequestMappings.GET_BY_CUSTOMER)
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('CONSULTANT_TYPE2')")
    @ApiOperation(value = "Subscription list", notes = "List of subscriptions")
    public List<Subscription> getAllByCustomer(@NotNull  @RequestParam(name = "customerId") Long customerId) {
    	LOGGER.debug("We can log whatever we need...");
        return this.subscriptionService.getAllSubscriptionByCustomer(customerId);
    }


    /**
     * Some javadoc here
     * 
     * @param subscriptionId
     * @return
     */
    @GetMapping(RequestMappings.GET_BY_ID)
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('CONSULTANT_TYPE1')")
    @ApiOperation(value = "Subscription list", notes = "List of subscriptions")
    public Subscription getAllById(@NotNull  @RequestParam(name = "subscriptionId") Long subscriptionId) {
    	LOGGER.debug("We can log whatever we need...");
    	 
         return this.subscriptionService.getSubscriptionById(subscriptionId);
    }

   
    
    /**
     * @param subscriptionCommand
     */
    @PostMapping(RequestMappings.POST_SUBSCRIPTION)
    @ResponseStatus(code = HttpStatus.CREATED)
    //@PreAuthorize("hasAnyAuthority(@authorizationCheck.getSomeGroup(), 'CONSULTANT_TYPE1')")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Save a Subscription", code = 201, notes = "Save a Subscription to a FulanitopService.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Subscription Subscribed subscribesfully.")
    })
    public void saveSubscription(@Valid @RequestBody final SubscriptionCommand subscriptionCommand) {
    	LOGGER.debug("We can log whatever we need...");
    	Subscription subscription = Subscription.builder().build();
          	 
    	 User personAuth = this.userService.getUserDataByUsername(SeguridadUtil.autheticatedUserName());
    	
    	 subscription.setIdPersonOwner(personAuth.getId());
         subscription.setPersonOwnerName(personAuth.getName());
         subscription.setSourceId(subscriptionCommand.getSourceId());
         
    	 switch (subscriptionCommand.getSourceType()) {
	         case REPORT:
	             // IF SOURCE ID IS A REPORT WE TAKE THE FILTER WEB AND OVERRIDE THE FILTER OF THE ORIGINAL REPORT
	             Report report = reportService.getReportById(subscription.getSourceId());
	             
	             ObjFilter objFilterSubPdf = oObjFilterService.getObjectFilterByIdAndQn(report.getObjFilterReportId(), report.getQnId());
	             
	             subscription.setObjFilterId(subscriptionService.setObjFilterWeb(objFilterSubPdf, subscriptionCommand.getFilter()));
	             break;
	         case QUESTIONNAIRE:
	             // IF SOURCE ID IS A QUESTIONNAIRE WE TAKE THE FILTER OF THE PERSON WHO CREATE THE SUB
	             ObjFilter objFilterSubExcel = oObjFilterService.getObjectFilterByIdAndQn(personAuth.getId(), subscription.getSourceId());
	             subscription.setObjFilterId(subscriptionService.setObjFilterWeb(objFilterSubExcel, subscriptionCommand.getFilter()));
	             break;
	     }
         //The Subscribers will be updated on the method insertSubscription
    	 this.subscriptionService.insertSubscription(subscription);
	     
    }

    /**
     * @param subscriptionId
     */
    @DeleteMapping(RequestMappings.DELETE_SUBSCRIPTION)
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Delete a Subscription", notes = "Delete whatever we are dealing with.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Subscription deleted successfully.")
    })
    public void deleteSubscription(@NotNull  @RequestParam(name = "subscriptionId") Long subscriptionId) {
    	this.subscriptionService.deleteSubscription(subscriptionId);
    }


    /**
     * @param jsonSubscriptionObject
     */
    @PatchMapping(RequestMappings.PATCH_SUBSCRIPTION)
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Update a Subscription", notes = "Updates a Subscription on the Database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Bad humorist slapped successfully.")
    })
    public void updateSubscription(@Validated(NullAllowed.class) @RequestBody(required = true) JSONSubscriptionObject jsonSubscriptionObject) /*throws JsonProcessingException*/ {
       
    	this.subscriptionService.updateSubscription(jsonSubscriptionObject);
    }
  
    
    /**
     * 
     */
    @GetMapping(RequestMappings.SEND_SUBSCRIPTIONS)
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Send bla bla", code = 201, notes = "Sends, Generates, saves something? who knows. I Just assume i does not write anything to a database, So GET")
    public void sendSubscriptions() {
    	this.subscriptionService.sendSubscriptions();
    }

    /**
     * @param personid
     * @return
     */
    @GetMapping(RequestMappings.GET_SUBSCRIBERS_BY_PERSON)
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Get bla bla", notes = "Getting bad jokes and bad apologies")
    public List<Subscriber> getSubscriberByPersonId(@NotNull @RequestParam(name = "personId") Long personId) {

            return this.subscriptionService.getSubscriberByPersonId(personId);
     
    }

   
    /**
     * @param jsonSubscriptionObject
     * @return new Subscription State
     */
    @PatchMapping(RequestMappings.PATCH_STATE_SUBSCRIPTION)
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Update a Subscription state", notes = "Updates a Subscription state")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Bad humorist slapped successfully.")
    })
    public Boolean changeStateSubscription(@NotNull  @RequestParam(name = "subId") Long subId) {
       
       return this.subscriptionService.changeStateSubscription(subId);
    }

    /**
     * @param updateSubscribersCommand
     */
    @PutMapping(RequestMappings.PUT_SUBSCRIBERS)
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Update Subscribers", notes = "I'm tired, I want to finish this test")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Subscribers updated successfully.")
    })
    public void updateSubscribers(@Valid @RequestBody final UpdateSubscribersCommand updateSubscribersCommand) {
    	
    	this.subscriptionService.updateSubscribers(updateSubscribersCommand);

    }

}
