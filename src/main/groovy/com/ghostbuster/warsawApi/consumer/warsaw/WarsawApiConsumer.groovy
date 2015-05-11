package com.ghostbuster.warsawApi.consumer.warsaw

import com.ghostbuster.warsawApi.consumer.warsaw.dsl.Filter
import com.ghostbuster.warsawApi.domain.external.warsaw.Response
import com.ghostbuster.warsawApi.domain.external.warsaw.WarsawData
import com.ghostbuster.warsawApi.domain.internal.Property
import com.ghostbuster.warsawApi.domain.internal.SubwayStation
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@CompileStatic
@Component
class WarsawApiConsumer {

    @Cacheable("properties")
    List<Property> getAllProperties() {
        RestTemplate restTemplate = new RestTemplate()
        Response response = (Response) restTemplate.getForObject(WarsawApiRequestBuilder
                .forPropertyRent()
                .build(),
                Response.class)
        return response.result.featureMemberList.collect { WarsawData data -> new Property(data) }
    }

    //odleglosc * waga

    @Cacheable("properties")
    Property getById(String id) {
        RestTemplate restTemplate = new RestTemplate()
        Response response = (Response) restTemplate.getForObject(WarsawApiRequestBuilder
                .forPropertyRent()
                .withFilter(createIdFilter(id))
                .build(),
                Response.class)
        return response.result.featureMemberList.collect { WarsawData data -> new Property(data) }.first()
    }

    @CompileDynamic
    private String createIdFilter(id) {
        return Filter.makeAsXML {
            propertyIsLike {
                propertyName 'ID'
                literal id
            }
        }
    }

    @Cacheable('subway')
    List<SubwayStation> getAllSubwayStations() {
        RestTemplate restTemplate = new RestTemplate()
        Response response = (Response) restTemplate.getForObject(WarsawApiRequestBuilder
                .forSubwayStations()
                .build(),
                Response.class)
        return response.result.featureMemberList.collect { WarsawData data -> new SubwayStation(data) }

    }
}