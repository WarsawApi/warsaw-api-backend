package com.ghostbuster.warsawApi.consumer.warsaw

import com.ghostbuster.warsawApi.domain.external.warsaw.Coordinate

class WarsawApiRequestBuilder {

    private final static String API_KEY = 'f466c384-f5cc-4787-af44-1468697daf47'
    private final static String PROPERTY_RENT = '45ba10ab-6562-49ce-b572-6c9b999464d6'
    private final static String PROPERTY_PURCHASE = 'baa1d9c9-4a90-401d-b215-57b1ed09e694'
    private final static String SUBWAY_ENTRANCE = '0ac7f6d1-a26b-430f-9e3d-a41c5356b9a3'

    private final StringBuilder url = new StringBuilder("https://api.um.warszawa.pl/api/action/wfsstore_get/?id=")

    private WarsawApiRequestBuilder(String apiId){
        url.append(apiId).append("&apikey=${API_KEY}")
    }

    static WarsawApiRequestBuilder forPropertyRent(){
        return new WarsawApiRequestBuilder(PROPERTY_RENT)
    }

    static WarsawApiRequestBuilder forPropertyPurchase(){
        return new WarsawApiRequestBuilder(PROPERTY_PURCHASE)
    }

    static WarsawApiRequestBuilder forSubwayStations(){
        return new WarsawApiRequestBuilder(SUBWAY_ENTRANCE)
    }

    WarsawApiRequestBuilder limitResults(Integer limit){
        url.append("&limit=${limit}")
        return this
    }

    WarsawApiRequestBuilder notFarThen(Coordinate from, Integer meters){
        url.append("&circle=${from.longitude},${from.latitude},${meters}")
        return this
    }

    WarsawApiRequestBuilder withFilter(String filter) {
        url.append("&filter=${filter}")
        return this
    }

    String build(){
        return url.toString()
    }
}
