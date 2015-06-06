package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.domain.internal.Property
import com.ghostbuster.warsawApi.domain.internal.preference.Sport
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

@CompileStatic
@Component
class SportScoreCalculator implements ScoreCalculator<Sport> {


    @Override
    Class<Sport> classOfPreference() {
        return Sport
    }

    @Override
    Double calculateScore(Property property, Sport preference) {
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

    private Double calculateScoreForTennis(Property property) {
        0d
    }

    private Double calculateScoreForPools(Property property) {
        0d
    }

    private Double calculateScoreForFitness(Property property) {
        0d
    }
}
