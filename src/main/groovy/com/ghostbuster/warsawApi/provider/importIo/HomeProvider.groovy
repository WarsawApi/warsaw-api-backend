package com.ghostbuster.warsawApi.provider.importIo

import com.ghostbuster.warsawApi.domain.internal.Home
import com.ghostbuster.warsawApi.service.LocationService
import groovy.json.JsonSlurper
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import rx.Observable
import rx.Subscriber
import rx.schedulers.Schedulers

@CompileStatic
@Component
class HomeProvider {

    private final LocationService locationService

    @Autowired
    HomeProvider(LocationService locationService) {
        this.locationService = locationService
    }

    // TODO: uncomment
//    @HystrixCommand(commandKey = 'ImportIO:properties', commandProperties = [@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")],
//            fallbackMethod = 'emptyListFallback')
    @CompileDynamic
    @Cacheable(value = 'properties', unless = "#result.isEmpty()")
    private List<Home> getPropertiesFromOtoDom(String url) {
        List<String> root = new JsonSlurper().parse(url.toURL()).results
        return root.collect {
            return new Home(address: it.odshowmap_value,
                    price: it.odlisting_value_2_numbers,
                    url: it.odlisting_link_1,
                    measurement: it.odlisting_value_4_numbers,
                    roomsCount: it.odlisting_value_3_numbers,
                    imageUrl: it.odimgborder_image)
        }
    }

    @CompileDynamic
    @Cacheable(value = 'properties', unless = "#result.isEmpty()")
    private List<Home> getPropertiesFromOtoDom_Sec(String url) {
        List<String> root = new JsonSlurper().parse(url.toURL()).results
        return root.collect {
            return new Home(address: it.value,
                    price: it.odlisting_value_2_numbers,
                    url: it.odlisting_link_1,
                    measurement: it.odlisting_value_4_numbers,
                    roomsCount: it.odlisting_value_3_numbers,
                    imageUrl: it.odimgborder_image)
        }
    }


