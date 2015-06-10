package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.consumer.importIo.ImportIoConsumer
import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.Localizable
import com.ghostbuster.warsawApi.domain.internal.preference.Entertainment
import com.ghostbuster.warsawApi.service.LocationService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@CompileStatic
@Component
final class EntertainmentScoreCalculator implements ScoreCalculator<Entertainment> {

    private final ImportIoConsumer importIoConsumer
    private final LocationService locationService

    @Autowired
    EntertainmentScoreCalculator(ImportIoConsumer importIoConsumer, LocationService locationService) {
        this.importIoConsumer = importIoConsumer
        this.locationService = locationService
    }

    @Override
    Class<Entertainment> classOfPreference() {
        return Entertainment
    }

    @Override
    Double calculateScore(Home property, Entertainment preference) {
        Double score = 0d

        if (preference.clubs) {
            score += calculateScoreForClubs(property)
        }
        if (preference.pubs) {
            score += calculateScoreForPubs(property)
        }
        if (preference.restaurants) {
            score += calculateScoreForRestaurants(property)
        }
        return score
    }

    private Double calculateScoreForRestaurants(Home property) {
        0d
    }

    private Double calculateScoreForPubs(Home property) {
        0d
    }

    @Cacheable('minDistanceToClub')
    private Double calculateScoreForClubs(Home property) {
        List<Localizable> locations = importIoConsumer.nightLifeLocations

        return property.calculateMinDistance(locations)
    }
}
