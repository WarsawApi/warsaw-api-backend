package com.ghostbuster.warsawApi.consumer.warsaw.dsl

import groovy.transform.CompileStatic
import spock.lang.Specification

@CompileStatic
final class FilterSpec extends Specification {

    def "Should return url with api key"() {
        given:
        String filter = '<Filter><PropertyIsLike><PropertyName>ID</PropertyName><Literal>389</Literal></PropertyIsLike></Filter>'

        when:
        String dslFilter = Filter.makeAsXML {
            propertyIsLike {
                propertyName "ID"
                literal "389"
            }
        }
        then:
        filter == dslFilter
    }

}