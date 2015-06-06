package com.ghostbuster.warsawApi.service

import com.ghostbuster.warsawApi.consumer.google.GeocodeServiceConsumer
import com.ghostbuster.warsawApi.domain.internal.Location
import com.ghostbuster.warsawApi.repository.LocationRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@CompileStatic
@Service
class LocationService {

    @Autowired
    LocationRepository repository

    @Autowired
    GeocodeServiceConsumer geocoder

    private int counter = 0

    Location findByAddress(String address) {
        List<Location> result = repository.findByAddress(address)

        if (result == null || result.isEmpty()) {
            result = geocodeAddress(address)
            repository.save(result)
        }

        if (this.counter++ % 5) {
            Thread.sleep(1001)
        }
        return result.first()
    }

    private List<Location> geocodeAddress(String address) {
        return [geocoder.geocode(address)]
    }


}
