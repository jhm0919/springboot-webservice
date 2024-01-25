package com.jhm.springbootwebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EnableJpaAuditing // config 패키지에 분리
@SpringBootApplication
public class SpringbootWebserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootWebserviceApplication.class, args);
	}

}
