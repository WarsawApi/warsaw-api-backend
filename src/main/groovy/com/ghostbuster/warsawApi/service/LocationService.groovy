package com.ghostbuster.warsawApi.service

import com.ghostbuster.warsawApi.domain.internal.EmptyLocation
import com.ghostbuster.warsawApi.domain.internal.Localizable
import com.ghostbuster.warsawApi.domain.internal.Location
import com.ghostbuster.warsawApi.provider.google.GeocodeServiceProvider
import com.ghostbuster.warsawApi.repository.LocationRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.concurrent.atomic.AtomicInteger

@CompileStatic
@Service
class LocationService {

    private final AtomicInteger counter = new AtomicInteger(0)

    private final LocationRepository repository
    private final GeocodeServiceProvider geocoder

    @Autowired
    LocationService(LocationRepository repository, GeocodeServiceProvider geocoder) {
        this.repository = repository
        this.geocoder = geocoder
    }

    @Cacheable('locations')
    @Transactional
    Localizable findByAddress(String address) {
        Localizable result = mapToEmptyIfNeeded(repository.findByAddress(address))

        if (result == null) {
            result = geocodeAddress(address)
            repository.save((Location) result)
            waitEvery5requests()
        }

        return result
    }

    private Localizable mapToEmptyIfNeeded(Location location) {
        if (!location.address) {
            return new EmptyLocation()
        }
        return location
    }

    private void waitEvery5requests() {
        if (this.counter.incrementAndGet() % 5) {
            Thread.sleep(1001)
        }
    }

    @Cacheable('locations')
    @Transactional
    List<Location> findByAddresses(List<String> addresses) {
        List<Localizable> locations = repository.findByAddressIn(addresses).collect { mapToEmptyIfNeeded(it) }
        List<String> notFound = addresses - locations*.address
        List<Location> geocoded = notFound.collect {
            if (this.counter.incrementAndGet() % 5) {
                Thread.sleep(1001)
            }
            return geocodeAddress(it)
        }

        List<Localizable> newlySaved = repository.save(geocoded) as List<Localizable>
        return newlySaved + locations
    }

    private Location geocodeAddress(String address) {
        return geocoder.geocode(address)
    }


}
