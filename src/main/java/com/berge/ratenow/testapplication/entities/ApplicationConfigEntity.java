package com.berge.ratenow.testapplication.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jruizh
 *
 */
@Data
@Entity
@Builder
@Table(name = "APPLICATION_CONFIG")
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationConfigEntity {

//	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;
	
	@Id
	@Column(name = "KEYID")
	private String keyId;

	@Basic(optional = false)
	@Column(name = "DESCRIPTION")
	private String description;

	@Basic(optional = false)
	@Column(name = "VALUE")
	private String value;

}
