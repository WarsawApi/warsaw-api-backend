package com.ghostbuster.warsawApi.consumer.warsaw

import com.ghostbuster.warsawApi.consumer.warsaw.dsl.Filter
import com.ghostbuster.warsawApi.domain.external.warsaw.Response
import com.ghostbuster.warsawApi.domain.external.warsaw.WarsawData
import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.Location
import com.ghostbuster.warsawApi.domain.internal.SubwayStation
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty
import groovy.transform.CompileStatic
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

//TODO: use fail fast strategy for hystrix command or use caching?
@CompileStatic
@Component
class WarsawApiConsumer {

    @Cacheable("properties")
    @HystrixCommand(commandKey = 'Warsaw:properties', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = '5000')])
    Home getById(String id) {
        RestTemplate restTemplate = new RestTemplate()
        Response response = (Response) restTemplate.getForObject(WarsawApiRequestBuilder
                .forPropertyRent()
                .withFilter(createIdFilter(id))
                .build(),
                Response.class)
        return response.result.featureMemberList.collect { WarsawData data -> new Home(data) }.first()
    }

    private String createIdFilter(String id) {
        return Filter.makeAsXML {
            propertyIsLike {
                propertyName 'ID'
                literal id
            }
        }
    }

    @Cacheable('subway')
    @HystrixCommand(commandKey = 'Warsaw:subways', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = '5000')])
    List<SubwayStation> getSubwayStations() {
        RestTemplate restTemplate = new RestTemplate()
        Response response = (Response) restTemplate.getForObject(WarsawApiRequestBuilder
                .forSubwayStations()
                .build(),
                Response.class)
        return response.result.featureMemberList.collect { WarsawData data -> new SubwayStation(data) }
    }

    @Cacheable('bikes')
    @HystrixCommand(commandKey = 'Warsaw:bikes', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = '5000')])
    List<Location> getBikesStations() {
        RestTemplate restTemplate = new RestTemplate()
        Response response = (Response) restTemplate.getForObject(WarsawApiRequestBuilder
                .forBikesStation()
                .build(),
                Response.class)
        return response.result.featureMemberCoordinates.collect { new Location(it) }
    }
}