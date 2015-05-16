package com.ghostbuster.warsawApi.consumer.google

import com.ghostbuster.warsawApi.domain.internal.Location
import groovy.json.JsonSlurper
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
class GeocodeServiceConsumer {

    @CompileDynamic
    Location geocode(String address) {
        String city = 'Warszawa'
        String country = 'Polska'
        def rootGeocode = new JsonSlurper().parse("https://maps.googleapis.com/maps/api/geocode/json?address=${address.replaceAll(' ', '+')},${city},+${country}&sensor=false&key=AIzaSyDT1c-BXhR8Nd8jMD-f6ud9pVN6dmhJVH4".toURL())
        def location = rootGeocode.results.geometry.location?.first()

        return new Location(latitude: location.lat, longitude: location.lng, address: address)
    }
}
