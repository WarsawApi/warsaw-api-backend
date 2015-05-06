package com.ghostbuster.warsawApi.controller

import com.ghostbuster.warsawApi.consumer.warsaw.WarsawApiConsumer
import com.ghostbuster.warsawApi.domain.internal.Property
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CompileStatic
@RestController
class PropertyController {

    @Autowired
    private WarsawApiConsumer propertyProvider

    @RequestMapping('/search')
    public List<Property> search(@RequestParam(value="school", required = false) Integer school, @RequestParam(value="metro", required = false) Integer metro){
        return propertyProvider.search('1')
    }

    @RequestMapping('/details')
    public Property details(@RequestParam(value = "id", required = true) String id) {
        return propertyProvider.getById(id)
    }
}
