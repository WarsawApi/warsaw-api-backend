package com.ghostbuster.warsawApi.domain.internal

import com.ghostbuster.warsawApi.domain.external.warsaw.WarsawData
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

@CompileStatic
@Canonical
@EqualsAndHashCode(excludes = "apiInfo")
class Property implements Location{

    final String id
    final String name
    final String url

    final WarsawApiInfo apiInfo = new WarsawApiInfo()

    public Property(){}

    public Property(WarsawData entity){
        coordinate = entity.getFirstCoordinate()
        id = entity.getKeyValue('ID')
        name = '?'
        url = entity.getKeyValue('OGLOSZENIE')
    }

}