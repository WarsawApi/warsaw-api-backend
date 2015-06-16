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
class TheaterProvider {

    private final LocationService locationService

    @Autowired
    TheaterProvider(LocationService locationService) {
        this.locationService = locationService
    }

    @HystrixCommand(commandKey = 'ImportIO:GeocodeTheaters', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
            @HystrixProperty(name = 'execution.isolation.thread.interruptOnTimeout', value = 'false'),
            @HystrixProperty(name = 'default.circuitBreaker.enabled', value = 'false')],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'theaters', unless = "#result.isEmpty()")
    List<Localizable> getTheatersLocations() {
        return locationService.findByAddresses(theaters)
    }

    @HystrixCommand(commandKey = 'ImportIO:theaters', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getTheaters() {
        def root = new JsonSlurper().parse(THEATERS_URL.toURL())
        return root.results*.value
    }

    @SuppressWarnings('unused')
    private List<Object> emptyListFallback() {
        return []
    }

    private static String THEATERS_URL = 'https://api.import.io/store/data/4902ac0e-1fa5-4273-8524-7' +
            '4bf472eecfe/_query?' +
            'input/webpage/url=http%3A%2F%2Fwarszawa.dlastudenta.pl%2Fteatr%2Fteatry' +
            '%2F&_user=a837cd70-64c7-47ef-9a54-775245e5543e&_apikey=a837cd70-64c7-47ef-9a54-' +
            '775245e5543e%3A%2BhpSmGix0JTPD' +
            '%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC' +
            '%2Bg%3D%3D'
}
