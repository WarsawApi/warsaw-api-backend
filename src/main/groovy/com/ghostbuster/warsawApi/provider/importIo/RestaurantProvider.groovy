package com.ghostbuster.warsawApi.provider.importIo

import com.ghostbuster.warsawApi.domain.internal.Localizable
import com.ghostbuster.warsawApi.service.LocationService
import groovy.json.JsonSlurper
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@CompileStatic
@Component
class RestaurantProvider {

    private final LocationService locationService

    @Autowired
    RestaurantProvider(LocationService locationService) {
        this.locationService = locationService
    }

//    @HystrixCommand(commandKey = 'ImportIO:GeocodeRestaurant', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
//            @HystrixProperty(name = 'execution.isolation.thread.interruptOnTimeout', value = 'false')],
//            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'restaurants', unless = "#result.isEmpty()")
    List<Localizable> getRestaurantsLocations() {
        return locationService.findByAddresses(restaurants)

    }
//
//    @HystrixCommand(commandKey = 'ImportIO:restaurants', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
//            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getRestaurants() {
        def root = new JsonSlurper().parse(RESTAURANTS_URL.toURL())
        return root.results*.content
    }

    @SuppressWarnings('unused')
    private List<Object> emptyListFallback() {
        return []
    }

    private final static String RESTAURANTS_URL = 'https://api.import.io/store/data/e0116a7a-8fc5-4c' +
            '6d-b7da-afff9d499f2a/_query?' +
            'input/webpage/url=http%3A%2F%2Frestaurantica.pl%2Flista-restauracji%2F&_user=a837cd70-' +
            '64c7-47ef-9a54-775245e5543e&_apikey=a837cd70-64c7-47ef-9a54-775245e5543e%3A' +
            '%2BhpSmGix0JTPD' +
            '%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC' +
            '%2Bg%3D%3D'
}
