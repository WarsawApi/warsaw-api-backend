package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.Location
import com.ghostbuster.warsawApi.domain.internal.preference.Nearby
import com.ghostbuster.warsawApi.service.LocationService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@CompileStatic
@Component
class NearbyScoreCalculator implements ScoreCalculator<Nearby> {

    LocationService locationService

    @Autowired
    NearbyScoreCalculator(LocationService locationService) {
        this.locationService = locationService
    }

    @Override
    Class<Nearby> classOfPreference() {
        return Nearby
    }

    @Override
    Double calculateScore(Home property, Nearby preference) {
        Double score = 0d

        if (!StringUtils.isEmpty(preference.phrase)) {
            score += calculateScoreForPhrase(property, preference.phrase)
        }

        Location loc = preference.location
        if (loc != null && !StringUtils.isEmpty(loc.latitude) && !StringUtils.isEmpty(loc.latitude)) {
            score += calculateScoreForLocation(property, loc)
        }

        return score
    }

    @Cacheable('minDistanceToNearby')
    private Double calculateScoreForLocation(Home property, Location location) {
        return property.distanceTo(location)
    }

    @Cacheable('minDistanceToPhrase')
    private Double calculateScoreForPhrase(Home property, String address) {
        Location location = locationService.findByAddress(address)

        return property.distanceTo(location)
    }
}
