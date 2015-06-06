package com.ghostbuster.warsawApi.consumer.warsaw

import com.ghostbuster.warsawApi.consumer.importIo.ImportIoConsumer
import com.ghostbuster.warsawApi.domain.internal.Preference
import com.ghostbuster.warsawApi.domain.internal.Property
import com.ghostbuster.warsawApi.domain.internal.preference.PreferenceAble
import com.ghostbuster.warsawApi.repository.LocationRepository
import com.ghostbuster.warsawApi.scoreCalculator.GenericScoreCalculator
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
    private LocationRepository locationRepository

    @Autowired
    private GenericScoreCalculator scoreCalculator


    public List<Property> search(Preference preferences) {
        List<Property> properties = importIoConsumer.propertiesFromOtoDom as List<Property>

        properties.each {
            it.score = NativeJobsParallerer.executeJob(scoreCalculator, it, extractProperties(preferences))
        }

        return properties.sort { properties.score }
    }

    private Collection<? extends PreferenceAble> extractProperties(Preference preferences) {
        return preferences.properties.findAll { k, v -> k != 'class' }.values().findAll {
            it != null
        } as Collection<? extends PreferenceAble>
    }

    public Property getById(String id) {
        return warsawConsumer.getById(id)
    }

}