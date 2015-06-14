package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.consumer.importIo.ImportIoConsumer
import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.Localizable
import com.ghostbuster.warsawApi.domain.internal.preference.Culture
import groovy.transform.CompileStatic
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@CompileStatic
@Component
class CultureScoreCalculator implements ScoreCalculator<Culture> {

    private final ImportIoConsumer importIoConsumer

    @Override
    Class<Culture> classOfPreference() {
        return Culture
    }

    @Override
    Double calculateScore(Home property, Culture preference) {
        Double score = 0d

        if (preference.cinemas) {
            score += calculateScoreForCinemas(property)
        }
        if (preference.exhibitions) {
            score += calculateScoreForExhibitions(property)
        }
        if (preference.theaters) {
            score += calculateScoreForTheaters(property)
        }
        return score
    }

    @Cacheable('minDistanceToTheater')
    private Double calculateScoreForTheaters(Home home) {
        List<Localizable> locations = importIoConsumer.theatersLocations

        return home.calculateMinDistance(locations)
    }

    @Cacheable('minDistanceToExhibition')
    private Double calculateScoreForExhibitions(Home home) {
        List<Localizable> locations = importIoConsumer.exhibitionsLocations

        return home.calculateMinDistance(locations)
    }

    @Cacheable('minDistanceToCinema')
    private Double calculateScoreForCinemas(Home home) {
        List<Localizable> locations = importIoConsumer.cinemasLocations

        return home.calculateMinDistance(locations)
    }
}
