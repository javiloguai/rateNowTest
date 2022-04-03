package com.berge.ratenow.testapplication.repositories;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.berge.ratenow.testapplication.entities.SubscriberEntity;
import com.berge.ratenow.testapplication.entities.SubscriptionEntity;


/**
 * @author jruizh
 * I put some examples of other kind of call I will not implement anything on other examples
 *
 */
@Repository
public interface SubscriberRepository extends JpaRepository<SubscriberEntity, Long> {

	@Query(value = "select BLABLABLA where personId = :personId ", nativeQuery = true)
	List<SubscriberEntity> findAllByPersonId(@NotNull Long personId);
	

	

}
