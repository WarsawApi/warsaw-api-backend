package com.ghostbuster.warsawApi.domain.internal

import com.ghostbuster.warsawApi.consumer.warsaw.NativeJobsParallerer
import com.ghostbuster.warsawApi.domain.external.warsaw.WarsawData
import com.ghostbuster.warsawApi.scoreCalculator.GenericScoreCalculator
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

    Location location = new Location()

    final private WarsawApiInfo apiInfo = new WarsawApiInfo()

    public Property() {}

    public Property(WarsawData entity) {
        objectId = entity.getKeyValue('ID')
        location.address = '?'
        url = entity.getKeyValue('OGLOSZENIE')
        location = new Location(entity.getFirstCoordinate())
    }

    Double calculateMinDistance(List<? extends Localizable> locations) {
        return distancesTo(locations).min()
    }

    Double calculateScore(GenericScoreCalculator scoreCalculator, Preference preferences) {
        return NativeJobsParallerer.executeJob(scoreCalculator, this, preferences.extractProperties())
    }

    @Override
    String getLatitude() { location.latitude }

    @Override
    String getLongitude() { location.longitude }

    @Override
    String getAddress() { location.address }

    @Override
    Double distanceTo(Localizable location) { location.distanceTo(location) }

    @Override
    List<Double> distancesTo(List<Localizable> locations) { location.distancesTo(locations) }
}