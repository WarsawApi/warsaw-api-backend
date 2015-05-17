package com.ghostbuster.warsawApi.domain.internal

import com.ghostbuster.warsawApi.domain.external.warsaw.WarsawData
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

@CompileStatic
@Canonical
@EqualsAndHashCode(excludes = "apiInfo")
class SubwayStation extends Location{

    String objectId

    public SubwayStation(WarsawData entity){
        super(entity.getFirstCoordinate())
        objectId = entity.getKeyValue('OBJECTID')
    }

}
