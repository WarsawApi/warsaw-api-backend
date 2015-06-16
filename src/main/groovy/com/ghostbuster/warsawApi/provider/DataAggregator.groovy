package com.ghostbuster.warsawApi.provider

import com.ghostbuster.warsawApi.domain.internal.Filters
import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.SearchRequest
import com.ghostbuster.warsawApi.provider.importIo.HomeProvider
import com.ghostbuster.warsawApi.provider.warsaw.WarsawApiProvider
import com.ghostbuster.warsawApi.scoreCalculator.GenericScoreCalculator
import com.ghostbuster.warsawApi.service.LocationService
import groovy.transform.CompileStatic
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

import java.util.stream.Collectors

@Component
@CompileStatic
class DataAggregator {

    private static final Logger log = Logger.getLogger(DataAggregator)

    @Autowired
    private WarsawApiProvider warsawConsumer

    @Autowired
    private HomeProvider importIoConsumer

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

        log.error("search sorting and returning")
        return homes.sort { it.score }
                .take(10)
    }

    boolean filter(Filters filter, Home property) {
        return filter ? filter.applyFilter(property) : true
    }

    @Cacheable('zippedProperties')
    List<Home> oneCallToGetThemAll() {
        log.error('start downloading homes')
        List<Home> a = importIoConsumer.homesPage1
                .zipWith(importIoConsumer.homesPage2, { List<Home> a, List<Home> b -> a + b })
//                .zipWith(homeProvider.homesPage3, { a, b -> a + b })
//                .zipWith(homeProvider.homesPage4, { a, b -> a + b })
//                .zipWith(homeProvider.homesPage5, { a, b -> a + b })
//                .zipWith(homeProvider.homesPage6, { a, b -> a + b })
//                .zipWith(homeProvider.homesPage7, { a, b -> a + b })
//                .zipWith(homeProvider.homesPage8, { a, b -> a + b })
//                .zipWith(homeProvider.homesPage9, { a, b -> a + b })
                .toList()
                .toBlocking()
                .single()
                .first()
        log.error('end downloading homes')
        return a
    }

}