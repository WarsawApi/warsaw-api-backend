package com.ghostbuster.warsawApi.consumer.importIo

import com.ghostbuster.warsawApi.domain.internal.Property
import groovy.json.JsonSlurper
import org.springframework.stereotype.Component

@Component
class ImportIoConsumer {

    List<String> getNigthLifeLocations(){
        def root = new JsonSlurper().parse(NIGTH_LIFE_LOCS_URL.toURL())
        return root.results*.venueaddress_value
    }

    List<Property> getPropertiesFromOtoDom() {
        def root = new JsonSlurper().parse(OTO_DOM_PROPERTIES_URL.toURL())
        return root.results.collect {
            return new Property(address: it.odshowmap_value,
                    price: it.odlisting_value_2_numbers,
                    url: it.odlisting_link_1,
                    space: it.odlisting_value_4_numbers,
                    roomsCount: it.odlisting_value_3_numbers,
                    imageUrl: it.odimgborder_image)
        }
    }


    String OTO_DOM_PROPERTIES_URL = 'https://api.import.io/store/data/096a49e1-edcf-4e68-9178-fb04f8b1ae35/_query?input/webpage/url=http%3A%2F%2Fotodom.pl%2Findex.php%3Fmod%3Dlisting%26source%3Dmain%26objSearchQuery.ObjectName%3DFlat%26objSearchQuery.OfferType%3Drent%26Location%3Dwarszawa%26objSearchQuery.Distance%3D0%26objSearchQuery.LatFrom%3D0%26objSearchQuery.LatTo%3D0%26objSearchQuery.LngFrom%3D0%26objSearchQuery.LngTo%3D0%26objSearchQuery.PriceFrom%3D%26objSearchQuery.PriceTo%3D%26objSearchQuery.AreaFrom%3D%26objSearchQuery.AreaTo%3D%26objSearchQuery.FlatRoomsNumFrom%3D%26objSearchQuery.FlatRoomsNumTo%3D%26objSearchQuery.FlatFloorFrom%3D%26objSearchQuery.FlatFloorTo%3D%26objSearchQuery.FlatFloorsNoFrom%3D%26objSearchQuery.FlatFloorsNoTo%3D%26objSearchQuery.FlatBuildingType%3D%26objSearchQuery.Heating%3D%26objSearchQuery.BuildingYearFrom%3D%26objSearchQuery.BuildingYearTo%3D%26objSearchQuery.FlatFreeFrom%3D%26objSearchQuery.CreationDate%3D%26objSearchQuery.Description%3D%26objSearchQuery.offerId%3D%26objSearchQuery.Orderby%3Ddefault%26resultsPerPage%3D100&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefWwZNyA1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'

    String NIGTH_LIFE_LOCS_URL = 'https://api.import.io/store/data/589a8d95-10c5-4b44-a17a-b60f88d80917/_query?input/webpage/url=https%3A%2F%2Ffoursquare.com%2Fexplore%3Fmode%3Durl%26near%3DWarsaw%26q%3DNightlife&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefWwZNyA1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'
}
