package com.berge.ratenow.testapplication.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.berge.ratenow.testapplication.bo.ObjFilter;
import com.berge.ratenow.testapplication.bo.Subscriber;
import com.berge.ratenow.testapplication.bo.Subscription;
import com.berge.ratenow.testapplication.server.requests.JSONSubscriptionObject;
import com.berge.ratenow.testapplication.server.requests.UpdateSubscribersCommand;


/**
 * @author jruizh
 *
 */
public interface SubscriptionService {

     //Page<Subscription> getAllSubscription(FilterData filterDAta);

	
	List<Subscription> getAllSubscriptionByPerson(@NotNull Long personId);

	List<Subscription> getAllSubscriptionByCustomer(@NotNull Long customerId);

	Subscription getSubscriptionById(@NotNull Long subscriptionId);

	Long setObjFilterWeb(ObjFilter objFilterSubPdf, String filter);

	void insertSubscription(@NotNull Subscription subscription);

	void deleteSubscription(@NotNull Long subscriptionId);

	void updateSubscription(@NotNull JSONSubscriptionObject jsonSubscriptionObject);

	void sendSubscriptions();

	List<Subscriber> getSubscriberByPersonId(@NotNull Long personId);

	Boolean changeStateSubscription(@NotNull Long subId);

	void updateSubscribers(@Valid UpdateSubscribersCommand updateSubscribersCommand);
}
