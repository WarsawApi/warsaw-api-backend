package com.ghostbuster.warsawApi.domain.internal

import groovy.transform.CompileStatic

@CompileStatic
interface LocationAble {

    Location getLocation()

    String getLatitude()

    String getLongitude()

    Double distanceTo(LocationAble location)
}
