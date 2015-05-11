package com.ghostbuster.warsawApi.domain.internal

import com.ghostbuster.warsawApi.domain.external.warsaw.WarsawData
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

@CompileStatic
@Canonical
@EqualsAndHashCode(excludes = "apiInfo")
class SubwayStation implements Location{

    String id

    public SubwayStation(WarsawData entity){
        coordinate = entity.getFirstCoordinate()
        id = entity.getKeyValue('OBJECTID')
    }

}
