package com.ghostbuster.warsawApi

import com.ghostbuster.warsawApi.consumer.warsaw.WarsawApiRequestBuilder
import spock.lang.Specification

class WarsawApiRequestBuilderSpec extends Specification {

    def "Should return url with api key"(){
        given:
            def builder = WarsawApiRequestBuilder.forPropertyPurchase()
        expect:
            builder.build().contains('&apikey=f466c384-f5cc-4787-af44-1468697daf47')
    }

    def "Should return url with proper api id"(){
        given:
        def rentApiBuilder = WarsawApiRequestBuilder.forPropertyRent()
        def purchaseApiBuilder = WarsawApiRequestBuilder.forPropertyPurchase()

        expect:
        rentApiBuilder.build().contains('id=45ba10ab-6562-49ce-b572-6c9b999464d6')
        purchaseApiBuilder.build().contains('id=baa1d9c9-4a90-401d-b215-57b1ed09e694')
    }

    def "Should return url with limit appended"(){
        given:
        def builder = WarsawApiRequestBuilder.forPropertyPurchase().limitResults(5)
        expect:
        builder.build().contains('&limit=5')
    }

    def "Shoould return proper url"(){
        given:
        def builder = WarsawApiRequestBuilder.forPropertyPurchase().limitResults(5)
        expect:
        builder.build() == 'https://api.um.warszawa.pl/api/action/wfsstore_get/?id=baa1d9c9-4a90-401d-b215-57b1ed09e694&apikey=f466c384-f5cc-4787-af44-1468697daf47&limit=5'
    }
}
