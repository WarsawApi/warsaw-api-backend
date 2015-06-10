package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.consumer.warsaw.WarsawApiConsumer
import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.SubwayStation
import com.ghostbuster.warsawApi.domain.internal.preference.Transportation
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@CompileStatic
@Component
final class TransportationScoreCalculator implements ScoreCalculator<Transportation> {

    private final WarsawApiConsumer consumer

    @Autowired
    TransportationScoreCalculator(WarsawApiConsumer consumer) {
        this.consumer = consumer
    }

    @Override
    Class<Transportation> classOfPreference() {
        return Transportation
    }

    @Override
    Double calculateScore(Home property, Transportation preference) {
        Double score = 0d

        if (preference.bus) {
            score += calculateScoreForBus(property)
        }
        if (preference.tramway) {
            score += calculateScoreForTramway(property)
        }
        if (preference.subway) {
            score += calculateScoreForSubway(property)
        }
        return score
    }

    @Cacheable('minDistanceToSubway')
    private Double calculateScoreForSubway(Home property) {
        List<SubwayStation> stations = consumer.subwayStations

        return property.calculateMinDistance(stations)
    }

    private Double calculateScoreForBus(Home property) {
        0d
    }

    private Double calculateScoreForTramway(Home property) {
        0d
    }
}
