package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.Localizable
import com.ghostbuster.warsawApi.domain.internal.preference.Culture
import com.ghostbuster.warsawApi.provider.importIo.CinemaProvider
import com.ghostbuster.warsawApi.provider.importIo.ExhibitionProvider
import com.ghostbuster.warsawApi.provider.importIo.TheaterProvider
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@CompileStatic
@Component
class CultureScoreCalculator implements ScoreCalculator<Culture> {

    private final TheaterProvider theaterProvider
    private final ExhibitionProvider exhibitionProvider
    private final CinemaProvider cinemaProvider

    @Autowired
    CultureScoreCalculator(TheaterProvider theaterProvider, ExhibitionProvider exhibitionProvider, CinemaProvider cinemaProvider) {
        this.theaterProvider = theaterProvider
        this.exhibitionProvider = exhibitionProvider
        this.cinemaProvider = cinemaProvider
    }

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

    @Cacheable(value = 'minDistanceToTheater', unless = "#result < 1")
    private Double calculateScoreForTheaters(Home home) {
        List<Localizable> locations = theaterProvider.theatersLocations

        return home.calculateMinDistance(locations)
    }

    @Cacheable(value = 'minDistanceToExhibition', unless = "#result < 1")
    private Double calculateScoreForExhibitions(Home home) {
        List<Localizable> locations = exhibitionProvider.exhibitionsLocations

        return home.calculateMinDistance(locations)
    }

    @Cacheable(value = 'minDistanceToCinema', unless = "#result < 1")
    private Double calculateScoreForCinemas(Home home) {
        List<Localizable> locations = cinemaProvider.cinemasLocations

        return home.calculateMinDistance(locations)
    }
}
