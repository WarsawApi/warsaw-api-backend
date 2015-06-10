package com.ghostbuster.warsawApi.controller

import com.ghostbuster.warsawApi.consumer.warsaw.WarsawApiIntegrator
import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.Result
import com.ghostbuster.warsawApi.domain.internal.SearchRequest
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CompileStatic
@RestController
@RequestMapping(produces = "application/json")
class PropertyController {

    private final WarsawApiIntegrator apiIntegrator

    @Autowired
    PropertyController(WarsawApiIntegrator apiIntegrator) {
        this.apiIntegrator = apiIntegrator
    }

    @RequestMapping('/search')
    public Result search(@RequestBody SearchRequest request) {
        return new Result(apiIntegrator.search(request))
    }

    @RequestMapping('/details')
    public Home details(@RequestParam(value = "id", required = true) String id) {
        return apiIntegrator.getById(id)
    }

}
