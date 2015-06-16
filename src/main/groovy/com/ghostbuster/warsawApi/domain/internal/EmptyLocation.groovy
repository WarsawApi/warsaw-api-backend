package com.ghostbuster.warsawApi.domain.internal

import groovy.transform.CompileStatic

@CompileStatic
class EmptyLocation implements Localizable {

    String address

    EmptyLocation(String address) {
        this.address = address
    }

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
    void setAddress(String newAddress) {

    }

    @Override
    Double distanceTo(Localizable location) {
        return 0d
    }

    @Override
    List<Double> distancesTo(List<Localizable> locations) {
        return [0d]
    }

    @Override
    boolean isEmpty() {
        return true
    }

    Location toLocation() {
        new Location(address: address, latitude: '0', longitude: '0', empty: true)
    }
}
