package com.ghostbuster.warsawApi.consumer.warsaw

import com.ghostbuster.warsawApi.domain.external.warsaw.ContainerEntity
import com.ghostbuster.warsawApi.domain.external.warsaw.Response
import com.ghostbuster.warsawApi.domain.internal.Property
import groovy.transform.CompileStatic
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@CompileStatic
@Component
class WarsawApiConsumer {

    @Cacheable("properties")
    List<Property> search(String id) {
        RestTemplate restTemplate = new RestTemplate()
        Response response = (Response)restTemplate.getForObject(WarsawApiRequestBuilder
                                                                        .forPropertyRent()
                                                                        .limitResults(5)
                                                                        .build(),
                                                                        Response.class)
        return response.result.featureMemberList.collect{ ContainerEntity sw -> new Property(sw) }
    }

    //odleglosc * waga

    Property getById(String id) {
        RestTemplate restTemplate = new RestTemplate()
        Response response = (Response) restTemplate.getForObject(WarsawApiRequestBuilder
                .forPropertyRent()
                .withFilter(WarsawApiFilterBuilder.forProperty('ID').isLike(id).build())
                .built(),
                Response.class)
        return response.result.featureMemberList.collect { ContainerEntity sw -> new Property(sw) }.first()
    }
}