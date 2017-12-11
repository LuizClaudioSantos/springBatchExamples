package me.luizclaudiosantos.compositeItemProcessors.config;

import me.luizclaudiosantos.compositeItemProcessors.batch.CustomLineAggregator;
import me.luizclaudiosantos.compositeItemProcessors.batch.FilteringItemProcessor;
import me.luizclaudiosantos.compositeItemProcessors.batch.UpperCaseItemProcessor;
import me.luizclaudiosantos.compositeItemProcessors.model.Customer;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.ListItemReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;


import javax.print.attribute.standard.ColorSupported;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public ListItemReader<Customer> itemReader() {

        List<Customer> items =
                Stream.iterate(1, i -> i + 1)
                        .limit(100)
                        .map(value -> new Customer(value,
                                "first name" + value,
                                "last name "+ value,
                                new Date(System.currentTimeMillis())))
                        .collect(Collectors.toList());

        return new ListItemReader<>(items);
    }

    @Bean
    public FlatFileItemWriter<Customer> customerItemWriter() throws Exception {

        FlatFileItemWriter<Customer> itemWriter = new FlatFileItemWriter<>();

        itemWriter.setLineAggregator(new CustomLineAggregator());
        String customerOutputPath = File.createTempFile("customerOutput", ".out").getAbsolutePath();
        System.out.println(">> Output Path: " + customerOutputPath);
        itemWriter.setResource(new FileSystemResource(customerOutputPath));
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

    @Bean
    public CompositeItemProcessor<Customer, Customer> itemProcessor() throws Exception {

        List<ItemProcessor<Customer, Customer>> delegates = new ArrayList<>(2);

        delegates.add(new FilteringItemProcessor());
        delegates.add(new UpperCaseItemProcessor());

        CompositeItemProcessor<Customer, Customer> compositeItemProcessor = new CompositeItemProcessor<>();

        compositeItemProcessor.setDelegates(delegates);
        compositeItemProcessor.afterPropertiesSet();

        return compositeItemProcessor;
    }

    @Bean
    public Step step1() throws Exception {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");

        return stepBuilderFactory.get(">> Step 1 " + dateFormat.format(new Date(System.currentTimeMillis())))
                .<Customer, Customer>chunk(10)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(customerItemWriter())
                .build();
    }

    @Bean
    public Job interfaceJob() throws Exception {
        return jobBuilderFactory.get("job")
                .start(step1()).build();

    }


}
