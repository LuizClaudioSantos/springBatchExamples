package me.luizclaudiosantos.flatFiles;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class XmlWriterApp {

	public static void main(String[] args) {
		SpringApplication.run(XmlWriterApp.class, args);
	}
}
