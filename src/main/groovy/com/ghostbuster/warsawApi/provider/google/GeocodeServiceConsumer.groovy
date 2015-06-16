package com.ghostbuster.warsawApi.provider.google

import com.ghostbuster.warsawApi.domain.internal.Location
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty
import groovy.json.JsonSlurper
import org.springframework.stereotype.Component

@Component
class GeocodeServiceConsumer {

    @HystrixCommand(commandKey = 'Google:geocode', commandProperties = [@HystrixProperty(name = 'hystrix.command.default.circuitBreaker.enabled', value = 'false')])
    Location geocode(String address) {
        String city = 'Warszawa'
        String country = 'Polska'
        try {
            return tryGeocode(address, city, country, 'AIzaSyBCOI68ONAcSknrt93Lv5QVmV9aWeyMcgo')
        } catch (RuntimeException ignored) {
            return tryGeocode(address, city, country, 'AIzaSyDx2PINJ-2faey_eKdNnG7v2ffQ2pigwhw')
        }
    }

    private Location tryGeocode(String address, String city, String country, String apiKey) {
        def rootGeocode = new JsonSlurper().parse("https://maps.googleapis.com/maps/api/geocode/json?address=${address.replaceAll(' ', '+')},${city},+${country}&sensor=false&key=${apiKey}".toURL())
        def locationList = rootGeocode.results?.geometry?.location

        if (locationList != null && !locationList.isEmpty()) {
            def location = locationList.first()
            return new Location(latitude: location.lat, longitude: location.lng, address: address)
        }
        throw new RuntimeException('geocoding failed!')
    }
}