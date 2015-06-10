package com.ghostbuster.warsawApi.consumer.warsaw.dsl

final class PropertyIsLike {

    String propertyName
    String literal

    void propertyName(String propertyName) {
        this.propertyName = propertyName
    }

    void literal(String literal) {
        this.literal = literal
    }

}
