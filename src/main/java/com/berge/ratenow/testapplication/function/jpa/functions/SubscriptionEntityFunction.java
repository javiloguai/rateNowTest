package com.berge.ratenow.testapplication.function.jpa.functions;

import java.util.function.Function;

import com.berge.ratenow.testapplication.bo.Subscription;
import com.berge.ratenow.testapplication.entities.SubscriptionEntity;
import com.berge.ratenow.testapplication.function.JpaResponseFunctions;
import com.berge.ratenow.testapplication.utils.Utils;

public class SubscriptionEntityFunction implements Function<SubscriptionEntity, Subscription> {

    @Override
    public Subscription apply(SubscriptionEntity input) {

    	Subscription output = null;

        if (input != null) {

        	output = Subscription.builder()
        			.id(input.getId())
        			.idPersonOwner(input.getIdPersonOwner())
        			.sourceId(input.getSourceId())
        			.objFilterId(input.getObjFilterId())
        			.registrationDate(input.getRegistrationDate())
        			.personOwnerName(input.getPersonOwnerName())
        			.active(input.getActive())
        			.personsWhoReceiveIt(Utils.transformList(input.getSubscribers(), JpaResponseFunctions.SUBSCRIBER_ENTITY_FUNCTION))
        			.build();         
        	
        }

        return output;
    }
}

