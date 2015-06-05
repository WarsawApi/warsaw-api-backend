package com.ghostbuster.warsawApi.domain.internal

import com.ghostbuster.warsawApi.domain.external.warsaw.WarsawData
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

@CompileStatic
@Canonical
@EqualsAndHashCode(excludes = "apiInfo")
class SubwayStation implements LocationAble {

    String objectId

    @Delegate(includes = ['distanceTo', 'longitude', 'latitude'])
    Location location

    public SubwayStation(WarsawData entity){
        objectId = entity.getKeyValue('OBJECTID')
        location = new Location(entity.getFirstCoordinate())
    }

}
