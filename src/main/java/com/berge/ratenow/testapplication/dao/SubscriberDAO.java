package com.berge.ratenow.testapplication.dao;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.berge.ratenow.testapplication.bo.Subscriber;
import com.berge.ratenow.testapplication.bo.Subscription;
import com.berge.ratenow.testapplication.entities.SubscriberEntity;
import com.berge.ratenow.testapplication.entities.SubscriptionEntity;


/**
 * 
 * @author jruizh
 *  *
 */
public interface SubscriberDAO extends CommonDAO {

	List<SubscriberEntity> getSubscriberByPersonId(@NotNull Long personId);


}
