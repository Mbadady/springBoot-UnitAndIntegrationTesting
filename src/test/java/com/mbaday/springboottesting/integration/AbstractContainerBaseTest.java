package com.mbaday.springboottesting.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public abstract class AbstractContainerBaseTest {

    static final MySQLContainer MY_SQL_CONTAINER;

    static {
//        initialize and create an object
        MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest")
                .withUsername("Mbadady")
                .withPassword("Mbadady1_")
                .withDatabaseName("ems");

//        Container will be started manually
        MY_SQL_CONTAINER.start();



    }

    //    to bind the above with the application so that it will be loaded in the application context
    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    }

}
