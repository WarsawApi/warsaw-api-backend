package com.ghostbuster.warsawApi.consumer.importIo

import com.ghostbuster.warsawApi.domain.internal.Property
import groovy.json.JsonSlurper
import org.springframework.stereotype.Component

@Component
class ImportIoConsumer {

    List<String> addresses =['ul. Fieldorfa 41','ul. Nowy Świat 15/17','ul. Górczewska 124','Al. Jerozolimskie 148','ul. Powsińska 31 ','Al. Komisji Edukacji Narodowej 55','Brukowa 25','Birżańska 1']

    List<String> getNigthLifeLocations(){
        String url = 'https://api.import.io/store/data/589a8d95-10c5-4b44-a17a-b60f88d80917/_query?input/webpage/url=https%3A%2F%2Ffoursquare.com%2Fexplore%3Fmode%3Durl%26near%3DWarsaw%26q%3DNightlife&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefWwZNyA1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'
        def root = new JsonSlurper().parse(url.toURL())
        return root.results*.venueaddress_value
    }

    List<Property> getPropertiesFromGumtree(){
        String url = 'https://api.import.io/store/data/40ec09d7-56f3-4d43-8489-56f03a84e4fa/_query?input/webpage/url=http%3A%2F%2Fwww.gumtree.pl%2Ffp-mieszkania-i-domy-do-wynajecia%2Fc9008&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefWwZNyA1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'
        def root = new JsonSlurper().parse(url.toURL())
        return root.results.collect{ gumProperty ->
            return createPropertyFromGumtree(gumProperty)
        }.take(10)
    }

    private Property createPropertyFromGumtree(gumProperty) {
        Property property = new Property()
        def randomGen = new Random()
        property.longitude = 21.016667 + randomGen.nextInt() % 100 / 1000
        property.latitude = 52.233333 + randomGen.nextInt() % 100 / 1000
        property.url = gumProperty.adlinksb_link
        property.imageUrl = gumProperty.thumbnail_image
        property.address = addresses.get(Math.abs(randomGen.nextInt()) % addresses.size())
        return property
    }
}
