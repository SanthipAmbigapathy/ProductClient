package com.springboot.productclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
public class ProductclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductclientApplication.class, args);
	}

}
