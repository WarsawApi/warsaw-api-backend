package com.ghostbuster.warsawApi.provider.importIo

import com.ghostbuster.warsawApi.domain.internal.Localizable
import com.ghostbuster.warsawApi.service.LocationService
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty
import groovy.json.JsonSlurper
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@CompileStatic
@Component
class CinemaProvider {

    private final LocationService locationService

    @Autowired
    CinemaProvider(LocationService locationService) {
        this.locationService = locationService
    }

    @HystrixCommand(commandKey = 'ImportIO:GeocodeCinemas', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
            @HystrixProperty(name = 'execution.isolation.thread.interruptOnTimeout', value = 'false'),
            @HystrixProperty(name = 'default.circuitBreaker.enabled', value = 'false')],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'cinemas', unless = "#result.isEmpty()")
    List<Localizable> getCinemasLocations() {
        return locationService.findByAddresses(cinemas)
    }

    @HystrixCommand(commandKey = 'ImportIO:cinemas', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getCinemas() {
        def root = new JsonSlurper().parse(CINEMAS_URL.toURL())
        return root.results*.value_1
    }

    @SuppressWarnings('unused')
    private List<Object> emptyListFallback() {
        return []
    }

    private final static String CINEMAS_URL = 'https://api.import.io/store/data/2c0b52f8-fdc5-45' +
            '97-a1f5-88aace635656/_query?' +
            'input/webpage/url=http%3A%2F%2Fwarszawa.repertuary.pl%2Fkino%2Fmap&_user=a837cd70-' +
            '64c7-47ef-9a54-775245e5543e&_apikey=a837cd70-64c7-47ef-9a54-775245e5543e%3A' +
            '%2BhpSmGix0JTPD' +
            '%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC' +
            '%2Bg%3D%3D'
}
