package com.ghostbuster.warsawApi.consumer.importIo

import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.domain.internal.Localizable
import com.ghostbuster.warsawApi.service.LocationService
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty
import groovy.json.JsonSlurper
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import rx.Observable
import rx.schedulers.Schedulers

//TODO split
@CompileStatic
@Component
class ImportIoConsumer {

    LocationService locationService

    @Autowired
    ImportIoConsumer(LocationService locationService) {
        this.locationService = locationService
    }

    @HystrixCommand(commandKey = 'ImportIO:GeocodeClubs', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'clubs', unless = "#result.isEmpty()")
    List<Localizable> getClubsLocations() {
        return clubs.collect { locationService.findByAddress(it) }
    }

    @HystrixCommand(commandKey = 'ImportIO:clubs', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getClubs() {
        def root = new JsonSlurper().parse(CLUBS_URL.toURL())
        return root.results*.searchresult_value
    }

    // TODO: uncomment
//    @HystrixCommand(commandKey = 'ImportIO:properties', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")],
//            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    @Cacheable(value = 'properties', unless = "#result.isEmpty()")
    private List<Home> getPropertiesFromOtoDom(String url) {
        List<String> root = new JsonSlurper().parse(url.toURL()).results
        List<Home> a = root.collect {
            return new Home(address: it.odshowmap_value,
                    price: it.odlisting_value_2_numbers,
                    url: it.odlisting_link_1,
                    measurement: it.odlisting_value_4_numbers,
                    roomsCount: it.odlisting_value_3_numbers,
                    imageUrl: it.odimgborder_image)
        }
        a.findAll {
            it.address == null
        }.each { println 'tutaj 1'; throw new RuntimeException('1') }
        return a
    }

    @CompileDynamic
    @Cacheable(value = 'properties', unless = "#result.isEmpty()")
    private List<Home> getPropertiesFromOtoDom_Sec(String url) {
        List<String> root = new JsonSlurper().parse(url.toURL()).results
        List<Home> a = root.collect {
            return new Home(address: it.value,
                    price: it.odlisting_value_2_numbers,
                    url: it.odlisting_link_1,
                    measurement: it.odlisting_value_4_numbers,
                    roomsCount: it.odlisting_value_3_numbers,
                    imageUrl: it.odimgborder_image)
        }
        a.findAll {
            it.address == null
        }.each { println 'tutaj 2'; throw new RuntimeException('2') }
        return a
    }


    @CompileDynamic
    private Observable<List<Home>> getHomesPageX(String pageUrl) {
        return Observable.create({ aSubscriber ->
            try {
                aSubscriber.onNext(getPropertiesFromOtoDom(pageUrl));

                if (!aSubscriber.isUnsubscribed()) {
                    aSubscriber.onCompleted();
                }
            } catch (Throwable t) {
                if (!aSubscriber.isUnsubscribed()) {
                    aSubscriber.onError(t);
                }
            }
        }).subscribeOn(Schedulers.newThread())
    }

    @CompileDynamic
    private Observable<List<Home>> getHomesPageX_Sec(String pageUrl) {
        return Observable.create({ aSubscriber ->
            try {
                aSubscriber.onNext(getPropertiesFromOtoDom_Sec(pageUrl));

                if (!aSubscriber.isUnsubscribed()) {
                    aSubscriber.onCompleted();
                }
            } catch (Throwable t) {
                if (!aSubscriber.isUnsubscribed()) {
                    aSubscriber.onError(t);
                }
            }
        }).subscribeOn(Schedulers.newThread())
    }

    Observable<List<Home>> getHomesPage1() {
        return getHomesPageX(HOMES_URL_PAGE1)
    }

    Observable<List<Home>> getHomesPage2() {
        return getHomesPageX(HOMES_URL_PAGE2)
    }

    Observable<List<Home>> getHomesPage3() {
        return getHomesPageX(HOMES_ULR_PAGE3)
    }

    Observable<List<Home>> getHomesPage4() {
        return getHomesPageX_Sec(HOMES_URL_PAGE4)
    }

    Observable<List<Home>> getHomesPage5() {
        return getHomesPageX_Sec(HOMES_URL_PAGE5)
    }

    Observable<List<Home>> getHomesPage6() {
        return getHomesPageX_Sec(HOMES_URL_PAGE6)
    }

    Observable<List<Home>> getHomesPage7() {
        return getHomesPageX_Sec(HOMES_URL_PAGE7)
    }

    Observable<List<Home>> getHomesPage8() {
        return getHomesPageX_Sec(HOMES_URL_PAGE8)
    }


    @HystrixCommand(commandKey = 'ImportIO:GeocodeTheaters', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'theaters', unless = "#result.isEmpty()")
    List<Localizable> getTheatersLocations() {
        return theaters.collect { locationService.findByAddress(it) }
    }

    @HystrixCommand(commandKey = 'ImportIO:theaters', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getTheaters() {
        def root = new JsonSlurper().parse(THEATERS_URL.toURL())
        return root.results*.value
    }
//
    @HystrixCommand(commandKey = 'ImportIO:GeocodeCinemas', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'cinemas', unless = "#result.isEmpty()")
    List<Localizable> getCinemasLocations() {
        return cinemas.collect { locationService.findByAddress(it) }
    }

    @HystrixCommand(commandKey = 'ImportIO:cinemas', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getCinemas() {
        def root = new JsonSlurper().parse(CINEMAS_URL.toURL())
        return root.results*.value_1
    }
//

    //
    @HystrixCommand(commandKey = 'ImportIO:GeocodeExhibitions', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'exhibitions', unless = "#result.isEmpty()")
    List<Localizable> getExhibitionsLocations() {
        return exhibitions.collect { locationService.findByAddress(it) }
    }

    //TODO test
    @HystrixCommand(commandKey = 'ImportIO:exhibitions', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getExhibitions() {
        def root = new JsonSlurper().parse(EXHIBITIONS_URL.toURL())
        //"wrapper_content": "pl. Małachowskiego 3 (mapa) tel. 22 556 96 00",

        return root.results*.wrapper_content
    }
//

    //
    @HystrixCommand(commandKey = 'ImportIO:GeocodeParks', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'parks', unless = "#result.isEmpty()")
    List<Localizable> getParksLocations() {
        return parks.collect { locationService.findByAddress(it) }
    }

    @HystrixCommand(commandKey = 'ImportIO:parks', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getParks() {
        def root = new JsonSlurper().parse(PARKS_URL.toURL())
        return root.results*.nazwa_content
    }
//

    //
    @HystrixCommand(commandKey = 'ImportIO:GeocodePools', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'pools', unless = "#result.isEmpty()")
    List<Localizable> getPoolsLocations() {
        return pools.collect { locationService.findByAddress(it) }
    }

    @HystrixCommand(commandKey = 'ImportIO:pools', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getPools() {
        def root = new JsonSlurper().parse(POOLS_URL.toURL())
        return root.results*.col_content
    }
//

    //
    @HystrixCommand(commandKey = 'ImportIO:GeocodeFitness', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'fitness', unless = "#result.isEmpty()")
    List<Localizable> getFitnessLocations() {
        return fitness.collect { locationService.findByAddress(it) }
    }

    @HystrixCommand(commandKey = 'ImportIO:fitness', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getFitness() {
        def root = new JsonSlurper().parse(FITNESS_URL.toURL())
        return root.results*.col_content.collect {
            int cityInAddress = it.indexOf('Warszawa')
            int substringLength = cityInAddress == -1 ? it.length() - 1 : cityInAddress + 'Warszawa'.length()

            return it.substring(0, substringLength)

        }
    }

//

    @HystrixCommand(commandKey = 'ImportIO:GeocodeTennis', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'tennis', unless = "#result.isEmpty()")
    List<Localizable> getTenninsLocations() {
        return tennis.collect { locationService.findByAddress(it) }
    }

    @HystrixCommand(commandKey = 'ImportIO:tennis', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getTennis() {
        def root = new JsonSlurper().parse(TENNINS_URL.toURL())
        return root.results*.col_content
    }

    //"
    @HystrixCommand(commandKey = 'ImportIO:GeocodePubs', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'pubs', unless = "#result.isEmpty()")
    List<Localizable> getPubsLocations() {
        return pubs.collect { locationService.findByAddress(it) }
    }

    @HystrixCommand(commandKey = 'ImportIO:pubs', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getPubs() {
        def root = new JsonSlurper().parse(PUBS_URL.toURL())
        return root.results*.searchresult_value
    }

//"
    @HystrixCommand(commandKey = 'ImportIO:GeocodeRestaurant', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @Cacheable(value = 'restaurants', unless = "#result.isEmpty()")
    List<Localizable> getRestaurantsLocations() {
        return restaurants.collect { locationService.findByAddress(it) }
    }

    @HystrixCommand(commandKey = 'ImportIO:restaurants', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")],
            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    private List<String> getRestaurants() {
        def root = new JsonSlurper().parse(RESTAURANTS_URL.toURL())
        return root.results*.content
    }

    @SuppressWarnings('unused')
    private List<Object> emptyListFallback() {
        return []
    }

    private static String CLUBS_URL = 'https://api.import.io/store/data/d007a422-0ea7-4d63-b0a6-24fee48ee3b8/_query?' +
            'input/webpage/url=https%3A%2F%2Fwww.zomato.com%2Fpl%2Fwarszawa%2Fklub%3Fopen' +
            '%3Dnow%26bar%3D1&_user=a837cd70-64c7-47ef-9a54-775245e5543e&_apikey=a837cd70-' +
            '64c7-47ef-9a54-775245e5543e%3A%2BhpSmGix0JTPD' +
            '%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC' +
            '%2Bg%3D%3D'

    private
    static String HOMES_URL_PAGE1 = 'https://api.import.io/store/data/096a49e1-edcf-4e68-9178-fb04f8b1ae35/_query?input/webpage/url=http%3A%2F%2Fotodom.pl%2Findex' +
            '.php%3Fmod%3Dlisting%26source%3Dmain%26objSearchQuery.ObjectName%3DFlat%26objSearchQuery.OfferType%3Drent%26Location%3Dwarszawa%26objSearchQuery' +
            '.Distance%3D0%26objSearchQuery' +
            '.LatFrom%3D0%26objSearchQuery.LatTo%3D0%26objSearchQuery.LngFrom%3D0%26objSearchQuery.LngTo%3D0%26objSearchQuery.PriceFrom%3D%26objSearchQuery.PriceTo%3D%26objSearchQuery' +
            '.AreaFrom%3D%26objSearchQuery.AreaTo%3D%26objSearchQuery.FlatRoomsNumFrom%3D%26objSearchQuery.FlatRoomsNumTo%3D%26objSearchQuery.FlatFloorFrom%3D%26objSearchQuery' +
            '.FlatFloorTo%3D%26objSearchQuery.FlatFloorsNoFrom%3D%26objSearchQuery.FlatFloorsNoTo%3D%26objSearchQuery.FlatBuildingType%3D%26objSearchQuery.Heating%3D%26objSearchQuery' +
            '.BuildingYearFrom%3D%26objSearchQuery.BuildingYearTo%3D%26objSearchQuery.FlatFreeFrom%3D%26objSearchQuery.CreationDate%3D%26objSearchQuery.Description%3D%26objSearchQuery' +
            '.offerId%3D%26objSearchQuery.Orderby%3Ddefault%26resultsPerPage%3D100&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da' +
            '%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefWwZNyA1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'

    private
    static String HOMES_URL_PAGE2 = 'https://api.import.io/store/data/da66e4d6-3d15-4a4f-898c-074740cd8fcb/_query?input/webpage/url=http%3A%2F%2Fotodom.pl%2Findex.php%3Fmod%3' +
            'Dlisting%26resultsPerPage%3D100%26objSearchQuery.Orderby%3D%26objSearchQuery.ObjectName%3DFlat%26objSearchQuery.OfferType%3Drent%26objSearchQuery.Country.ID%3D1%26objSea' +
            'rchQuery.Province.ID%3D7%26objSearchQuery.ProvinceName%3DMazowieckie%26objSearchQuery.District.ID%3D197%26objSearchQuery.CityName%3DWarszawa%26Location%3Dmazowieckie%25' +
            '2C%2520Warszawa%26currentPage%3D2&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefWw' +
            'ZNyA1xc' +
            'XGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'

    private
    static String HOMES_ULR_PAGE3 = 'https://api.import.io/store/data/23e5f746-682d-4b4c-a94e-dbe5a823ebcd/_query?input/webpage/url=http%3A%2F%2Fotodom.pl%2Findex.php%3' +
            'Fmod%3Dlisting%26resultsPerPage%3D100%26objSearchQuery.Orderby%3D%26objSearchQuery.ObjectName%3DFlat%26objSearchQuery.OfferType%3Drent%26objSearchQuery.Country.ID' +
            '%3D1%26objSearchQuery.Province.ID%3D7%26objSearchQuery.ProvinceName%3DMazowieckie%26objSearchQuery.District.ID%3D197%26objSearchQuery.CityName%3DWarszawa%26Locati' +
            'on%3Dmazowieckie%252C%2520Warszawa%26currentPage%3D3&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e' +
            '35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefWwZNyA1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'

    private
    static String HOMES_URL_PAGE4 = 'https://api.import.io/store/data/e76f8d8f-638e-417f-9053-ef4dbae999e3/_query?input/webpage/url=http%3A%2F%2Fotodom.pl%2Findex.php%3Fm' +
            'od%3Dlisting%26resultsPerPage%3D100%26objSearchQuery.Orderby%3D%26objSearchQuery.ObjectName%3DFlat%26objSearchQuery.OfferType%3Drent%26objSearchQuery.Country.ID%3D1%2' +
            '6objSearchQuery.Province.ID%3D7%26objSearchQuery.ProvinceName%3DMazowieckie%26objSearchQuery.District.ID%3D197%26objSearchQuery.CityName%3DWarszawa%26Location%3Dmazow' +
            'ieckie%252C%2520Warszawa%26currentPage%3D4&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1p' +
            'xso5EJAefWwZNyA1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'

    private
    static String HOMES_URL_PAGE5 = 'https://api.import.io/store/data/9ad7bec3-9713-4085-84ff-e66b4cebb77e/_query?input/webpage/url=http%3A%2F%2Fotodom.pl%2Findex.php%3Fmod%' +
            '3Dlisting%26resultsPerPage%3D100%26objSearchQuery.Orderby%3D%26objSearchQuery.ObjectName%3DFlat%26objSearchQuery.OfferType%3Drent%26objSearchQuery.Country.ID%3D1%26objS' +
            'earchQuery.Province.ID%3D7%26objSearchQuery.ProvinceName%3DMazowieckie%26objSearchQuery.District.ID%3D197%26objSearchQuery.CityName%3DWarszawa%26Location%3Dmazowieckie%2' +
            '52C%2520Warszawa%26currentPage%3D5&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefW' +
            'wZNyA1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'

    private
    static String HOMES_URL_PAGE6 = 'https://api.import.io/store/data/25db3923-ada4-43e2-8a35-c5c129337ca2/_query?input/webpage/url=http%3A%2F%2Fotodom.pl%2Findex.php%3Fmod%' +
            '3Dlisting%26resultsPerPage%3D100%26objSearchQuery.Orderby%3D%26objSearchQuery.ObjectName%3DFlat%26objSearchQuery.OfferType%3Drent%26objSearchQuery.Country.ID%3D1%26obj' +
            'SearchQuery.Province.ID%3D7%26objSearchQuery.ProvinceName%3DMazowieckie%26objSearchQuery.District.ID%3D197%26objSearchQuery.CityName%3DWarszawa%26Location%3Dmazowieckie' +
            '%252C%2520Warszawa%26currentPage%3D6&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJA' +
            'efWwZNyA1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'

    private
    static String HOMES_URL_PAGE7 = 'https://api.import.io/store/data/5d3a71da-667d-4527-9087-a7c61c89d267/_query?input/webpage/url=http%3A%2F%2Fotodom.pl%2Findex.php%3Fmod%3' +
            'Dlisting%26resultsPerPage%3D100%26objSearchQuery.Orderby%3D%26objSearchQuery.ObjectName%3DFlat%26objSearchQuery.OfferType%3Drent%26objSearchQuery.Country.ID%3D1%26objSea' +
            'rchQuery.Province.ID%3D7%26objSearchQuery.ProvinceName%3DMazowieckie%26objSearchQuery.District.ID%3D197%26objSearchQuery.CityName%3DWarszawa%26Location%3Dmazowieckie%252' +
            'C%2520Warszawa%26currentPage%3D7&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefWwZN' +
            'yA1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'

    private
    static String HOMES_URL_PAGE8 = 'https://api.import.io/store/data/651fd155-12e1-431d-8349-3dd46c1f1e56/_query?input/webpage/url=http%3A%2F%2Fotodom.pl%2Findex.php%3Fmod%3D' +
            'listing%26resultsPerPage%3D100%26objSearchQuery.Orderby%3D%26objSearchQuery.ObjectName%3DFlat%26objSearchQuery.OfferType%3Drent%26objSearchQuery.Country.ID%3D1%26objSearc' +
            'hQuery.Province.ID%3D7%26objSearchQuery.ProvinceName%3DMazowieckie%26objSearchQuery.District.ID%3D197%26objSearchQuery.CityName%3DWarszawa%26Location%3Dmazowieckie%252C%' +
            '2520Warszawa%26currentPage%3D8&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefWwZNyA' +
            '1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'

    private
    static String THEATERS_URL = 'https://api.import.io/store/data/4902ac0e-1fa5-4273-8524-74bf472eecfe/_query?' +
            'input/webpage/url=http%3A%2F%2Fwarszawa.dlastudenta.pl%2Fteatr%2Fteatry' +
            '%2F&_user=a837cd70-64c7-47ef-9a54-775245e5543e&_apikey=a837cd70-64c7-47ef-9a54-' +
            '775245e5543e%3A%2BhpSmGix0JTPD' +
            '%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC' +
            '%2Bg%3D%3D'

    private
    static String CINEMAS_URL = 'https://api.import.io/store/data/2c0b52f8-fdc5-4597-a1f5-88aace635656/_query?' +
            'input/webpage/url=http%3A%2F%2Fwarszawa.repertuary.pl%2Fkino%2Fmap&_user=a837cd70-' +
            '64c7-47ef-9a54-775245e5543e&_apikey=a837cd70-64c7-47ef-9a54-775245e5543e%3A' +
            '%2BhpSmGix0JTPD' +
            '%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC' +
            '%2Bg%3D%3D'

    private
    static String EXHIBITIONS_URL = 'https://api.import.io/store/data/3cc2a029-059f-4b7b-a98b-49500f0ad5fa/_query?' +
            'input/webpage/url=http%3A%2F%2Fwww.warsawtour.pl%2Fatrakcja_turystyczna%2Flista%2Fpl' +
            '%2F175&_user=a837cd70-64c7-47ef-9a54-775245e5543e&_apikey=a837cd70-64c7-47ef-9a54-' +
            '775245e5543e%3A%2BhpSmGix0JTPD' +
            '%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC' +
            '%2Bg%3D%3D'

    private static String PARKS_URL = 'https://api.import.io/store/data/28eba12d-efbf-44ba-8ad7-52a9af07aa7c/_query?' +
            'input/webpage/url=http%3A%2F%2Fpl.wikipedia.org%2Fwiki' +
            '%2FTereny_zieleni_w_Warszawie&_user=a837cd70-64c7-47ef-9a54-' +
            '775245e5543e&_apikey=a837cd70-64c7-47ef-9a54-775245e5543e%3A%2BhpSmGix0JTPD' +
            '%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC' +
            '%2Bg%3D%3D'

    private static String POOLS_URL = 'https://api.import.io/store/data/0b5a0ea1-750e-4306-bd52-20da1f53133e/_query?' +
            'input/webpage/url=http%3A%2F%2Fwww.benefitsystems.pl%2Fsearch%2Fmultisport%3Flocation%3DWarszawa%252C%2B' +
            'Mazowieckie%26query%3D%26card%255B%255D%3D3%26category%255B%255D%3D8%26new%3D%26submit%3D&_user=a837cd70' +
            '-64c7-47ef-9a54-775245e5543e&_apikey=a837cd70-64c7-47ef-9a54-775245e55' +
            '43e%3A%2BhpSmGix0JTPD%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC%2Bg%3D%3D'

    private
    static String FITNESS_URL = 'https://api.import.io/store/data/ea819dd2-d1d4-455b-bbaa-97065f74a636/_query?' +
            'input/webpage/url=http%3A%2F%2Fwww.benefitsystems.pl%2Fsearch%2Fmultisport%3Flocation' +
            '%3DWarszawa%252C%2BMazowieckie%26query%3D%26card%255B%255D%3D3%26category' +
            '%255B%255D%3D1%26category%255B%255D%3D2%26new%3D%26submit%3D' +
            '%23&_user=a837cd70-64c7-47ef-9a54-775245e5543e&_apikey=a837cd70-64c7-47ef-9a54-' +
            '775245e5543e%3A%2BhpSmGix0JTPD' +
            '%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC' +
            '%2Bg%3D%3D'

    private
    static String TENNINS_URL = 'https://api.import.io/store/data/67190267-9eed-475f-b9cb-804146af8900/_query?' +
            'input/webpage/url=http%3A%2F%2Fwww.tenisportal.com%2Fwyniki%2Fkort%2Fwarszawa%2F&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da' +
            '&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefWwZNyA1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'

    private static String PUBS_URL = 'https://api.import.io/store/data/fe431774-7e71-41f8-8579-f0691df11161/_query?' +
            'input/webpage/url=https%3A%2F%2Fwww.zomato.com%2Fpl%2Fwarszawa' +
            '%2Fpub&_user=a837cd70-64c7-47ef-9a54-775245e5543e&_apikey=a837cd70-64c7-47ef-9a54-' +
            '775245e5543e%3A%2BhpSmGix0JTPD' +
            '%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC' +
            '%2Bg%3D%3D'

    private
    static String RESTAURANTS_URL = 'https://api.import.io/store/data/e0116a7a-8fc5-4c6d-b7da-afff9d499f2a/_query?' +
            'input/webpage/url=http%3A%2F%2Frestaurantica.pl%2Flista-restauracji%2F&_user=a837cd70-' +
            '64c7-47ef-9a54-775245e5543e&_apikey=a837cd70-64c7-47ef-9a54-775245e5543e%3A' +
            '%2BhpSmGix0JTPD' +
            '%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC' +
            '%2Bg%3D%3D'
}
