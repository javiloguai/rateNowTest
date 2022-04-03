package com.berge.ratenow.testapplication.service.impl;


import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.berge.ratenow.testapplication.bo.ObjFilter;
import com.berge.ratenow.testapplication.bo.Subscriber;
import com.berge.ratenow.testapplication.bo.Subscription;
import com.berge.ratenow.testapplication.config.exception.BusinessRuleViolatedException;
import com.berge.ratenow.testapplication.config.exception.WeirdProcessException;
import com.berge.ratenow.testapplication.dao.SubscriberDAO;
import com.berge.ratenow.testapplication.dao.SubscriptionDAO;
import com.berge.ratenow.testapplication.entities.SubscriberEntity;
import com.berge.ratenow.testapplication.entities.SubscriptionEntity;
import com.berge.ratenow.testapplication.function.JpaRequestFunctions;
import com.berge.ratenow.testapplication.function.JpaResponseFunctions;
import com.berge.ratenow.testapplication.server.requests.JSONSubscriptionObject;
import com.berge.ratenow.testapplication.server.requests.UpdateSubscribersCommand;
import com.berge.ratenow.testapplication.server.resume.controller.Date;
import com.berge.ratenow.testapplication.server.resume.controller.Person;
import com.berge.ratenow.testapplication.service.BasicService;
import com.berge.ratenow.testapplication.service.SubscriptionService;
import com.berge.ratenow.testapplication.utils.Utils;

/**
 * @author jruizh
 * I put some examples of other kind of call I will not implement anything on other examples
 *
 */
@Service
@Validated
@Transactional
public class SubscriptionServiceImpl extends BasicService implements SubscriptionService {
	
	private static final Logger LOGGER = LogManager.getLogger(SubscriptionServiceImpl.class);

       
    @Autowired
    private SubscriptionDAO subscriptionDAO;
    
    @Autowired
    private SubscriberDAO subscriberDAO;


	@Override
	public List<Subscription> getAllSubscriptionByPerson(@NotNull Long personId) {
		LOGGER.debug("Some log...");
		 return Utils.transformList(subscriptionDAO.getAllSubscription(personId), JpaResponseFunctions.SUBSCRIPTION_ENTITY_FUNCTION);
	}


	@Override
	public List<Subscription> getAllSubscriptionByCustomer(@NotNull Long customerId) {
		LOGGER.debug("Some log...");
		//TODO
		//return Utils.transformList(subscriptionDAO.getAllSubscriptionByCustomer(customerId), JpaResponseFunctions.SUBSCRIPTION_ENTITY_FUNCTION);
		return null;
	}


	@Override
	public Subscription getSubscriptionById(@NotNull Long subscriptionId) {
		LOGGER.debug("Some log...");
		return JpaResponseFunctions.SUBSCRIPTION_ENTITY_FUNCTION.apply(subscriptionDAO.getSubscriptionById(subscriptionId));
	}


	@Override
	public Long setObjFilterWeb(ObjFilter objFilterSubPdf, String filter) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void insertSubscription(@NotNull Subscription subscription) {
		LOGGER.debug("Some log...");
		if(validateSubscription(subscription)){
			SubscriptionEntity newS = SubscriptionEntity.builder()
			.idPersonOwner(subscription.getIdPersonOwner())
			.sourceId(subscription.getSourceId())
			.objFilterId(subscription.getObjFilterId())
			.registrationDate(subscription.getRegistrationDate())
			.personOwnerName(subscription.getPersonOwnerName())
			.subscribers(Utils.transformList(subscription.getPersonsWhoReceiveIt(), JpaRequestFunctions.SUBSCRIBER_FUNCTION))		
			.build();
			
			subscriptionDAO.insertSubscription(newS);
			
		}
		
	}


	/**
	 * Here we valdate all we have to validate before inserting
	 * @param subscription
	 * @return
	 */
	private boolean validateSubscription(Subscription subscription) {
		// TODO Auto-generated method stub
		//
		return true;
	}


