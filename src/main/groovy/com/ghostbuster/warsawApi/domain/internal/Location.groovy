package com.ghostbuster.warsawApi.domain.internal

import com.ghostbuster.warsawApi.domain.external.warsaw.Coordinate
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor

import javax.persistence.Entity
import javax.persistence.Id

@CompileStatic
@TupleConstructor
@Entity
final class Location implements Localizable {

    @Id
    String address

    String latitude
    String longitude

    Location(){}

    Location(Coordinate coordinate){
        this.latitude = coordinate.latitude
        this.longitude = coordinate.longitude
    }

    @Override
    Double distanceTo(Localizable location) {
        Double x1 = latitude.toDouble()
        Double x2 = location.latitude.toDouble()
        Double y1 = longitude.toDouble()
        Double y2 = location.longitude.toDouble()

        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
    }

    @Override
    List<Double> distancesTo(List<Localizable> locations) {
        return locations*.distanceTo(this)
    }

}

