package com.alcor.ril;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/** @SpringBootApplication   same as @Configuration @EnableAutoConfiguration @ComponentScan
 */
@SpringBootApplication(scanBasePackages = {"pers.roamer.boracay","com.alcor.ril"})
@ImportResource(locations = {"classpath:boracay-config.xml"})
@EnableJpaRepositories("pers.roamer.boracay")
@EntityScan("pers.roamer.boracay.entity")

@Log4j2
public class RilApplication {

    public static void main(String[] args) {
        SpringApplication.run(RilApplication.class, args);
    }

}
