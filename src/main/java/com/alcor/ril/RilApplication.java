package com.alcor.ril;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/** @SpringBootApplication   same as @Configuration @EnableAutoConfiguration @ComponentScan
 */
@SpringBootApplication(scanBasePackages = {"pers.roamer.boracay","com.alcor.ril"})
@ImportResource(locations = {"classpath:boracay-config.xml"})
@EnableJpaRepositories({"pers.roamer.boracay","com.alcor.ril"})
@EntityScan({"pers.roamer.boracay.entity","com.alcor.ril.entity"})
//@EnableAsync()
@EnableScheduling

@Log4j2
public class RilApplication  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(RilApplication.class, args);
    }

}
