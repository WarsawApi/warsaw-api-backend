package com.ghostbuster.warsawApi.provider.warsaw

import com.ghostbuster.warsawApi.domain.external.warsaw.Response
import com.ghostbuster.warsawApi.domain.external.warsaw.WarsawData
import com.ghostbuster.warsawApi.domain.internal.Location
import com.ghostbuster.warsawApi.domain.internal.SubwayStation
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty
import groovy.transform.CompileStatic
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@CompileStatic
@Component
class WarsawApiConsumer {

    @HystrixCommand(commandKey = 'Warsaw:subways', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = '5000')],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'subways', unless = "#result.isEmpty()")
    List<SubwayStation> getSubwayStations() {
        RestTemplate restTemplate = new RestTemplate()
        Response response = (Response) restTemplate.getForObject(WarsawApiRequestBuilder
                .forSubwayStations()
                .build(),
                Response.class)
        return response.result.featureMemberList.collect { WarsawData data -> new SubwayStation(data) }
    }

    @HystrixCommand(commandKey = 'Warsaw:bikes', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = '5000')],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'bikes', unless = "#result.isEmpty()")
    List<Location> getBikesStations() {
        RestTemplate restTemplate = new RestTemplate()
        Response response = (Response) restTemplate.getForObject(WarsawApiRequestBuilder
                .forBikesStation()
                .build(),
                Response.class)
        return response.result.featureMemberCoordinates.collect { new Location(it) }
    }

    @SuppressWarnings('unused')
    private List<Object> emptyListFallback() {
        return []
    }
}