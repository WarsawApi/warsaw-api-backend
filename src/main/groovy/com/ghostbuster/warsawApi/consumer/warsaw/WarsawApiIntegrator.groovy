package com.ghostbuster.warsawApi.consumer.warsaw

import com.ghostbuster.warsawApi.consumer.google.GeocodeServiceConsumer
import com.ghostbuster.warsawApi.consumer.importIo.ImportIoConsumer
import com.ghostbuster.warsawApi.domain.internal.*
import com.ghostbuster.warsawApi.repository.LocationRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@CompileStatic
class WarsawApiIntegrator {

    @Autowired
    private WarsawApiConsumer warsawConsumer

    @Autowired
    private GeocodeServiceConsumer geocoder

    @Autowired
    private ImportIoConsumer importIoConsumer

    @Autowired
    private LocationRepository locationRepository


    public List<Property> search(Preference preferences) {
        List<SubwayStation> stations = warsawConsumer.subwayStations

        Integer metro = 1;

        List<Property> properties = importIoConsumer.propertiesFromOtoDom as List<Property>
        properties.each {
            it.distances.metro = calculateMinDistance(stations, it)
        }

        List<LocationAble> nightLife = retrieveNightLifeLocations()
//
        return properties.sort({ calculateMinDistance(stations, it) * metro })
    }

    public Property getById(String id) {
        return warsawConsumer.getById(id)
    }

    Integer calculateMinDistance(List<? extends LocationAble> locations, Property property) {
        return property.distancesTo(locations).min()
    }

    List<LocationAble> retrieveNightLifeLocations() {
        return importIoConsumer.nigthLifeLocations.collect{
            Integer counter = 0;
            List<Location> result = locationRepository.findByAddress(it)
            if(result == null && result.isEmpty()){
                result = [geocoder.geocode(it)]
                locationRepository.save(result)
            }
            if(counter++ %5){
                Thread.sleep(1001)
            }
            return result
        }.flatten() as List<LocationAble>
    }

}