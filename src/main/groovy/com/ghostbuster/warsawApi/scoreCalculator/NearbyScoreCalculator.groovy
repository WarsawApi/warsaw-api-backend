package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.domain.internal.Property
import com.ghostbuster.warsawApi.domain.internal.preference.Nearby
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

@CompileStatic
@Component
class NearbyScoreCalculator implements ScoreCalculator<Nearby> {

    @Override
    Double calculateScore(Property property, Nearby preference) {
        return 0d
    }

    @Override
    Class<Nearby> classOfPreference() {
        return Nearby
    }
}
