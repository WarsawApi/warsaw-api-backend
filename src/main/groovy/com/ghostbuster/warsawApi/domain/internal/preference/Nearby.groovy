package com.ghostbuster.warsawApi.domain.internal.preference

import com.ghostbuster.warsawApi.domain.internal.Location
import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
@Canonical
class Nearby implements PreferenceAble {

    String phrase

    Location location
}
