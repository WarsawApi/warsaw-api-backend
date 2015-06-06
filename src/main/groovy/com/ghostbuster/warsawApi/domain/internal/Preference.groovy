package com.ghostbuster.warsawApi.domain.internal

import com.ghostbuster.warsawApi.domain.internal.preference.*
import groovy.transform.CompileStatic

import java.util.stream.Stream

@CompileStatic
class Preference {

    Nearby nearby
    Transportation transportation
    Culture culture
    Entertainment entertainment
    Sport sport
    Recreation recreation


    Stream<? extends PreferenceAble> extractPropertiesAsStream() {
        return properties.entrySet()
                .parallelStream()
                .filter { it.key != 'class' && it.value }
                .map { it.value as PreferenceAble }
    }
}
