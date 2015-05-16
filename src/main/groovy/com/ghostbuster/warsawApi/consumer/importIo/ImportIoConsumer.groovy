package com.ghostbuster.warsawApi.consumer.importIo

import groovy.json.JsonSlurper
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
class ImportIoConsumer {

    @CompileDynamic
    List<String> getNigthLifeLocations(){
        String url = 'https://api.import.io/store/data/589a8d95-10c5-4b44-a17a-b60f88d80917/_query?input/webpage/url=https%3A%2F%2Ffoursquare.com%2Fexplore%3Fmode%3Durl%26near%3DWarsaw%26q%3DNightlife&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefWwZNyA1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'
        def root = new JsonSlurper().parse(url.toURL())
        return root.results*.venueaddress_value
    }
}
