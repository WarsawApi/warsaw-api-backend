package com.ghostbuster.warsawApi.domain.internal

import com.ghostbuster.warsawApi.domain.external.warsaw.Coordinate
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@CompileStatic
@TupleConstructor
@Entity
class Location implements LocationAble {

    @Id
    @GeneratedValue
    long id

    String latitude
    String longitude

    String address

    Location(){}

    Location(Coordinate coordinate){
        this.latitude = coordinate.latitude
        this.longitude = coordinate.longitude
    }

    Double distanceTo(LocationAble location) {
        Double x1 = latitude.toDouble()
        Double x2 = location.latitude.toDouble()
        Double y1 = longitude.toDouble()
        Double y2 = location.longitude.toDouble()

        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
    }

    List<Double> distancesTo(List<LocationAble> locations) {
        return locations*.distanceTo(this)
    }

    @Override
    Location getLocation() {
        return this
    }
}

