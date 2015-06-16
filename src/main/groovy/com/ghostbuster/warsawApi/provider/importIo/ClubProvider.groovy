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
class ClubProvider {

    private final LocationService locationService

    @Autowired
    ClubProvider(LocationService locationService) {
        this.locationService = locationService
    }

    @HystrixCommand(commandKey = 'ImportIO:GeocodeClubs', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
            @HystrixProperty(name = 'execution.isolation.thread.interruptOnTimeout', value = 'false'),
            @HystrixProperty(name = 'default.circuitBreaker.enabled', value = 'false')],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'clubs', unless = "#result.isEmpty()")
    List<Localizable> getClubsLocations() {
        return clubs.collect { locationService.findByAddress(it) }
    }

    @HystrixCommand(commandKey = 'ImportIO:clubs', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getClubs() {
        def root = new JsonSlurper().parse(CLUBS_URL.toURL())
        return root.results*.searchresult_value
    }

    @SuppressWarnings('unused')
    private List<Object> emptyListFallback() {
        return []
    }

    private final static String CLUBS_URL = 'https://api.import.io/store/data/d007a422-0ea7-4' +
            'd63-b0a6-24fee48ee3b8/_query?' +
            'input/webpage/url=https%3A%2F%2Fwww.zomato.com%2Fpl%2Fwarszawa%2Fklub%3Fopen' +
            '%3Dnow%26bar%3D1&_user=a837cd70-64c7-47ef-9a54-775245e5543e&_apikey=a837cd70-' +
            '64c7-47ef-9a54-775245e5543e%3A%2BhpSmGix0JTPD' +
            '%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC' +
            '%2Bg%3D%3D'
}
