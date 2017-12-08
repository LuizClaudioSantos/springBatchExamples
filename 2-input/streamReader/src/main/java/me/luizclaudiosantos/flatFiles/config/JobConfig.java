package me.luizclaudiosantos.flatFiles.config;

import me.luizclaudiosantos.flatFiles.domain.Customer;
import me.luizclaudiosantos.flatFiles.domain.CustomerFieldSetMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Configuration
public class JobConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;



    @Bean
    public StateFullItemReader itemReader() {

        List<String> items =
                Stream.iterate(1, i -> i + 1)
                        .limit(100)
                        .map(value -> ">>Item = "  + value)
                        .collect(Collectors.toList());

        return new StateFullItemReader(items);
    }

    @Bean
    public ItemWriter<String> itemWriter() {
        return items -> {
            for (String value : items) {
                System.out.println(value);
            }
        };
    }


    @Bean
    public Step step1(){

        return stepBuilderFactory.get(">> Step 1 ")
                .<String, String>chunk(10)
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }


    @Bean
    public Job interfaceJob(){
        return jobBuilderFactory.get("job")
                .start(step1()).build();

    }


}
