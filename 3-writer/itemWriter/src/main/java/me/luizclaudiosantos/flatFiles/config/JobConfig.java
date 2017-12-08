package me.luizclaudiosantos.flatFiles.config;

import javafx.scene.input.DataFormat;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public ListItemReader<String> itemReader() {

        List<String> items =
                Stream.iterate(1, i -> i + 1)
                        .limit(100)
                        .map(value -> ">>Item = "  + value)
                        .collect(Collectors.toList());

        return new ListItemReader<>(items);
    }

    @Bean
    public ItemWriter<String> itemWriter() {
        return new SysOutItemWriter();
    }


    @Bean
    public Step step1(){

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");

        return stepBuilderFactory.get(">> Step 1 " + dateFormat.format(new Date(System.currentTimeMillis())))
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
