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
class ParkProvider {

    private final LocationService locationService

    @Autowired
    ParkProvider(LocationService locationService) {
        this.locationService = locationService
    }

    @HystrixCommand(commandKey = 'ImportIO:GeocodeParks', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
            @HystrixProperty(name = 'execution.isolation.thread.interruptOnTimeout', value = 'false')],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'parks', unless = "#result.isEmpty()")
    List<Localizable> getParksLocations() {
        return parks.collect { locationService.findByAddress(it) }
    }

    @HystrixCommand(commandKey = 'ImportIO:parks', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getParks() {
        def root = new JsonSlurper().parse(PARKS_URL.toURL())
        return root.results*.nazwa_content
    }

    @SuppressWarnings('unused')
    private List<Object> emptyListFallback() {
        return []
    }

    private final static String PARKS_URL = 'https://api.import.io/store/' +
            'data/28eba12d-efbf-44ba-8ad7-52a9af07aa7c/_query?' +
            'input/webpage/url=http%3A%2F%2Fpl.wikipedia.org%2Fwiki' +
            '%2FTereny_zieleni_w_Warszawie&_user=a837cd70-64c7-47ef-9a54-' +
            '775245e5543e&_apikey=a837cd70-64c7-47ef-9a54-775245e5543e%3A%2BhpSmGix0JTPD' +
            '%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC' +
            '%2Bg%3D%3D'
}
