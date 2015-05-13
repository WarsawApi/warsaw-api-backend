package com.ghostbuster.warsawApi.domain.internal

import com.ghostbuster.warsawApi.domain.external.warsaw.Coordinate
import groovy.transform.CompileStatic

@CompileStatic
class Location {

    final String latitude
    final String longitude

    Location(){}

    Location(Coordinate coordinate){
        this.latitude = coordinate.latitude
        this.longitude = coordinate.longitude
    }

    Double distanceTo(Location location) {
        Double x1 = latitude.toDouble()
        Double x2 = location.latitude.toDouble()
        Double y1 = longitude.toDouble()
        Double y2 = location.longitude.toDouble()

        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
}

