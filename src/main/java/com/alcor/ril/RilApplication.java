package com.alcor.ril;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication(scanBasePackages = "pers.roamer.boracay")
@RestController
@ImportResource(locations = {"classpath:boracay-config.xml"})
@EnableJpaRepositories("pers.roamer.boracay")
@EntityScan("pers.roamer.boracay.entity")
public class RilApplication {

    public static void main(String[] args) {
        SpringApplication.run(RilApplication.class, args);
    }

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/index");
        return modelAndView;
    }
}
