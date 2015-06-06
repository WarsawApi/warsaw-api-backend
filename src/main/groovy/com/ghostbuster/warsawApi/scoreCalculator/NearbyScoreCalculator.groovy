package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.domain.internal.Location
import com.ghostbuster.warsawApi.domain.internal.Property
import com.ghostbuster.warsawApi.domain.internal.preference.Nearby
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@CompileStatic
@Component
class NearbyScoreCalculator implements ScoreCalculator<Nearby> {

    @Override
    Class<Nearby> classOfPreference() {
        return Nearby
    }

    @Override
    Double calculateScore(Property property, Nearby preference) {
        Double score = 0d

        if (!StringUtils.isEmpty(preference.phrase)) {
            score += calculateScoreForPhrase(property, preference.phrase)
        }

        Location loc = preference.location
        if (loc != null && !StringUtils.isEmpty(loc.latitude) && !StringUtils.isEmpty(loc.latitude)) {
            score += calculateScoreForLocation(property)
        }

        return score
    }

    Double calculateScoreForLocation(Property property) {
        0d
    }

    Double calculateScoreForPhrase(Property property, String address) {
        0d
    }
}