    private Observable<List<Home>> getHomesPageX(String pageUrl) {
        return Observable.create((new Observable.OnSubscribe<List<Home>>() {
            @Override
            public void call(Subscriber<? super List<Home>> observer) {
                try {
                    observer.onNext(getPropertiesFromOtoDom(pageUrl));

                    if (!observer.isUnsubscribed()) {
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        })).subscribeOn(Schedulers.newThread())
    }

    private Observable<List<Home>> getHomesPageX_Sec(String pageUrl) {
        return Observable.create((new Observable.OnSubscribe<List<Home>>() {
            @Override
            public void call(Subscriber<? super List<Home>> observer) {
                try {
                    observer.onNext(getPropertiesFromOtoDom_Sec(pageUrl));

                    if (!observer.isUnsubscribed()) {
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        })).subscribeOn(Schedulers.newThread())
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

    private final static String CLUBS_URL = 'https://api.import.io/store/data/d007a422-0ea7' +
            '-4d63-b0a6-24fee48ee3b8/_query?' +
            'input/webpage/url=https%3A%2F%2Fwww.zomato.com%2Fpl%2Fwarszawa%2Fklub%3Fopen' +
            '%3Dnow%26bar%3D1&_user=a837cd70-64c7-47ef-9a54-775245e5543e&_apikey=a837cd70-' +
            '64c7-47ef-9a54-775245e5543e%3A%2BhpSmGix0JTPD' +
            '%2BvB2HgMLG7OHkvFeBRTBcskxsmSlrplUI1e6aIxhYlgw24rS0GzOl32sUvkvGiRiYMI7rjC' +
            '%2Bg%3D%3D'

    private final static String HOMES_URL_PAGE1 = 'https://api.import.io/store/data/096a49e1-edcf-4e68-9' +
            '178-fb04f8b1ae35/_query?input/webpage/url=http%3A%2F%2Fotodom.pl%2Findex' +
            '.php%3Fmod%3Dlisting%26source%3Dmain%26objSearchQuery.ObjectName%3DFlat%26objSearchQuery.OfferType%3Drent%26Location%3Dwarszawa%26objSearchQuery' +
            '.Distance%3D0%26objSearchQuery' +
            '.LatFrom%3D0%26objSearchQuery.LatTo%3D0%26objSearchQuery.LngFrom%3D0%26objSearchQuery.LngTo%3D0%26objSearchQuery.PriceFrom%3D%26objSearchQuery.PriceTo%3D%26objSearchQuery' +
            '.AreaFrom%3D%26objSearchQuery.AreaTo%3D%26objSearchQuery.FlatRoomsNumFrom%3D%26objSearchQuery.FlatRoomsNumTo%3D%26objSearchQuery.FlatFloorFrom%3D%26objSearchQuery' +
            '.FlatFloorTo%3D%26objSearchQuery.FlatFloorsNoFrom%3D%26objSearchQuery.FlatFloorsNoTo%3D%26objSearchQuery.FlatBuildingType%3D%26objSearchQuery.Heating%3D%26objSearchQuery' +
            '.BuildingYearFrom%3D%26objSearchQuery.BuildingYearTo%3D%26objSearchQuery.FlatFreeFrom%3D%26objSearchQuery.CreationDate%3D%26objSearchQuery.Description%3D%26objSearchQuery' +
            '.offerId%3D%26objSearchQuery.Orderby%3Ddefault%26resultsPerPage%3D100&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da' +
            '%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefWwZNyA1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'

    private final static String HOMES_URL_PAGE2 = 'https://api.import.io/store/data/da66e4d6-3d15-4a4f-8' +
            '98c-074740cd8fcb/_query?input/webpage/url=http%3A%2F%2Fotodom.pl%2Findex.php%3Fmod%3' +
            'Dlisting%26resultsPerPage%3D100%26objSearchQuery.Orderby%3D%26objSearchQuery.ObjectName%3DFlat%26objSearchQuery.OfferType%3Drent%26objSearchQuery.Country.ID%3D1%26objSea' +
            'rchQuery.Province.ID%3D7%26objSearchQuery.ProvinceName%3DMazowieckie%26objSearchQuery.District.ID%3D197%26objSearchQuery.CityName%3DWarszawa%26Location%3Dmazowieckie%25' +
            '2C%2520Warszawa%26currentPage%3D2&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefWw' +
            'ZNyA1xc' +
            'XGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'

    private final static String HOMES_ULR_PAGE3 = 'https://api.import.io/store/data/23e5f746-682d-4b4c-a94e-dbe5' +
            'a823ebcd/_query?input/webpage/url=http%3A%2F%2Fotodom.pl%2Findex.php%3' +
            'Fmod%3Dlisting%26resultsPerPage%3D100%26objSearchQuery.Orderby%3D%26objSearchQuery.ObjectName%3DFlat%26objSearchQuery.OfferType%3Drent%26objSearchQuery.Country.ID' +
            '%3D1%26objSearchQuery.Province.ID%3D7%26objSearchQuery.ProvinceName%3DMazowieckie%26objSearchQuery.District.ID%3D197%26objSearchQuery.CityName%3DWarszawa%26Locati' +
            'on%3Dmazowieckie%252C%2520Warszawa%26currentPage%3D3&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e' +
            '35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefWwZNyA1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'

    private final static String HOMES_URL_PAGE4 = 'https://api.import.io/store/data/e76f8d8f-638e-417f' +
            '-9053-ef4dbae999e3/_query?input/webpage/url=http%3A%2F%2Fotodom.pl%2Findex.php%3Fm' +
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

    private final static String HOMES_URL_PAGE6 = 'https://api.import.io/store/data/25db3923-ada4-43e2-8a35-c5' +
            'c129337ca2/_query?input/webpage/url=http%3A%2F%2Fotodom.pl%2Findex.php%3Fmod%' +
            '3Dlisting%26resultsPerPage%3D100%26objSearchQuery.Orderby%3D%26objSearchQuery.ObjectName%3DFlat%26objSearchQuery.OfferType%3Drent%26objSearchQuery.Country.ID%3D1%26obj' +
            'SearchQuery.Province.ID%3D7%26objSearchQuery.ProvinceName%3DMazowieckie%26objSearchQuery.District.ID%3D197%26objSearchQuery.CityName%3DWarszawa%26Location%3Dmazowieckie' +
            '%252C%2520Warszawa%26currentPage%3D6&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJA' +
            'efWwZNyA1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'

    private final static String HOMES_URL_PAGE7 = 'https://api.import.io/store/data/5d3a7' +
            '1da-667d-4527-9087-a7c61c89d267/_query?input/webpage/url=http%3A%2F%2Fotodom.pl%2Findex.php%3Fmod%3' +
            'Dlisting%26resultsPerPage%3D100%26objSearchQuery.Orderby%3D%26objSearchQuery.ObjectName%3DFlat%26objSearchQuery.OfferType%3Drent%26objSearchQuery.Country.ID%3D1%26objSea' +
            'rchQuery.Province.ID%3D7%26objSearchQuery.ProvinceName%3DMazowieckie%26objSearchQuery.District.ID%3D197%26objSearchQuery.CityName%3DWarszawa%26Location%3Dmazowieckie%252' +
            'C%2520Warszawa%26currentPage%3D7&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefWwZN' +
            'yA1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'

    private final static String HOMES_URL_PAGE8 = 'https://api.import.io/store/data/651fd155-12e1-431d-' +
            '8349-3dd46c1f1e56/_query?input/webpage/url=http%3A%2F%2Fotodom.pl%2Findex.php%3Fmod%3D' +
            'listing%26resultsPerPage%3D100%26objSearchQuery.Orderby%3D%26objSearchQuery.ObjectName%3DFlat%26objSearchQuery.OfferType%3Drent%26objSearchQuery.Country.ID%3D1%26objSearc' +
            'hQuery.Province.ID%3D7%26objSearchQuery.ProvinceName%3DMazowieckie%26objSearchQuery.District.ID%3D197%26objSearchQuery.CityName%3DWarszawa%26Location%3Dmazowieckie%252C%' +
            '2520Warszawa%26currentPage%3D8&_user=04e6d081-0839-4cd3-b00b-e35e6fee10da&_apikey=04e6d081-0839-4cd3-b00b-e35e6fee10da%3AUGKyc4zkFKdTu87dRv0FxwX7IIcuq7m1pxso5EJAefWwZNyA' +
            '1xcXGLVn8pJrHoQd0FSOvPoeg3Mm43g3IxBH4Q%3D%3D'
}
