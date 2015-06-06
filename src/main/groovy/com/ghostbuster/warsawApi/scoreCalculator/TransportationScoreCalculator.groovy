package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.consumer.warsaw.WarsawApiConsumer
import com.ghostbuster.warsawApi.domain.internal.Property
import com.ghostbuster.warsawApi.domain.internal.SubwayStation
import com.ghostbuster.warsawApi.domain.internal.preference.Transportation
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@CompileStatic
@Component
class TransportationScoreCalculator implements ScoreCalculator<Transportation> {


    @Autowired
    WarsawApiConsumer consumer

    @Override
    Class<Transportation> classOfPreference() {
        return Transportation
    }

    @Override
    Double calculateScore(Property property, Transportation preference) {
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

    private Double calculateScoreForSubway(Property property) {
        List<SubwayStation> stations = consumer.subwayStations

        return property.calculateMinDistance(stations)
    }

    private Double calculateScoreForBus(Property property) {
        0d
    }

    private Double calculateScoreForTramway(Property property) {
        0d
    }
}
