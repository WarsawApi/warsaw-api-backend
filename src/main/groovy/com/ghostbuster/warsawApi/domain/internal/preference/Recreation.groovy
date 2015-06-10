package com.ghostbuster.warsawApi.domain.internal.preference

import groovy.transform.CompileStatic

@CompileStatic
final class Recreation implements PreferenceAble {

    boolean parks
    boolean bikes
    boolean reservoirs
}
