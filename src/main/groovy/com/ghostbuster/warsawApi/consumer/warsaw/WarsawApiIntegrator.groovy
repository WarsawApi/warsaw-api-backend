package com.ghostbuster.warsawApi.consumer.warsaw

import com.ghostbuster.warsawApi.consumer.importIo.ImportIoConsumer
import com.ghostbuster.warsawApi.domain.internal.Filters
import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.SearchRequest
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


    public List<Home> search(SearchRequest request) {
        return importIoConsumer.propertiesFromOtoDom
                .parallelStream()
                .filter(this.&filter.curry(request.filters))
                .map { it.transalateAddress(locationService) }
                .map { it.calculateScore(scoreCalculator, request.preferences) }
                .collect(Collectors.toList())
                .sort { properties.score }
                .take(10)
    }

    boolean filter(Filters filter, Home property) {
        return filter?.applyFilter(property) ?: true
    }

    public Home getById(String id) {
        return warsawConsumer.getById(id)
    }

}