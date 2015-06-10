package com.ghostbuster.warsawApi.domain.internal

import com.ghostbuster.warsawApi.domain.external.warsaw.WarsawData
import com.ghostbuster.warsawApi.domain.internal.preference.PreferenceAble
import com.ghostbuster.warsawApi.scoreCalculator.GenericScoreCalculator
import com.ghostbuster.warsawApi.service.LocationService
import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
@Canonical
final class Home implements Localizable {

    String objectId
    String url
    Double score
    String price
    String imageUrl

    String measurement
    String roomsCount

    @Delegate
    Location location = new Location()

    final private WarsawApiInfo apiInfo = new WarsawApiInfo()

    public Home() {}

    public Home(WarsawData entity) {
        objectId = entity.getKeyValue('ID')
        location.address = '?'
        url = entity.getKeyValue('OGLOSZENIE')
        location = new Location(entity.getFirstCoordinate())
    }

    Double calculateMinDistance(List<? extends Localizable> locations) {
        return distancesTo(locations).min() ?: 0d
    }

    Home calculateScore(GenericScoreCalculator scoreCalculator, Preferences preferences) {
        score = preferences.extractPropertiesAsStream().mapToDouble { v -> scoreCalculator.calculateScore(this, v as PreferenceAble) }.sum()
        return this
    }

    Home transalateAddress(LocationService locationService) {
        location = locationService.findByAddress(address)
        return this
    }
}