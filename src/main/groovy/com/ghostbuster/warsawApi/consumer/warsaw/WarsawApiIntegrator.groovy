package com.ghostbuster.warsawApi.consumer.warsaw

import com.ghostbuster.warsawApi.consumer.importIo.ImportIoConsumer
import com.ghostbuster.warsawApi.domain.internal.AggregatedFilter
import com.ghostbuster.warsawApi.domain.internal.Property
import com.ghostbuster.warsawApi.domain.internal.Request
import com.ghostbuster.warsawApi.scoreCalculator.GenericScoreCalculator
import com.ghostbuster.warsawApi.service.LocationService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.util.stream.Collectors

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


    public List<Property> search(Request request) {
        List<Property> properties = importIoConsumer.propertiesFromOtoDom
                .parallelStream()
                .filter(this.&filter.curry(request.filter))
                .collect(Collectors.toList())

        properties.parallelStream().forEach { it.transalateAddress(locationService) }
        properties.parallelStream().forEach { it.calculateScore(scoreCalculator, request.preference) }

        return properties.sort { properties.score }.take(10)
    }

    boolean filter(AggregatedFilter filter, Property property) {
        return filter?.applyFilter(property) ?: true
    }

    public Property getById(String id) {
        return warsawConsumer.getById(id)
    }

}