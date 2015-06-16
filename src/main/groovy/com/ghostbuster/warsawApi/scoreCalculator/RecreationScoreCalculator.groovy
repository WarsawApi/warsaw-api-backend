package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.Localizable
import com.ghostbuster.warsawApi.domain.internal.Location
import com.ghostbuster.warsawApi.domain.internal.preference.Recreation
import com.ghostbuster.warsawApi.provider.importIo.ParkProvider
import com.ghostbuster.warsawApi.provider.warsaw.WarsawApiConsumer
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@CompileStatic
@Component
class RecreationScoreCalculator implements ScoreCalculator<Recreation> {

    private final WarsawApiConsumer warsawApi
    private final ParkProvider parkProvider

    @Autowired
    RecreationScoreCalculator(WarsawApiConsumer warsawApi, ParkProvider parkProvider) {
        this.warsawApi = warsawApi
        this.parkProvider = parkProvider
    }

    @Override
    Class<Recreation> classOfPreference() {
        return Recreation
    }

    @Override
    Double calculateScore(Home property, Recreation preference) {
        Double score = 0d

        if (preference.bikes) {
            score += calculateScoreForBikes(property)
        }
        if (preference.parks) {
            score += calculateScoreForParks(property)
        }
        if (preference.reservoirs) {
            score += calculateScoreForReservoirs(property)
        }
        return score
    }

    @Cacheable('minDistanceToPark')
    private Double calculateScoreForParks(Home property) {
        List<Localizable> parksLocations = parkProvider.parksLocations

        return property.calculateMinDistance(parksLocations)
    }

    private Double calculateScoreForReservoirs(Home property) {
        return 0d
    }

    @Cacheable('minDistanceToBike')
    private Double calculateScoreForBikes(Home property) {
        List<Location> bikeStations = warsawApi.bikesStations

        return property.calculateMinDistance(bikeStations)
    }
}
