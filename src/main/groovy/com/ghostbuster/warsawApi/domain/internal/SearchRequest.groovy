package com.ghostbuster.warsawApi.domain.internal

import groovy.transform.CompileStatic

@CompileStatic
class SearchRequest {

    Preference preferences
    AggregatedFilter filters

}
