package com.ghostbuster.warsawApi.domain.internal

import com.ghostbuster.warsawApi.domain.external.warsaw.WarsawData
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

@CompileStatic
@Canonical
@EqualsAndHashCode(excludes = "apiInfo")
class Property extends Location{

    String objectId
    String address
    String url
    Distances distances
    String imageUrl

    final WarsawApiInfo apiInfo = new WarsawApiInfo()

    public Property(){}

    public Property(WarsawData entity){
        super(entity.getFirstCoordinate())
        objectId = entity.getKeyValue('ID')
        address = '?'
        url = entity.getKeyValue('OGLOSZENIE')
        distances = new Distances()
    }

}