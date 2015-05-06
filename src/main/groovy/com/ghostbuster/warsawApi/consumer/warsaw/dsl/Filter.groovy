package com.ghostbuster.warsawApi.consumer.warsaw.dsl

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.dataformat.xml.XmlMapper

class Filter {

    PropertyIsLike propertyIsLike

    static String xml(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = Filter) closure) {
        Filter memoDsl = new Filter()
        closure.delegate = memoDsl
        closure()
        return doXml(memoDsl)
    }

    void propertyIsLike(@DelegatesTo(PropertyIsLike) Closure body) {
        propertyIsLike = new PropertyIsLike()
        def code = body.rehydrate(propertyIsLike, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
    }

    private static String doXml(Filter memoDsl) {
        XmlMapper mapper = new XmlMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.PASCAL_CASE_TO_CAMEL_CASE)
        return mapper.writeValueAsString(memoDsl)
    }

}
