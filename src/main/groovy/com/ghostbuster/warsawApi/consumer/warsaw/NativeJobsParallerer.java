package com.ghostbuster.warsawApi.consumer.warsaw;

import com.ghostbuster.warsawApi.domain.internal.Property;
import com.ghostbuster.warsawApi.domain.internal.preference.PreferenceAble;
import com.ghostbuster.warsawApi.scoreCalculator.GenericScoreCalculator;

import java.util.Collection;

public class NativeJobsParallerer {

    public static Double executeJob(GenericScoreCalculator calculator, Property property, Collection<? extends PreferenceAble> preferences) {
        return preferences.parallelStream().mapToDouble(v -> doubleThings(calculator, property, v)).sum();
//        Double sum = 0d;
//        for(PreferenceAble pref : preferences){
//            sum += doubleThings(calculator,property,pref);
//        }
//        return sum;
    }

    private static Double doubleThings(GenericScoreCalculator calculator, Property property, PreferenceAble v) {
        return calculator.calculateScore(property, v);
    }
}
