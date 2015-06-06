package com.ghostbuster.warsawApi.consumer.warsaw

import com.ghostbuster.warsawApi.consumer.importIo.ImportIoConsumer
import com.ghostbuster.warsawApi.domain.internal.Preference
import com.ghostbuster.warsawApi.domain.internal.Property
import com.ghostbuster.warsawApi.scoreCalculator.GenericScoreCalculator
import com.ghostbuster.warsawApi.service.LocationService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@CompileStatic
class WarsawApiIntegrator {

    @Autowired
    private WarsawApiConsumer warsawConsumer

    @Autowired
    private ImportIoConsumer importIoConsumer

    @Autowired
    private LocationService locationService
    @Autowired
    private GenericScoreCalculator scoreCalculator


    public List<Property> search(Preference preferences) {
        List<Property> properties = importIoConsumer.propertiesFromOtoDom as List<Property>

        properties.parallelStream().forEach { it.location = locationService.findByAddress(it.address) }
        properties.parallelStream().forEach { it.calculateScore(scoreCalculator, preferences) }

        return properties.sort { properties.score }
    }

    public Property getById(String id) {
        return warsawConsumer.getById(id)
    }

}