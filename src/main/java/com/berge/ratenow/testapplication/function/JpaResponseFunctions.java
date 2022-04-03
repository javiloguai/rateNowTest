package com.berge.ratenow.testapplication.function;

import com.berge.ratenow.testapplication.function.jpa.functions.SubscriberEntityFunction;
import com.berge.ratenow.testapplication.function.jpa.functions.SubscriptionEntityFunction;

public class JpaResponseFunctions {

    public static final SubscriptionEntityFunction SUBSCRIPTION_ENTITY_FUNCTION = new SubscriptionEntityFunction();
    
    public static final SubscriberEntityFunction SUBSCRIBER_ENTITY_FUNCTION = new SubscriberEntityFunction();

    private JpaResponseFunctions() {
        super();
    }

}
