package com.ghostbuster.warsawApi.domain.internal

import groovy.transform.CompileStatic

@CompileStatic
interface Localizable {

    String getLatitude()

    String getLongitude()

    String getAddress()

    Double distanceTo(Localizable location)

    List<Double> distancesTo(List<Localizable> locations)
}
