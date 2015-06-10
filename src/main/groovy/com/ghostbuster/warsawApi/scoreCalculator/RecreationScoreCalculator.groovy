package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.consumer.warsaw.WarsawApiConsumer
import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.Location
import com.ghostbuster.warsawApi.domain.internal.preference.Recreation
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@CompileStatic
@Component
class RecreationScoreCalculator implements ScoreCalculator<Recreation> {

    private final WarsawApiConsumer consumer

    @Autowired
    RecreationScoreCalculator(WarsawApiConsumer consumer) {
        this.consumer = consumer
    }

    @Override
    Class<Recreation> classOfPreference() {
        return Recreation
    }

    @Override
    Double calculateScore(Home property, Recreation preference) {
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

    private Double calculateScoreForParks(Home property) {
        return 0d
    }

    private Double calculateScoreForReservoirs(Home property) {
        return 0d
    }

    @Cacheable('minDistanceToBike')
    private Double calculateScoreForBikes(Home property) {
        List<Location> bikeStations = consumer.bikesStations

        return property.calculateMinDistance(bikeStations)
    }
}
