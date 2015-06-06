package com.ghostbuster.warsawApi.scoreCalculator

import com.ghostbuster.warsawApi.consumer.importIo.ImportIoConsumer
import com.ghostbuster.warsawApi.domain.internal.Localizable
import com.ghostbuster.warsawApi.domain.internal.Property
import com.ghostbuster.warsawApi.domain.internal.preference.Entertainment
import com.ghostbuster.warsawApi.service.LocationService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
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
        return 0d
    }

    private List<Localizable> retrieveNightLifeLocations() {
        return importIoConsumer.nigthLifeLocations.collect {
            locationService.findByAddress(it)
        }
    }
}
