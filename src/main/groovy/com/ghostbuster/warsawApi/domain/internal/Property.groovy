package com.ghostbuster.warsawApi.domain.internal

import com.ghostbuster.warsawApi.domain.external.warsaw.WarsawData
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

@CompileStatic
@Canonical
@EqualsAndHashCode(excludes = "apiInfo")
class Property extends Location{

    final String id
    final String address
    final String url
    final Distances distances

    final WarsawApiInfo apiInfo = new WarsawApiInfo()

    public Property(){}

    public Property(WarsawData entity){
        super(entity.getFirstCoordinate())
        id = entity.getKeyValue('ID')
        address = '?'
        url = entity.getKeyValue('OGLOSZENIE')
    }

}