package com.ghostbuster.warsawApi

import groovy.transform.CompileStatic
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean

@CompileStatic
@SpringBootApplication
@EnableCaching
class Application {

    static void main(String[] args) {
        SpringApplication.run(Application.class, args)
    }

    @Bean
    CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("properties",'subway')
    }
}
