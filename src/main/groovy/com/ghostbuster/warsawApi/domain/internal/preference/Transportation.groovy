package com.ghostbuster.warsawApi.domain.internal.preference

import groovy.transform.CompileStatic

@CompileStatic
final class Transportation implements PreferenceAble {

    boolean subway
    boolean tramway
    boolean bus

}
