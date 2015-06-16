package com.ghostbuster.warsawApi.domain.internal

import groovy.transform.CompileStatic

@CompileStatic
class EmptyLocation implements Localizable {

    @Override
    String getLatitude() {
        return 0
    }

    @Override
    String getLongitude() {
        return 0
    }

    @Override
    String getAddress() {
        return 'empty'
    }

    @Override
    Double distanceTo(Localizable location) {
        return 0d
    }

    @Override
    List<Double> distancesTo(List<Localizable> locations) {
        return [0d]
    }

    Location toLocation() {
        new Location(address: '', latitude: '0', longitude: '0')
    }
}
