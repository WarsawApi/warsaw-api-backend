package com.ghostbuster.warsawApi.controller

import com.ghostbuster.warsawApi.SpringTestRunner
import com.ghostbuster.warsawApi.domain.internal.Property
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

final class PropertyControllerSpec extends SpringTestRunner {


    void "should return properties on /search endpoint"() {
        when:
        ResponseEntity<Property[]> entity = new RestTemplate().getForEntity("http://localhost:8080/search", Property[].class)
        then:
        entity.statusCode == HttpStatus.OK
    }
}
