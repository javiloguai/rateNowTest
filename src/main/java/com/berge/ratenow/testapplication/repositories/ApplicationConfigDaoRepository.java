package com.berge.ratenow.testapplication.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.berge.ratenow.testapplication.entities.ApplicationConfigEntity;


/**
 * @author jruizh
 *
 */
public interface ApplicationConfigDaoRepository extends JpaRepository<ApplicationConfigEntity, Long> {

	public Optional<ApplicationConfigEntity> findById(Long id);

	public boolean existsById(Long id);
	
	public boolean existsByKeyId(String keyId);
	
	public ApplicationConfigEntity findByKeyId(String keyId);

	public List<ApplicationConfigEntity> findAll();

}
