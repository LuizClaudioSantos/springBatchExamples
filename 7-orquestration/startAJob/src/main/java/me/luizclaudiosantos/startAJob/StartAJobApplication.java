package me.luizclaudiosantos.startAJob;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class StartAJobApplication {

	public static void main(String[] args) {
		SpringApplication.run(StartAJobApplication.class, args);
	}
}
