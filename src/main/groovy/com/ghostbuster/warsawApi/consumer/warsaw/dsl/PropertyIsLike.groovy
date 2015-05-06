package com.ghostbuster.warsawApi.consumer.warsaw.dsl

class PropertyIsLike {

    String propertyName
    String literal

    def propertyName(String propertyName) {
        this.propertyName = propertyName
    }

    def literal(String literal) {
        this.literal = literal
    }

}
