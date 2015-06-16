package com.ghostbuster.warsawApi.provider.warsaw

import spock.lang.Specification

final class WarsawApiRequestBuilderSpec extends Specification {

    def "Should return url with api key"() {
        given:
        String apiKey = 'f466c384-f5cc-4787-af44-1468697daf47'

        when:
        String url = WarsawApiRequestBuilder.forPropertyPurchase().build()

        then:
        url.contains("&apikey=${apiKey}")
    }

    def "Should return url with proper api id for every api"() {

        expect:
        builder.build().contains("id=${result}")

        where:
        builder                                       | result
        WarsawApiRequestBuilder.forPropertyRent()     | '45ba10ab-6562-49ce-b572-6c9b999464d6'
        WarsawApiRequestBuilder.forPropertyPurchase() | 'baa1d9c9-4a90-401d-b215-57b1ed09e694'

    }

    def "Should return url with limit appended"() {
        given:
        String limit = '5'

        when:
        String url = WarsawApiRequestBuilder.forPropertyPurchase().limitResults(5).build()

        then:
        url.contains("&limit=${limit}")
    }

    def "WarsawApiRequestBuilder should return proper url"() {
        given:
        String url = 'https://api.um.warszawa.pl/api/action/wfsstore_get/?id=45ba10ab-6562-49ce-b572-6c9b999464d6&apikey=f466c384-f5cc-4787-af44-1468697daf47&limit=5'

        when:
        String buildUrl = WarsawApiRequestBuilder
                .forPropertyRent()
                .limitResults(5)
                .build()
        then:
        buildUrl == url
    }
}
