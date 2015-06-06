package com.ghostbuster.warsawApi.domain.internal.preference

import groovy.transform.CompileStatic

@CompileStatic
class Transportation implements PreferenceAble {

    boolean subway
    boolean tramway
    boolean bus

}
