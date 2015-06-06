package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.consumer.importIo.ImportIoConsumer
import com.ghostbuster.warsawApi.domain.internal.Localizable
import com.ghostbuster.warsawApi.domain.internal.Property
import com.ghostbuster.warsawApi.domain.internal.preference.Entertainment
import com.ghostbuster.warsawApi.service.LocationService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@CompileStatic
@Component
class EntertainmentScoreCalculator implements ScoreCalculator<Entertainment> {

    @Autowired
    private ImportIoConsumer importIoConsumer

    @Autowired
    private LocationService locationService

    @Override
    Class<Entertainment> classOfPreference() {
        return Entertainment
    }

    @Override
    Double calculateScore(Property property, Entertainment preference) {
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

    private Double calculateScoreForRestaurants(Property property) {
        0d
    }

    private Double calculateScoreForPubs(Property property) {
        0d
    }

    @Cacheable('minDistanceToClub')
    private Double calculateScoreForClubs(Property property) {
        List<Localizable> locations = importIoConsumer.nightLifeLocations

        return property.calculateMinDistance(locations)
    }
}
