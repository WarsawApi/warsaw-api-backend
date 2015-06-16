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

//TODO split
@CompileStatic
@Component
class TennisProvider {

    private final LocationService locationService

    @Autowired
    TennisProvider(LocationService locationService) {
        this.locationService = locationService
    }

    @HystrixCommand(commandKey = 'ImportIO:GeocodeTennis', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
            @HystrixProperty(name = 'execution.isolation.thread.interruptOnTimeout', value = 'false'),
            @HystrixProperty(name = 'default.circuitBreaker.enabled', value = 'false')],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'tennis', unless = "#result.isEmpty()")
    List<Localizable> getTenninsLocations() {
        return locationService.findByAddresses(tennis)

    }

    @HystrixCommand(commandKey = 'ImportIO:tennis', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getTennis() {
        def root = new JsonSlurper().parse(TENNINS_URL.toURL())
        return root.results*.col_content
    }

    @SuppressWarnings('unused')
    private List<Object> emptyListFallback() {
        return []
    }

    private final static String TENNINS_URL = 'https://api.import.io/store/data/67190267-9eed' +
            '-475f-b9cb-804146af8900/_query?' +
            'input/webpage/url=http%3A%2F%2Fwww.tenisportal.com%2Fwyniki%2Fkort%2Fwarszawa%2F&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da' +
            '&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefWwZNyA1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'

}
