package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.preference.PreferenceAble

interface ScoreCalculator<T extends PreferenceAble> {

    Class<T> classOfPreference()

    Double calculateScore(Home property, T preference)
}