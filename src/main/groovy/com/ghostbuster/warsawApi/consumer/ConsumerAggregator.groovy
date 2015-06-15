package com.ghostbuster.warsawApi.consumer

import com.ghostbuster.warsawApi.consumer.importIo.ImportIoConsumer
import com.ghostbuster.warsawApi.consumer.warsaw.WarsawApiConsumer
import com.ghostbuster.warsawApi.domain.internal.Filters
import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.SearchRequest
import com.ghostbuster.warsawApi.scoreCalculator.GenericScoreCalculator
import com.ghostbuster.warsawApi.service.LocationService
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.util.stream.Collectors

@Component
@CompileStatic
class ConsumerAggregator {

    @Autowired
    private WarsawApiConsumer warsawConsumer

    @Autowired
    private ImportIoConsumer importIoConsumer

    @Autowired
    private LocationService locationService
    @Autowired
    private GenericScoreCalculator scoreCalculator


    public List<Home> search(SearchRequest request) {
        List<Home> homes = oneCallToGetThemAll()
                .parallelStream()
                .filter(this.&filter.curry(request.filters))
                .map { it.transalateAddress(locationService) }
                .map { it.calculateScore(scoreCalculator, request.preferences) }
                .collect(Collectors.toList())

        return homes.sort { it.score }
                .take(10)
    }

    boolean filter(Filters filter, Home property) {
        return filter ? filter.applyFilter(property) : true
    }

    public Home getById(String id) {
        return warsawConsumer.getById(id)
    }

    @CompileDynamic
    List<Home> oneCallToGetThemAll() {
        return importIoConsumer.homesPage1
                .zipWith(importIoConsumer.homesPage1, { a, b -> a + b })
                .zipWith(importIoConsumer.homesPage2, { a, b -> a + b })
                .zipWith(importIoConsumer.homesPage3, { a, b -> a + b })
                .zipWith(importIoConsumer.homesPage4, { a, b -> a + b })
                .zipWith(importIoConsumer.homesPage5, { a, b -> a + b })
                .zipWith(importIoConsumer.homesPage6, { a, b -> a + b })
                .zipWith(importIoConsumer.homesPage7, { a, b -> a + b })
                .zipWith(importIoConsumer.homesPage8, { a, b -> a + b })
                .toList()
                .toBlocking()
                .single()
                .first()
    }

}