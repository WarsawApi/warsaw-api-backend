package com.ghostbuster.warsawApi.domain.internal

import groovy.transform.CompileStatic

@CompileStatic
class AggregatedFilter {

    SimpleFilter price
    SimpleFilter measurement
    SimpleFilter rooms

    boolean applyFilter(Property property) {
        return priceFilter(property) && measurementFilter(property) && roomsFilter(property)
    }

    private void roomsFilter(Property property) {
        rooms?.applyFilter(property.roomsCount) ?: true
    }

    private void measurementFilter(Property property) {
        measurement?.applyFilter(property.measurement) ?: true
    }

    private void priceFilter(Property property) {
        price?.applyFilter(property.price) ?: true
    }

}
