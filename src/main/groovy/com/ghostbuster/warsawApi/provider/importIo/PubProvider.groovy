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
class PubProvider {

    private final LocationService locationService

    @Autowired
    PubProvider(LocationService locationService) {
        this.locationService = locationService
    }

    @HystrixCommand(commandKey = 'ImportIO:GeocodePubs', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
            @HystrixProperty(name = 'execution.isolation.thread.interruptOnTimeout', value = 'false')],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'pubs', unless = "#result.isEmpty()")
    List<Localizable> getPubsLocations() {
        return locationService.findByAddresses(pubs)
    }

    @HystrixCommand(commandKey = 'ImportIO:pubs', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getPubs() {
        def root = new JsonSlurper().parse(PUBS_URL.toURL())
        return root.results*.searchresult_value
    }

    @SuppressWarnings('unused')
    private List<Object> emptyListFallback() {
        return []
    }

    private static String PUBS_URL = 'https://api.import.io/store/data/fe431774-7e71-41f8-8579-f0691df11161/_query?' +
            'input/webpage/url=https%3A%2F%2Fwww.zomato.com%2Fpl%2Fwarszawa' +
            '%2Fpub&_user=a837cd70-64c7-47ef-9a54-775245e5543e&_apikey=a837cd70-64c7-47ef-9a54-' +
            '775245e5543e%3A%2BhpSmGix0JTPD' +
            '%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC' +
            '%2Bg%3D%3D'
}
