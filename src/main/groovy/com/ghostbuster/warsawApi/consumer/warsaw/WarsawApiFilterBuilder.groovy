package com.ghostbuster.warsawApi.consumer.warsaw

class WarsawApiFilterBuilder {

    //https://api.um.warszawa.pl/api/action/wfsstore_get/?id=45ba10ab-6562-49ce-b572-6c9b999464d6&apikey=f466c384-f5cc-4787-af44-1468697daf47
    // &filter=<Filter><PropertyIsLike><PropertyName>ID</PropertyName><Literal>389</Literal></PropertyIsLike></Filter>


    private final String property
    private StringBuilder filter = new StringBuilder('<Filter>')

    private WarsawApiFilterBuilder(String property) {
        this.property = property
    }

    static WarsawApiFilterBuilder forProperty(String property) {
        return new WarsawApiFilterBuilder(property)
    }

    WarsawApiFilterBuilder isLike(String condition) {
        filter.append("<PropertyIsLike><PropertyName>${property}</PropertyName><Literal>${condition}</Literal></PropertyIsLike></Filter>")
        return this
    }

    String build() {
        return filter.toString()
    }
}
