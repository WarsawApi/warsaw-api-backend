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
class FitnessProvider {

    private final LocationService locationService

    @Autowired
    FitnessProvider(LocationService locationService) {
        this.locationService = locationService
    }

    @HystrixCommand(commandKey = 'ImportIO:GeocodeFitness', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
            @HystrixProperty(name = 'execution.isolation.thread.interruptOnTimeout', value = 'false')],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'fitness', unless = "#result.isEmpty()")
    List<Localizable> getFitnessLocations() {
        return locationService.findByAddresses(fitness)
    }

    @HystrixCommand(commandKey = 'ImportIO:fitness', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getFitness() {
        def root = new JsonSlurper().parse(FITNESS_URL.toURL())
        return root.results*.col_content.collect {
            int cityInAddress = it.indexOf('Warszawa')
            int substringLength = cityInAddress == -1 ? it.length() - 1 : cityInAddress + 'Warszawa'.length()

            return it.substring(0, substringLength)

        }
    }

    @SuppressWarnings('unused')
    private List<Object> emptyListFallback() {
        return []
    }

    private final static String FITNESS_URL = 'https://api.import.io/store/data/ea8' +
            '19dd2-d1d4-455b-bbaa-97065f74a636/_query?' +
            'input/webpage/url=http%3A%2F%2Fwww.benefitsystems.pl%2Fsearch%2Fmultisport%3Flocation' +
            '%3DWarszawa%252C%2BMazowieckie%26query%3D%26card%255B%255D%3D3%26category' +
            '%255B%255D%3D1%26category%255B%255D%3D2%26new%3D%26submit%3D' +
            '%23&_user=a837cd70-64c7-47ef-9a54-775245e5543e&_apikey=a837cd70-64c7-47ef-9a54-' +
            '775245e5543e%3A%2BhpSmGix0JTPD' +
            '%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC' +
            '%2Bg%3D%3D'
}
