package com.ghostbuster.warsawApi.domain.internal

import com.ghostbuster.warsawApi.domain.internal.preference.*
import groovy.transform.CompileStatic

@CompileStatic
class Preference {

    Nearby nearby
    Transportation transportation
    Culture culture
    Entertainment entertainment
    Sport sport
    Recreation recreation

    Collection<? extends PreferenceAble> extractProperties() {
        return properties.findAll { k, v -> k != 'class' }.values().findAll {
            it != null
        } as Collection<? extends PreferenceAble>
    }
}
