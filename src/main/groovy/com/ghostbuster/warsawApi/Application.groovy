package com.ghostbuster.warsawApi

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard
import org.springframework.context.annotation.Bean
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter

import javax.sql.DataSource

@CompileStatic
@SpringBootApplication
@EnableCaching
@EnableHystrix
@EnableHystrixDashboard
class Application {

    @Autowired
    private DataSource dataSource

    static void main(String[] args) {
        SpringApplication.run(Application.class, args)
    }

    @Bean
    CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("properties", 'locations', 'minDistanceToBike', 'minDistanceToSubway', 'minDistanceToClub',
                'minDistanceToTheater', 'minDistanceToCinema', 'minDistanceToeExhibition', 'minDistanceToPark', 'minDistanceToPool', 'minDistanceToFitness', 'minDistanceToTennis',
                'minDistanceToPub', 'minDistanceToRestaurant', 'minDistanceToLocation', 'minDistanceToPhrase', 'subways', 'bikes', 'clubs', 'theaters', 'cinemas', 'exhibitions',
                'parks', 'pools', 'fitness', 'tennis', 'pubs', 'restaurants', 'zippedProperties')
    }


    @Bean
    DataSource dataSource() {
        URI dbUri = new URI(System.getenv("DATABASE_URL"))
        String username = dbUri.getUserInfo().split(":")[0]
        String password = dbUri.getUserInfo().split(":")[1]
        int port = dbUri.getPort()
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + port + dbUri.getPath()

        DataSourceBuilder factory = DataSourceBuilder
                .create(this.class.getClassLoader())
                .driverClassName('org.postgresql.Driver')
                .url(dbUrl)
                .username(username)
                .password(password)

        return factory.build();
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory =
                new LocalContainerEntityManagerFactoryBean();

        entityManagerFactory.setDataSource(dataSource)

        entityManagerFactory.setPackagesToScan(
                'com.ghostbuster.warsawApi.domain')

        // Vendor adapter
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter()
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);

        // Hibernate properties
        Properties additionalProperties = new Properties()
        additionalProperties.put(
                "hibernate.dialect",
                'org.hibernate.dialect.PostgreSQLDialect')
        additionalProperties.put(
                "hibernate.show_sql",
                'true')
        additionalProperties.put(
                "hibernate.hbm2ddl.auto",
                'create')
        entityManagerFactory.setJpaProperties(additionalProperties)

        return entityManagerFactory
    }

}
