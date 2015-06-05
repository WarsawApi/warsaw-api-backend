package com.ghostbuster.warsawApi.controller

import com.ghostbuster.warsawApi.consumer.warsaw.WarsawApiIntegrator
import com.ghostbuster.warsawApi.domain.internal.Preference
import com.ghostbuster.warsawApi.domain.internal.Property
import com.ghostbuster.warsawApi.domain.internal.Result
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

    @Autowired
    WarsawApiIntegrator apiIntegrator

    @RequestMapping('/search')
    public Result search(@RequestBody Preference preferences) {
        return new Result(apiIntegrator.search(preferences))
    }

    @RequestMapping('/details')
    public Property details(@RequestParam(value = "id", required = true) String id) {
        return apiIntegrator.getById(id)
    }

}
