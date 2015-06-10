package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.preference.Sport
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

@CompileStatic
@Component
final class SportScoreCalculator implements ScoreCalculator<Sport> {


    @Override
    Class<Sport> classOfPreference() {
        return Sport
    }

    @Override
    Double calculateScore(Home property, Sport preference) {
        Double score = 0d

        if (preference.fitness) {
            score += calculateScoreForFitness(property)
        }
        if (preference.pools) {
            score += calculateScoreForPools(property)
        }
        if (preference.tennis) {
            score += calculateScoreForTennis(property)
        }

        return score
    }

    private Double calculateScoreForTennis(Home property) {
        0d
    }

    private Double calculateScoreForPools(Home property) {
        0d
    }

    private Double calculateScoreForFitness(Home property) {
        0d
    }
}
