package com.ghostbuster.warsawApi.consumer.warsaw

import com.ghostbuster.warsawApi.domain.internal.Property
import com.ghostbuster.warsawApi.domain.internal.SubwayStation
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@CompileStatic
class WarsawApiIntegrator {

    @Autowired
    private WarsawApiConsumer apiProvider


    public List<Property> search(Integer school, Integer metro){
        List<SubwayStation> stations = apiProvider.getAllSubwayStations()
        return apiProvider.getAllProperties().sort(this.&calculateScoreForProperty.curry(stations,metro))
    }

    public Property getById(String id) {
        return apiProvider.getById(id)
    }

    Integer calculateScoreForProperty(List<SubwayStation> stations, Integer metroWeigth,Property property){
        return stations*.distanceTo(property).min() * metroWeigth
    }

}
