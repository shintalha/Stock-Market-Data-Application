package com.personal.stockmarketmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StockMarketManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockMarketManagementApplication.class, args);
	}

}
