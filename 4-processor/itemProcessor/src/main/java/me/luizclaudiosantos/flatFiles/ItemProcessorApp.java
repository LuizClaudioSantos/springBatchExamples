package me.luizclaudiosantos.flatFiles;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class ItemProcessorApp {

	public static void main(String[] args) {
		SpringApplication.run(ItemProcessorApp.class, args);
	}
}
