package com.ghostbuster.warsawApi

import groovy.transform.CompileStatic
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean

import javax.sql.DataSource

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

    @Bean
    public DataSource dataSource() {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        int port = dbUri.getPort();
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + port + dbUri.getPath();

        DataSourceBuilder factory = DataSourceBuilder
                .create(this.class.getClassLoader())
                .driverClassName('org.postgresql.Driver')
                .url(dbUrl)
                .username(username)
                .password(password);
        return factory.build();
    }

}
