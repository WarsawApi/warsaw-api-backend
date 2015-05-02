package com.ghostbuster.warsawApi.consumer.warsaw

import spock.lang.Specification

final class WarsawApiRequestBuilderTest extends Specification {
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
