package com.ghostbuster.warsawApi.domain.internal

import groovy.transform.CompileStatic

@CompileStatic
interface Localizable {

    String getLatitude()

    String getLongitude()

    String getAddress()

    void setAddress(String newAddress)

    Double distanceTo(Localizable location)

    List<Double> distancesTo(List<Localizable> locations)

    boolean isEmpty()
}
