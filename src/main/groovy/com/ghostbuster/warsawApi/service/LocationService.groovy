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

    private final AtomicInteger counter = new AtomicInteger(0)

    private final LocationRepository repository
    private final GeocodeServiceConsumer geocoder

    @Autowired
    LocationService(LocationRepository repository, GeocodeServiceConsumer geocoder) {
        this.repository = repository
        this.geocoder = geocoder
    }

    @Cacheable('locations')
    Location findByAddress(String address) {
        Location result = repository.findByAddress(address)

        if (result == null) {
            result = geocodeAddress(address)
            repository.save(result)
            if (this.counter.incrementAndGet() % 5) {
                Thread.sleep(1001)
            }
        }

        return result
    }

    //throwing error for some reason probably because of
//    @Cacheable('locations')
//    List<Location> findByAddresses(List<String> addresses) {
//        List<Location> locations = repository.findByAddressIn(addresses)
//
//        List<String> notFound = addresses - locations*.address
//
//        List<Location> newlySaved = repository.save(notFound.collect { geocodeAddress(it) })
//
//        return newlySaved + locations
//    }

    private Location geocodeAddress(String address) {
        return geocoder.geocode(address)
    }


}
