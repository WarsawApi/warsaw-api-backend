package com.ghostbuster.warsawApi.domain.internal

import groovy.transform.CompileStatic

@CompileStatic
class Nearby implements PreferenceAble {

    String phrase

    Location location
}
