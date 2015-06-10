package com.ghostbuster.warsawApi.domain.internal

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor

@CompileStatic
@TupleConstructor
final class Result {
    List<Home> items
}
