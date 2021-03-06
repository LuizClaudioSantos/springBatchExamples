package me.luizclaudiosantos.flatFiles;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class FlatFilesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlatFilesApplication.class, args);
	}
}
