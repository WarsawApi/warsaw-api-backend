package com.ghostbuster.warsawApi.domain.internal

import groovy.transform.CompileStatic

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@CompileStatic
final class WarsawApiInfo {

    final String source = "https://api.um.warszawa.pl/api/"
    final String dataTransformationTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
    final String dataAcquisitionTime = dataTransformationTime
}
