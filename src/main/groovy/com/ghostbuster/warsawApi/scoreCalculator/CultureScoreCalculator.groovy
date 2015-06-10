package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.preference.Culture
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

@CompileStatic
@Component
class CultureScoreCalculator implements ScoreCalculator<Culture> {

    @Override
    Class<Culture> classOfPreference() {
        return Culture
    }

    @Override
    Double calculateScore(Home property, Culture preference) {
        return 0d
    }
}
