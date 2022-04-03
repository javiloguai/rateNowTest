package com.berge.ratenow.testapplication.function.jpa.functions;

import java.util.function.Function;

import com.berge.ratenow.testapplication.bo.Subscriber;
import com.berge.ratenow.testapplication.entities.SubscriberEntity;

public class SubscriberFunction implements Function<Subscriber, SubscriberEntity> {

    @Override
    public SubscriberEntity apply(Subscriber input) {

    	SubscriberEntity output = null;

        if (input != null) {

        	output = SubscriberEntity.builder()
        			.id(input.getId())
        			.someData(input.getSomeData())
        			.registrationDate(input.getRegistrationDate())
        			.build();         
        	
        }

        return output;
    }
}

