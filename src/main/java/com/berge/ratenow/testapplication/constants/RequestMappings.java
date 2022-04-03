package com.berge.ratenow.testapplication.constants;

/**
 * Resource's URIs.
 */
public final class RequestMappings {

	/**
	 * Default constructor. This class can't be instantiated.
	 */
	private RequestMappings() {
		super();
	}


	public static final String SUBSCRIPTIONS = "/subscriptions";
	

	public static final String GET_BY_PERSON = "/getAllByPerson";
	public static final String GET_BY_CUSTOMER = "/getAllByCustomer";
	public static final String GET_BY_ID = "/getAllById";
	public static final String GET_SUBSCRIBERS_BY_PERSON = "/getSubscriberByPersonId";
	public static final String POST_SUBSCRIPTION = "/saveSubscription";
	public static final String PATCH_SUBSCRIPTION = "/updateSubscription";
	public static final String PUT_SUBSCRIBERS = "/updateSubscribers";
	public static final String DELETE_SUBSCRIPTION = "/deleteSubscription";
	public static final String SEND_SUBSCRIPTIONS = "/sendSubscriptions";
	public static final String PATCH_STATE_SUBSCRIPTION = "/changeStateSubscription";
	
	
	
	
	

}
