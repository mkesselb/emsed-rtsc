package com.demo.emsed_rtsc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.baeldung.persistence.repo", "com.demo.emsed_rtsc"}) 
@EntityScan(basePackages = {"com.baeldung.persistence.model", "com.demo.emsed_rtsc"}) 
@SpringBootApplication
public class EmsedRtscApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmsedRtscApplication.class, args);
	}

}
