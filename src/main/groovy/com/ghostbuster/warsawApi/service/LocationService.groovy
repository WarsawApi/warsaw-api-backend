package com.ghostbuster.warsawApi.service

import com.ghostbuster.warsawApi.consumer.google.GeocodeServiceConsumer
import com.ghostbuster.warsawApi.domain.internal.Location
import com.ghostbuster.warsawApi.repository.LocationRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

import java.util.concurrent.atomic.AtomicInteger

@CompileStatic
@Service
class LocationService {

    @Autowired
    LocationRepository repository

    @Autowired
    GeocodeServiceConsumer geocoder

    private AtomicInteger counter = new AtomicInteger(0)

    @Cacheable('locations')
    Location findByAddress(String address) {
        List<Location> result = repository.findByAddress(address)

        if (result == null || result.isEmpty()) {
            result = geocodeAddress(address)
            repository.save(result)
        }

        if (this.counter.incrementAndGet() % 5) {
            Thread.sleep(1001)
        }
        return result.first()
    }

    private List<Location> geocodeAddress(String address) {
        return [geocoder.geocode(address)]
    }


}
