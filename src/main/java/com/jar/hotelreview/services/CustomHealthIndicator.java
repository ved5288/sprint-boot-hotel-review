package com.jar.hotelreview.services;

import java.util.Random;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator{

    @Override
    public Health health(){

        // Randomly return health as UP or DOWN.
        if (new Random().nextInt(2) == 0){
            return Health.up().build();
        }

        return Health.down().build();
    }
    
}
