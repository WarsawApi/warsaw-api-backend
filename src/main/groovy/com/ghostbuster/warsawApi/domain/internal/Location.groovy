package com.ghostbuster.warsawApi.domain.internal

import com.ghostbuster.warsawApi.domain.common.Coordinate

trait Location {

    Coordinate coordinate

    Double distanceTo(Location location) {
        Double x1 = coordinate.latitude.toDouble()
        Double x2 = location.coordinate.latitude.toDouble()
        Double y1 = coordinate.longitude.toDouble()
        Double y2 = location.coordinate.longitude.toDouble()

        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
}

