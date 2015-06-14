package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.consumer.importIo.ImportIoConsumer
import com.ghostbuster.warsawApi.consumer.warsaw.WarsawApiConsumer
import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.Localizable
import com.ghostbuster.warsawApi.domain.internal.Location
import com.ghostbuster.warsawApi.domain.internal.preference.Recreation
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@CompileStatic
@Component
class RecreationScoreCalculator implements ScoreCalculator<Recreation> {

    private final WarsawApiConsumer warsawApi

    private final ImportIoConsumer importIoConsumer

    @Autowired
    RecreationScoreCalculator(WarsawApiConsumer warsawApi, ImportIoConsumer importIoConsumer) {
        this.warsawApi = warsawApi
        this.importIoConsumer = importIoConsumer
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
        List<Localizable> parksLocations = importIoConsumer.parksLocations

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