	@Override
	public void deleteSubscription(@NotNull Long subscriptionId) {
		LOGGER.debug("Some log...");
		SubscriptionEntity subs = subscriptionDAO.getSubscriptionById(subscriptionId);
		
		// We do not check subs because previous call throws NotFoundException
		
		subscriptionDAO.deleteSubscription(subs);

	       
	}


	@Override
	public void updateSubscription(@NotNull JSONSubscriptionObject jsonSubscriptionObject) {
		LOGGER.debug("Some log...");
		if(jsonSubscriptionObject.getIdSubscription() == null ) {
				throw new BusinessRuleViolatedException("Keep my wife's name out your fu&$%$## mouth!");	
		}
		
		SubscriptionEntity subs = subscriptionDAO.getSubscriptionById(jsonSubscriptionObject.getIdSubscription());
		
		// We do not check subs because previous call throws NotFoundException
		
		subs.setObjFilterId(jsonSubscriptionObject.getObjFilterId());
		//subs.someOtherProps(jsonSubscriptionObject.getOtherProps())
	      
    	subscriptionDAO.updateSubscription(subs);
	        
		
	}


	@Override
	public void sendSubscriptions() {
		LOGGER.debug("Some log...");
		List<Subscription> subscriptionToSend = new ArrayList<>();
        
        subscriptionToSend = subscriptionDAO.getAllSubscriptionsToSend();
        this.generateSubscriptionDocsToSend(subscriptionToSend);


		
	}
	
	private void generateSubscriptionDocsToSend(List<Subscription> subscriptionToSend) {
		LOGGER.debug("Some log...");
		boolean somethingGoneWrongOnThatWeirdGenerationProcess = false;
		
		if (somethingGoneWrongOnThatWeirdGenerationProcess){			
			throw new WeirdProcessException("Something went Wrong...");
		}
	}


	@Override
	public List<Subscriber> getSubscriberByPersonId(@NotNull Long personId) {
		return Utils.transformList(subscriberDAO.getSubscriberByPersonId(personId), JpaResponseFunctions.SUBSCRIBER_ENTITY_FUNCTION);
		
	}


	@Override
	public Boolean changeStateSubscription(@NotNull Long subId) {

		SubscriptionEntity subscription = subscriptionDAO.getSubscriptionById(subId);
		// We do not check subs because previous call throws NotFoundException

            subscription.setActive(!subscription.getActive());
            subscriptionDAO.updateSubscription(subscription);
            return subscription.getActive();


	}


	@Override
	public void updateSubscribers(@Valid UpdateSubscribersCommand updateSubscribersCommand) {

		LOGGER.debug("Some log...");
		if(updateSubscribersCommand.getIdSubscription() == null ) {
				throw new BusinessRuleViolatedException("Keep my wife's name out your fu&$%$## mouth!");	
		}
		
		SubscriptionEntity subscription = subscriptionDAO.getSubscriptionById(updateSubscribersCommand.getIdSubscription());
		
		// We do not check subs because previous call throws NotFoundException
		
		if(updateSubscribersCommand.getPersonIds() != null && updateSubscribersCommand.getPersonIds().size() > 0 ) {
			
			List<SubscriberEntity> new_subscribers = new ArrayList<SubscriberEntity>();
			
			updateSubscribersCommand.getPersonIds().forEach(personId -> {
	    		 new_subscribers.addAll(subscriberDAO.getSubscriberByPersonId(personId));
		        });
			
			subscription.setSubscribers(new_subscribers);
			
			subscriptionDAO.deleteSubscriberBySubscriptionId(updateSubscribersCommand.getIdSubscription());
			
			subscriptionDAO.updateSubscription(subscription);
	      		
		}else {
			throw new BusinessRuleViolatedException("... No comments");
		}
        
		
	}
    

}
