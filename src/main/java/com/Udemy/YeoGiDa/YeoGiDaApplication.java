package com.Udemy.YeoGiDa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableCaching
@SpringBootApplication
public class YeoGiDaApplication {

	public static void main(String[] args) {
		SpringApplication.run(YeoGiDaApplication.class, args);
	}

}
