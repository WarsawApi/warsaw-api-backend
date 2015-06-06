package com.ghostbuster.warsawApi.domain.internal

import groovy.transform.CompileStatic
import org.springframework.util.StringUtils

@CompileStatic
class SimpleFilter {
    String min
    String max

    boolean applyFilter(String value) {
        Double min = castMinToDouble(min)
        Double max = castMaxToDouble(max)

        Double doubleVal
        try {
            doubleVal = Double.parseDouble(value)
        } catch (Exception ex) {
            return false
        }

        return doubleVal > min && doubleVal < max
    }

    private Double castMinToDouble(String value) {
        if (!StringUtils.isEmpty(value)) {
            return Double.parseDouble(value)
        }
        return Double.MIN_VALUE
    }

    private Double castMaxToDouble(String value) {
        if (!StringUtils.isEmpty(value)) {
            return Double.parseDouble(value)
        }
        return Double.MAX_VALUE
    }
}
