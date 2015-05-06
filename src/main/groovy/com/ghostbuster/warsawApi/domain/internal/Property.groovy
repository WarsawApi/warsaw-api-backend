package com.ghostbuster.warsawApi.domain.internal

import com.ghostbuster.warsawApi.domain.external.warsaw.ContainerEntity
import com.ghostbuster.warsawApi.domain.external.warsaw.KeyValue
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

@CompileStatic
@Canonical
@EqualsAndHashCode(excludes = "apiInfo")
class Property {

    final String id
    final String latitude
    final String longitude
    final String name
    final String url

    final WarsawApiInfo apiInfo = new WarsawApiInfo()

    public Property(){}

    public Property(ContainerEntity entity){
        latitude = entity?.geometry?.coordinates?.first().latitude
        longitude = entity?.geometry?.coordinates?.first().longitude
        id = entity.properties.find {KeyValue kv -> kv.key == 'ID' }?.value
        name = '?'
        url = entity.properties.find { KeyValue kv -> kv.key == 'OGLOSZENIE' }?.value
    }

}