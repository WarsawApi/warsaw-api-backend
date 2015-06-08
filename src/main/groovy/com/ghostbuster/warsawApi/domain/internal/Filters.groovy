package com.ghostbuster.warsawApi.domain.internal

import groovy.transform.CompileStatic

@CompileStatic
class Filters {

    SimpleFilter price
    SimpleFilter measurement
    SimpleFilter rooms

    boolean applyFilter(Home property) {
        return priceFilter(property) && measurementFilter(property) && roomsFilter(property)
    }

    private void roomsFilter(Home property) {
        rooms?.applyFilter(property.roomsCount) ?: true
    }

    private void measurementFilter(Home property) {
        measurement?.applyFilter(property.measurement) ?: true
    }

    private void priceFilter(Home property) {
        price?.applyFilter(property.price) ?: true
    }

}
