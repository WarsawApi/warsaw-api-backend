package com.ghostbuster.warsawApi.consumer.google

import com.ghostbuster.warsawApi.domain.internal.Location
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty
import groovy.json.JsonSlurper
import org.apache.log4j.Logger
import org.springframework.stereotype.Component

@Component
class GeocodeServiceConsumer {

    Logger log = Logger.getLogger(GeocodeServiceConsumer)

    @HystrixCommand(commandKey = 'Google:geocode', commandProperties = [@HystrixProperty(name = 'hystrix.command.default.circuitBreaker.enabled', value = 'false')])
    Location geocode(String address) {
        String city = 'Warszawa'
        String country = 'Polska'
        def rootGeocode = new JsonSlurper().parse("https://maps.googleapis.com/maps/api/geocode/json?address=${address.replaceAll(' ', '+')},${city},+${country}&sensor=false&key=AIzaSyDT1c-BXhR8Nd8jMD-f6ud9pVN6dmhJVH4".toURL())
        def locationList = rootGeocode.results?.geometry?.location

        if (locationList != null && !locationList.isEmpty()) {
            def location = locationList.first()
            return new Location(latitude: location.lat, longitude: location.lng, address: address)
        }
        log.error(address)
        return new Location()
    }
}
