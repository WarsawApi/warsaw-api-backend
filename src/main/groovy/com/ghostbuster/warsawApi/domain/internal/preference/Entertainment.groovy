package com.ghostbuster.warsawApi.domain.internal.preference

import groovy.transform.CompileStatic

@CompileStatic
final class Entertainment implements PreferenceAble {

    boolean clubs
    boolean restaurants
    boolean pubs
}
