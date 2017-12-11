package me.luizclaudiosantos.MultiDestination;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class FlatFileWriterApp {

	public static void main(String[] args) {
		SpringApplication.run(FlatFileWriterApp.class, args);
	}
}
