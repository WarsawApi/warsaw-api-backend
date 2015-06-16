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
class ExhibitionProvider {

    private final LocationService locationService

    @Autowired
    ExhibitionProvider(LocationService locationService) {
        this.locationService = locationService
    }

    @HystrixCommand(commandKey = 'ImportIO:GeocodeExhibitions', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
            @HystrixProperty(name = 'execution.isolation.thread.interruptOnTimeout', value = 'false'),
            @HystrixProperty(name = 'default.circuitBreaker.enabled', value = 'false')],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'exhibitions', unless = "#result.isEmpty()")
    List<Localizable> getExhibitionsLocations() {
        return locationService.findByAddresses(exhibitions)
    }

    @HystrixCommand(commandKey = 'ImportIO:exhibitions', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getExhibitions() {
        def root = new JsonSlurper().parse(EXHIBITIONS_URL.toURL())
        //"wrapper_content": "pl. Małachowskiego 3 (mapa) tel. 22 556 96 00",

        return root.results*.wrapper_content
    }

    @SuppressWarnings('unused')
    private List<Object> emptyListFallback() {
        return []
    }

    private final static String EXHIBITIONS_URL = 'https://api.import.io/store/data/3cc2a029-059f-' +
            '4b7b-a98b-49500f0ad5fa/_query?' +
            'input/webpage/url=http%3A%2F%2Fwww.warsawtour.pl%2Fatrakcja_turystyczna%2Flista%2Fpl' +
            '%2F175&_user=a837cd70-64c7-47ef-9a54-775245e5543e&_apikey=a837cd70-64c7-47ef-9a54-' +
            '775245e5543e%3A%2BhpSmGix0JTPD' +
            '%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC' +
            '%2Bg%3D%3D'
}
