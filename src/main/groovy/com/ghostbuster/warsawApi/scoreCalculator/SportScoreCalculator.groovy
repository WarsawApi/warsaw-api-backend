package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.Localizable
import com.ghostbuster.warsawApi.domain.internal.preference.Sport
import com.ghostbuster.warsawApi.provider.importIo.FitnessProvider
import com.ghostbuster.warsawApi.provider.importIo.PoolProvider
import com.ghostbuster.warsawApi.provider.importIo.TennisProvider
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@CompileStatic
@Component
class SportScoreCalculator implements ScoreCalculator<Sport> {

    private final FitnessProvider fitnessProvider
    private final TennisProvider tennisProvider
    private final PoolProvider poolProvider

    @Autowired
    SportScoreCalculator(FitnessProvider fitnessProvider, TennisProvider tennisProvider, PoolProvider poolProvider) {
        this.fitnessProvider = fitnessProvider
        this.tennisProvider = tennisProvider
        this.poolProvider = poolProvider
    }

    @Override
    Class<Sport> classOfPreference() {
        return Sport
    }

    @Override
    Double calculateScore(Home property, Sport preference) {
        Double score = 0d

        if (preference.fitness) {
            score += calculateScoreForFitness(property)
        }
        if (preference.pools) {
            score += calculateScoreForPools(property)
        }
        if (preference.tennis) {
            score += calculateScoreForTennis(property)
        }

        return score
    }

    @Cacheable('minDistanceToTennis')
    private Double calculateScoreForTennis(Home property) {
        List<Localizable> tennis = tennisProvider.tenninsLocations

        return property.calculateMinDistance(tennis)
    }

    @Cacheable('minDistanceToPool')
    private Double calculateScoreForPools(Home property) {
        List<Localizable> pools = poolProvider.poolsLocations

        return property.calculateMinDistance(pools)
    }

    @Cacheable('minDistanceToFitness')
    private Double calculateScoreForFitness(Home property) {
        List<Localizable> fitness = fitnessProvider.fitnessLocations

        return property.calculateMinDistance(fitness)
    }
}
