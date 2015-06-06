package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.domain.internal.Property
import com.ghostbuster.warsawApi.domain.internal.preference.Sport
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

@CompileStatic
@Component
class SportScoreCalculator implements ScoreCalculator<Sport> {


    @Override
    Double calculateScore(Property property, Sport preference) {
        return 0d
    }

    @Override
    Class<Sport> classOfPreference() {
        return Sport
    }
}
