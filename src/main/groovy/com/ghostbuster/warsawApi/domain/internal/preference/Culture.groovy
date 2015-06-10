package com.ghostbuster.warsawApi.domain.internal.preference

import groovy.transform.CompileStatic

@CompileStatic
final class Culture implements PreferenceAble {

    boolean cinemas
    boolean exhibitions
    boolean theaters
}
