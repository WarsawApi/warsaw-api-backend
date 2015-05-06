package com.ghostbuster.warsawApi

import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

abstract class SpringTestRunner extends Specification {

    @Shared
    @AutoCleanup
    ConfigurableApplicationContext context
    void setupSpec() {
        Future future = Executors
                .newSingleThreadExecutor().submit(
                new Callable() {
                    @Override

                    public ConfigurableApplicationContext call() throws Exception {
                        return (ConfigurableApplicationContext) SpringApplication
                                .run(Application.class)
                    }
                })
        context = future.get(120, TimeUnit.SECONDS)
    }

}
