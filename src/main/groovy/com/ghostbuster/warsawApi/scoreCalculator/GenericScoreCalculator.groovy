package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.preference.PreferenceAble
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@CompileStatic
@Component
final class GenericScoreCalculator {

    private final Map<Class<? extends PreferenceAble>,
            ? extends ScoreCalculator> preferenceCalculators = new HashMap<>()

    @Autowired
    GenericScoreCalculator(List<? extends ScoreCalculator> calculators) {
        calculators.each {
            ScoreCalculator v -> preferenceCalculators.put(v.classOfPreference(), v)
        }
    }

    Double calculateScore(Home property, PreferenceAble pref) {
        return preferenceCalculators[pref.class].calculateScore(property, pref)
    }
}
