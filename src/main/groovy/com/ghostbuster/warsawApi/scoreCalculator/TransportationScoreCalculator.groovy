package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.consumer.warsaw.WarsawApiConsumer
import com.ghostbuster.warsawApi.domain.internal.Property
import com.ghostbuster.warsawApi.domain.internal.preference.Transportation
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@CompileStatic
@Component
class TransportationScoreCalculator implements ScoreCalculator<Transportation> {


    @Autowired
    WarsawApiConsumer consumer
    // List<SubwayStation> stations = warsawConsumer.subwayStations


    @Override
    Double calculateScore(Property property, Transportation preference) {
        return 0d
    }

    @Override
    Class<Transportation> classOfPreference() {
        return Transportation
    }
}
