package com.ghostbuster.warsawApi.domain.external.warsaw

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.ghostbuster.warsawApi.domain.common.Coordinate
import groovy.transform.CompileStatic

@CompileStatic
@JsonIgnoreProperties(ignoreUnknown = true)
class WarsawData {

    Geometry geometry
    List<KeyValue> properties


    String getKeyValue(String key){
        return properties?.find {KeyValue kv -> kv.key == 'ID' }?.value
    }

    Coordinate getFirstCoordinate(){
        geometry?.coordinates?.first()
    }
}
