package com.ghostbuster.warsawApi.consumer.warsaw.dsl

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.dataformat.xml.XmlMapper

class Filter {

    PropertyIsLike propertyIsLike

    static String makeAsXML(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = Filter) Closure closure) {
        Filter filter = new Filter()
        closure.delegate = filter
        closure()
        return doXml(filter)
    }

    void propertyIsLike(@DelegatesTo(PropertyIsLike) Closure closure) {
        propertyIsLike = new PropertyIsLike()
        def code = closure.rehydrate(propertyIsLike, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
    }

    private static String doXml(Filter filter) {
        XmlMapper mapper = new XmlMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.PASCAL_CASE_TO_CAMEL_CASE)
        return mapper.writeValueAsString(filter)
    }

}
