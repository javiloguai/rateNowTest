package com.berge.ratenow.testapplication.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.berge.ratenow.testapplication.entities.SubscriptionEntity;


/**
 * @author jruizh
 * I put some examples of other kind of call I will not implement anything on other examples
 *
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
	
	boolean existsByPersonId(Long personId);

	List<SubscriptionEntity> findAllByPersonId(Long personId);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "delete from SUBSCRIPTION where personId = :personId", nativeQuery = true)
	void deleteByPersonId(@Param("personId") Long personId);

}
