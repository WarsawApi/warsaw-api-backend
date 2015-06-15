package com.ghostbuster.warsawApi.controller

import com.ghostbuster.warsawApi.consumer.ConsumerAggregator
import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.Result
import com.ghostbuster.warsawApi.domain.internal.SearchRequest
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CompileStatic
@RestController
@RequestMapping(produces = "application/json")
class HomeRestController {

    private final ConsumerAggregator apiIntegrator

    @Autowired
    HomeRestController(ConsumerAggregator apiIntegrator) {
        this.apiIntegrator = apiIntegrator
    }

    @RequestMapping('/search')
    @HystrixCommand(commandKey = 'Search', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000"),
            @HystrixProperty(name = 'execution.isolation.thread.interruptOnTimeout', value = 'false')])
    public Result search(@RequestBody SearchRequest request) {
        return new Result(apiIntegrator.search(request))
    }

    @RequestMapping('/details')
    @HystrixCommand(commandKey = 'Details', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")])
    public Home details(@RequestParam(value = "id", required = true) String id) {
        return apiIntegrator.getById(id)
    }

}
