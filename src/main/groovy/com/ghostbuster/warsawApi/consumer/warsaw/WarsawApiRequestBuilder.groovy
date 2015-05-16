package com.ghostbuster.warsawApi.consumer.warsaw

import com.ghostbuster.warsawApi.domain.external.warsaw.Coordinate

class WarsawApiRequestBuilder {

    private final static String API_KEY = 'f466c384-f5cc-4787-af44-1468697daf47'
    private final static String PROPERTY_RENT = '45ba10ab-6562-49ce-b572-6c9b999464d6'
    private final static String PROPERTY_PURCHASE = 'baa1d9c9-4a90-401d-b215-57b1ed09e694'
    private final static String SUBWAY_ENTRANCE = '0ac7f6d1-a26b-430f-9e3d-a41c5356b9a3'
    private final static String CYCLES_STATION = 'd2f0c41f-cda1-440a-8a27-f01f724529f8'
    private final static String PARK_AND_RIDE = '157648fd-a603-4861-af96-884a8e35b155'
    private final static String THEATERS ='e26218cb-61ec-4ccb-81cc-fd19a6fee0f8'

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

    static WarsawApiRequestBuilder forCyclesStation(){
        return new WarsawApiRequestBuilder(CYCLES_STATION)
    }

    static WarsawApiRequestBuilder forParkAndRide(){
        return new WarsawApiRequestBuilder(PARK_AND_RIDE)
    }

    static WarsawApiRequestBuilder forTheaters(){
        return new WarsawApiRequestBuilder(THEATERS)
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
