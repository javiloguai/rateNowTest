package com.berge.ratenow.testapplication.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@Table(name = "SUBSCRIPTION")
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	
	@Basic(optional = false)
	@Column(name = "PERSON_ID_OWNER", nullable = false)
	private Long idPersonOwner;
	
	@Basic(optional = true)
	@Column(name = "SOURCE_ID")
	private Long sourceId;
	
	@Basic(optional = true)
	@Column(name = "OBJ_FILTER_ID")
	private Long objFilterId;
	
	@Basic(optional = true)
	@Column(name = "REGISTRATIONDATE", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date registrationDate;

	
	@Basic(optional = true)
	@Column(name = "PERSON_OWNER_NAME")
	private String personOwnerName;
	
	@Builder.Default
	@Column(name = "ACTIVE", nullable = false)
	private Boolean active = Boolean.TRUE;
	
	
	/**
	 * We assume table creation allows us to delete on cascade through JPA, otherwise we should have to delete them programatically
	 */
	@Builder.Default
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "subscription")
	//@OnDelete(action = OnDeleteAction.CASCADE)
	private List<SubscriberEntity> subscribers = new ArrayList<>();
	
	
}
