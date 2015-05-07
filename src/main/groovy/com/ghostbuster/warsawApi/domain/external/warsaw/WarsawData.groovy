package com.ghostbuster.warsawApi.domain.external.warsaw

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.CompileStatic

@CompileStatic
@JsonIgnoreProperties(ignoreUnknown = true)
class WarsawData {

    Geometry geometry
    List<KeyValue> properties
}
