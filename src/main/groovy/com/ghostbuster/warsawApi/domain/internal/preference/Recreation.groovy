package com.ghostbuster.warsawApi.domain.internal.preference

import groovy.transform.CompileStatic

@CompileStatic
class Recreation implements PreferenceAble {

    boolean parks
    boolean bikes
    boolean reservoirs
}
