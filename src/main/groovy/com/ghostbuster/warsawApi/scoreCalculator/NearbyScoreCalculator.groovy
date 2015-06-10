package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.Location
import com.ghostbuster.warsawApi.domain.internal.preference.Nearby
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@CompileStatic
@Component
final class NearbyScoreCalculator implements ScoreCalculator<Nearby> {


    @Override
    Class<Nearby> classOfPreference() {
        return Nearby
    }

    @Override
    Double calculateScore(Home property, Nearby preference) {
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

    Double calculateScoreForLocation(Home property) {
        0d
    }

    Double calculateScoreForPhrase(Home property, String address) {
        0d
    }
}
