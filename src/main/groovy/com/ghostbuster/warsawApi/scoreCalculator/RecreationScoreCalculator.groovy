package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.consumer.warsaw.WarsawApiConsumer
import com.ghostbuster.warsawApi.domain.internal.Location
import com.ghostbuster.warsawApi.domain.internal.Property
import com.ghostbuster.warsawApi.domain.internal.preference.Recreation
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@CompileStatic
@Component
class RecreationScoreCalculator implements ScoreCalculator<Recreation> {

    @Autowired
    WarsawApiConsumer consumer

    @Override
    Class<Recreation> classOfPreference() {
        return Recreation
    }

    @Override
    Double calculateScore(Property property, Recreation preference) {
        Double score = 0d

        if (preference.bikes) {
            score += calculateScoreForBikes(property)
        }
        if (preference.parks) {
            score += calculateScoreForParks(property)
        }
        if (preference.reservoirs) {
            score += calculateScoreForReservoirs(property)
        }
        return score
    }

    private Double calculateScoreForParks(Property property) {
        return 0d
    }

    private Double calculateScoreForReservoirs(Property property) {
        return 0d
    }

    private Double calculateScoreForBikes(Property property) {
        List<Location> bikeStations = consumer.bikesStations

        return property.calculateMinDistance(bikeStations)
    }
}
