package com.ghostbuster.warsawApi.domain.internal

import groovy.transform.CompileStatic

@CompileStatic
final class Filters {

    SimpleFilter price
    SimpleFilter measurement
    SimpleFilter rooms

    boolean applyFilter(Home property) {
        return priceFilter(property) && measurementFilter(property) && roomsFilter(property)
    }

    private boolean roomsFilter(Home property) {
        return rooms ? rooms.applyFilter(property.roomsCount) : true
    }

    private boolean measurementFilter(Home property) {
        return measurement ? measurement.applyFilter(property.measurement) : true
    }

    private boolean priceFilter(Home property) {
        return price ? price.applyFilter(property.price) : true
    }

}
