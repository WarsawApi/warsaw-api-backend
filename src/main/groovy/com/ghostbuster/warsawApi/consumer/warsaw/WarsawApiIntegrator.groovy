package com.ghostbuster.warsawApi.consumer.warsaw

import com.ghostbuster.warsawApi.domain.internal.Location
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


    public List<Property> search(Integer school, Integer metro) {
        List<SubwayStation> stations = apiProvider.getAllSubwayStations()
        List<Property> properties = apiProvider.getAllProperties()

        properties.each {
            it.distances.metro = calculateMinDistance(stations, it)
        }

        return properties.sort({calculateMinDistance(stations,it) * metro})
    }

    public Property getById(String id) {
        return apiProvider.getById(id)
    }

    Integer calculateMinDistance(List<? extends Location> locations, Property property) {
        return locations*.distanceTo(property).min()
    }

}
