package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.domain.internal.Property
import com.ghostbuster.warsawApi.domain.internal.preference.Recreation
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

@CompileStatic
@Component
class RecreationScoreCalculator implements ScoreCalculator<Recreation> {

    @Override
    Double calculateScore(Property property, Recreation preference) {
        return 0d
    }

    @Override
    Class<Recreation> classOfPreference() {
        return Recreation
    }
}
