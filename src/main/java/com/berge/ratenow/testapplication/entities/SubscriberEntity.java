package com.berge.ratenow.testapplication.entities;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "SUBSCRIBER")
@AllArgsConstructor
@NoArgsConstructor
public class SubscriberEntity {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBSCRIPTION_ID", nullable = false)
    private SubscriptionEntity subscription;


	@Basic(optional = true)
	@Column(name = "REGISTRATIONDATE", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date registrationDate;

	
	@Basic(optional = true)
	@Column(name = "SOME_DATA")
	private String someData;
	
	
}
