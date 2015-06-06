package com.ghostbuster.warsawApi.domain.internal

import com.ghostbuster.warsawApi.domain.external.warsaw.WarsawData
import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
@Canonical
class Property implements Localizable {

    String objectId
    String url
    Double score
    String price
    String imageUrl

    String space
    String roomsCount

    @Delegate
//(includes = ['distanceTo', 'distancesTo', 'longitude', 'latitude'])
    Location location = new Location()

    final private WarsawApiInfo apiInfo = new WarsawApiInfo()

    public Property(){}

    public Property(WarsawData entity){
        objectId = entity.getKeyValue('ID')
        address = '?'
        url = entity.getKeyValue('OGLOSZENIE')
        location = new Location(entity.getFirstCoordinate())
    }

    Double calculateMinDistance(List<? extends Localizable> locations) {
        return distancesTo(locations).min()
    }

    String getLatitude() {}

    String getLongitude() {}

    Double distanceTo(Localizable location) {}

    List<Double> distancesTo(List<Localizable> locations) {}

}