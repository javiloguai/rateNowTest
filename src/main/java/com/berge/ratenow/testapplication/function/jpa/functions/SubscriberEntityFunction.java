package com.berge.ratenow.testapplication.function.jpa.functions;

import java.util.function.Function;

import com.berge.ratenow.testapplication.bo.Subscriber;
import com.berge.ratenow.testapplication.entities.SubscriberEntity;

public class SubscriberEntityFunction implements Function<SubscriberEntity, Subscriber> {

    @Override
    public Subscriber apply(SubscriberEntity input) {

    	Subscriber output = null;

        if (input != null) {

        	output = Subscriber.builder()
        			.id(input.getId())
        			.someData(input.getSomeData())
        			.registrationDate(input.getRegistrationDate())
        			.build();         
        	
        }

        return output;
    }
}

