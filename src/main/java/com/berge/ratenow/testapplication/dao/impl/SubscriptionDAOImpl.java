package com.berge.ratenow.testapplication.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.berge.ratenow.testapplication.bo.Subscription;
import com.berge.ratenow.testapplication.config.exception.NotFoundException;
import com.berge.ratenow.testapplication.dao.SubscriptionDAO;
import com.berge.ratenow.testapplication.dto.comun.BusquedaGeneralCommand;
import com.berge.ratenow.testapplication.entities.SubscriptionEntity;
import com.berge.ratenow.testapplication.repositories.SubscriberRepository;
import com.berge.ratenow.testapplication.repositories.SubscriptionRepository;
import com.berge.ratenow.testapplication.utils.PaginacionUtil;

/**
 * @author jruizh
 * I put some examples of other kind of call I will not implement anything on other examples
 *
 */
@Component
@Transactional
public class SubscriptionDAOImpl implements SubscriptionDAO {

	private static final Logger LOGGER = LogManager.getLogger(SubscriptionDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	SubscriptionRepository subscriptionRepository;
	
	@Autowired
	SubscriberRepository subscriberRepository;

	
	@Override
	public Map<String, Object> findAll(BusquedaGeneralCommand command, Pageable pageable) {
		LOGGER.debug("Empty method...");
		return null;
	}

	@Override
	public List<SubscriptionEntity> getAllSubscriptionCriteria(@NotNull Long personId) {
		LOGGER.debug("Some log...");
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SubscriptionEntity> query = builder.createQuery(SubscriptionEntity.class);
		Root<SubscriptionEntity> r = query.from(SubscriptionEntity.class);

		Predicate predicate = builder.conjunction();

		if (personId != null) {
			predicate = builder.and(predicate, builder.equal(r.get("personId"), personId));
		}
		
		query = query.orderBy(builder.asc(r.get("personId")), builder.asc(r.get("registrationDate")));

		query.where(predicate);
		TypedQuery<SubscriptionEntity> typedQuery = (TypedQuery<SubscriptionEntity>) PaginacionUtil.readyForQuery(entityManager.createQuery(query), null);
		List<SubscriptionEntity> res = typedQuery.getResultList();

		return res;
	}

	@Override
	public List<SubscriptionEntity> getAllSubscription(@NotNull Long personId) {
		LOGGER.debug("Some log...");
		return subscriptionRepository.findAllByPersonId(personId);
	}

	@Override
	public SubscriptionEntity getSubscriptionById(@NotNull Long subscriptionId) {
		Optional<SubscriptionEntity> sO = subscriptionRepository.findById(subscriptionId);
		if (!sO.isPresent()) {
			LOGGER.debug("Can't wait to see you on G.I. Jane 2");
			throw new NotFoundException("Keep my wife's name out your fu&$%$## mouth!");
		}
		return sO.get();
	}

	@Override
	public void insertSubscription(@NotNull SubscriptionEntity subscription) {
		LOGGER.debug("Some log...");
		subscriptionRepository.saveAndFlush(subscription);

		// WE assume persist on cascade woorks on this database design and we won't need anything on the following block
		/////////////////////////////////////////////////////////
//		subscription.getSubscribers().forEach(s -> {
//    		 subscriberRepository. save(s);
//        });
//		
//		or
//		
//		subscriberRepository.saveAll(subscription.getSubscribers());
		/////////////////////////////////////////////////////////
	
		
	}

	@Override
	public void deleteSubscription(@NotNull SubscriptionEntity subs) {
		LOGGER.debug("Some log...");
		subscriptionRepository.delete(subs);
	}

	@Override
	public void updateSubscription(@NotNull SubscriptionEntity subs) {
		LOGGER.debug("Some log...");
		subscriptionRepository.saveAndFlush(subs);
		
	}

	@Override
	public List<Subscription> getAllSubscriptionsToSend() {
		LOGGER.debug("Some log...");
		return null;
	}

	@Override
	public void deleteSubscriberBySubscriptionId(Long idSubscription) {
		// TODO Auto-generated method stub
		
	}

}
