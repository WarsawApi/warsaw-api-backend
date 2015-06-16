package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.Localizable
import com.ghostbuster.warsawApi.domain.internal.preference.Entertainment
import com.ghostbuster.warsawApi.provider.importIo.ClubProvider
import com.ghostbuster.warsawApi.provider.importIo.PubProvider
import com.ghostbuster.warsawApi.provider.importIo.RestaurantProvider
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@CompileStatic
@Component
class EntertainmentScoreCalculator implements ScoreCalculator<Entertainment> {

    private final RestaurantProvider restaurantProvider
    private final PubProvider pubProvider
    private final ClubProvider clubProvider

    @Autowired
    EntertainmentScoreCalculator(RestaurantProvider restaurantProvider, PubProvider pubProvider, ClubProvider clubProvider) {
        this.restaurantProvider = restaurantProvider
        this.pubProvider = pubProvider
        this.clubProvider = clubProvider
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

    @Cacheable('minDistanceToRestaurant')
    private Double calculateScoreForRestaurants(Home property) {
        List<Localizable> locations = restaurantProvider.restaurantsLocations

        return property.calculateMinDistance(locations)
    }

    @Cacheable('minDistanceToPub')
    private Double calculateScoreForPubs(Home property) {
        List<Localizable> locations = pubProvider.pubsLocations

        return property.calculateMinDistance(locations)
    }

    @Cacheable('minDistanceToClub')
    private Double calculateScoreForClubs(Home property) {
        List<Localizable> locations = clubProvider.clubsLocations

        return property.calculateMinDistance(locations)
    }
}
