package com.berge.ratenow.testapplication.dao;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.berge.ratenow.testapplication.bo.Subscription;
import com.berge.ratenow.testapplication.entities.SubscriptionEntity;


/**
 * 
 * @author jruizh
 *  *
 */
public interface SubscriptionDAO extends CommonDAO {

	public List<SubscriptionEntity> getAllSubscriptionCriteria(@NotNull Long customerId);

	public List<SubscriptionEntity> getAllSubscription(@NotNull Long personId);

	public SubscriptionEntity getSubscriptionById(@NotNull Long subscriptionId);

	public void insertSubscription(@NotNull SubscriptionEntity newS);

	public void deleteSubscription(@NotNull SubscriptionEntity subs);

	public void updateSubscription(SubscriptionEntity subs);

	public List<Subscription> getAllSubscriptionsToSend();

	public void deleteSubscriberBySubscriptionId(Long idSubscription);
	
}
