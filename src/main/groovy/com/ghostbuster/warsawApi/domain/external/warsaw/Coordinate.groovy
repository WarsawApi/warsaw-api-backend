package com.ghostbuster.warsawApi.domain.external.warsaw

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor

@CompileStatic
@TupleConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class Coordinate {
    final String latitude
    final String longitude
}
