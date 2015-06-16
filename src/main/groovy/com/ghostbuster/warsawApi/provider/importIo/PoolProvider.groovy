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
class PoolProvider {

    private final LocationService locationService

    @Autowired
    PoolProvider(LocationService locationService) {
        this.locationService = locationService
    }

    @HystrixCommand(commandKey = 'ImportIO:GeocodePools', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
            @HystrixProperty(name = 'execution.isolation.thread.interruptOnTimeout', value = 'false'),
            @HystrixProperty(name = 'default.circuitBreaker.enabled', value = 'false')],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'pools', unless = "#result.isEmpty()")
    List<Localizable> getPoolsLocations() {
        return locationService.findByAddresses(pools)
    }

    @HystrixCommand(commandKey = 'ImportIO:pools', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getPools() {
        def root = new JsonSlurper().parse(POOLS_URL.toURL())
        return root.results*.col_content
    }

    @SuppressWarnings('unused')
    private List<Object> emptyListFallback() {
        return []
    }

    private final static String POOLS_URL = 'https://api.import.io/store/data/' +
            '0b5a0ea1-750e-4306-bd52-20da1f53133e/_query?' +
            'input/webpage/url=http%3A%2F%2Fwww.benefitsystems.pl%2Fsearch%2Fmultisport%3Flocation%3DWarszawa%252C%2B' +
            'Mazowieckie%26query%3D%26card%255B%255D%3D3%26category%255B%255D%3D8%26new%3D%26submit%3D&_user=a837cd70' +
            '-64c7-47ef-9a54-775245e5543e&_apikey=a837cd70-64c7-47ef-9a54-775245e55' +
            '43e%3A%2BhpSmGix0JTPD%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC%2Bg%3D%3D'

}
