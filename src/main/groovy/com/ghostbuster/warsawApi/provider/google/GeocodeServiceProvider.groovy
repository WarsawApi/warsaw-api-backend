package com.ghostbuster.warsawApi.provider.google

import com.ghostbuster.warsawApi.domain.internal.EmptyLocation
import com.ghostbuster.warsawApi.domain.internal.Location
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty
import groovy.json.JsonSlurper
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.apache.log4j.Logger
import org.springframework.stereotype.Component

@Component
@CompileStatic
class GeocodeServiceProvider {

    private static
    final List<String> apiKeys = ['AIzaSyBCOI68ONAcSknrt93Lv5QVmV9aWeyMcgo', 'AIzaSyDx2PINJ-2faey_eKdNnG7v2ffQ2pigwhw', 'AIzaSyDD1SuEa_b-M3d9XRniLDwyyHmKR7T86X0']
    private final static Logger log = Logger.getLogger(GeocodeServiceProvider)

    @HystrixCommand(commandKey = 'Google:geocode', commandProperties = [@HystrixProperty(name = 'default.circuitBreaker.enabled', value = 'false')])
    Location geocode(String address) {
        String city = 'Warszawa'
        String country = 'Polska'

        for (String key : apiKeys) {
            try {
                return tryGeocode(address, city, country, key)
            } catch (GeocodeException ignored) {

            }
        }
        log.error("error while geocoding address : ${address}")
        return new EmptyLocation().toLocation()
    }

    @CompileDynamic
    private Location tryGeocode(String address, String city, String country, String apiKey) {
        def rootGeocode = new JsonSlurper().parse("https://maps.googleapis.com/maps/api/geocode/json?address=${address.replaceAll(' ', '+')},${city},+${country}&sensor=false&key=${apiKey}".toURL())
        def locationList = rootGeocode.results?.geometry?.location

        if (locationList != null && !locationList.isEmpty()) {
            def location = locationList.first()
            return new Location(latitude: location.lat, longitude: location.lng, address: address)
        }
        throw new GeocodeException('geocoding failed!')
    }

    static class GeocodeException extends RuntimeException {

    }
}
