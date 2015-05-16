package com.ghostbuster.warsawApi.controller

import com.ghostbuster.warsawApi.SpringTestRunner
import com.ghostbuster.warsawApi.domain.internal.Result
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

final class PropertyControllerSpec extends SpringTestRunner {


    void "should return properties on /search endpoint"() {
        when:
        ResponseEntity<Result> entity = new RestTemplate().getForEntity("http://localhost:8080/search", Result.class)
        then:
        entity.statusCode == HttpStatus.OK
    }
}
