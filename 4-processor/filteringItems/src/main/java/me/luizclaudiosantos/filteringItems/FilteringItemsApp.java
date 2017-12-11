package me.luizclaudiosantos.filteringItems;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class FilteringItemsApp {

	public static void main(String[] args) {
		SpringApplication.run(FilteringItemsApp.class, args);
	}
}
