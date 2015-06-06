package com.ghostbuster.warsawApi.consumer.warsaw;

import com.ghostbuster.warsawApi.domain.internal.Property;
import com.ghostbuster.warsawApi.domain.internal.preference.PreferenceAble;
import com.ghostbuster.warsawApi.scoreCalculator.GenericScoreCalculator;

import java.util.Collection;

public class NativeJobsParallerer {

    public static Double executeJob(GenericScoreCalculator calculator, Property property, Collection<? extends PreferenceAble> preferences) {
        return preferences.parallelStream().mapToDouble(v -> calculator.calculateScore(property, v)).sum();
    }
}
