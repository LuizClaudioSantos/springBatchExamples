package me.luizclaudiosantos.validatingItems;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class ValidatingItemsApp {

	public static void main(String[] args) {
		SpringApplication.run(ValidatingItemsApp.class, args);
	}
}
