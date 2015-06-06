package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.domain.internal.Property
import com.ghostbuster.warsawApi.domain.internal.preference.PreferenceAble

interface ScoreCalculator<T extends PreferenceAble> {

    Class<T> classOfPreference()

    Double calculateScore(Property property, T preference)
}