package com.ghostbuster.warsawApi.domain.internal

import com.ghostbuster.warsawApi.domain.common.Coordinate

trait Location {

    String latitude
    String longitude

    Double distanceTo(Location location) {
        Double x1 = latitude.toDouble()
        Double x2 = location.latitude.toDouble()
        Double y1 = longitude.toDouble()
        Double y2 = location.longitude.toDouble()

        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    void setCoordinate(Coordinate coordinate){
        this.latitude = coordinate.latitude
        this.longitude = coordinate.longitude
    }
}

