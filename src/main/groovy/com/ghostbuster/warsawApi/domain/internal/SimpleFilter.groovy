package com.ghostbuster.warsawApi.domain.internal

import groovy.transform.CompileStatic
import org.springframework.util.StringUtils

@CompileStatic
final class SimpleFilter {
    String min
    String max

    boolean applyFilter(String value) {
        Double min = castMinToDouble(min)
        Double max = castMaxToDouble(max)

        Double doubleVal
        try {
            doubleVal = Double.parseDouble(value)
        } catch (NumberFormatException | NullPointerException ignored) {
            return false
        }

        return doubleVal >= min && doubleVal <= max
    }

    private static Double castMinToDouble(String value) {
        if (!StringUtils.isEmpty(value)) {
            return Double.parseDouble(value)
        }
        return Double.MIN_VALUE
    }

    private static Double castMaxToDouble(String value) {
        if (!StringUtils.isEmpty(value)) {
            return Double.parseDouble(value)
        }
        return Double.MAX_VALUE
    }
}
