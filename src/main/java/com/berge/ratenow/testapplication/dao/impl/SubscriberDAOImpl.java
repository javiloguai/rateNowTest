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
import com.berge.ratenow.testapplication.dao.SubscriberDAO;
import com.berge.ratenow.testapplication.dao.SubscriptionDAO;
import com.berge.ratenow.testapplication.dto.comun.BusquedaGeneralCommand;
import com.berge.ratenow.testapplication.entities.SubscriberEntity;
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
public class SubscriberDAOImpl implements SubscriberDAO {

	private static final Logger LOGGER = LogManager.getLogger(SubscriptionDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;


	@Autowired
	SubscriberRepository subscriberRepository;


	@Override
	public Map<String, Object> findAll(BusquedaGeneralCommand command, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<SubscriberEntity> getSubscriberByPersonId(@NotNull Long personId) {
		Optional<SubscriptionEntity> sO = subscriberRepository.findAllByPersonId(personId);
		if (!sO.isPresent()) {
			LOGGER.debug("Can't wait to see you on G.I. Jane 2");
			throw new NotFoundException("Keep my wife's name out your fu&$%$## mouth!");
		}
		return sO.get();
		
	}
	
	
}
