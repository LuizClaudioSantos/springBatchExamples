package me.luizclaudiosantos.MultiDestination.config;

import me.luizclaudiosantos.MultiDestination.batch.CustomerLineAggregator;
import me.luizclaudiosantos.MultiDestination.model.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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
        //itemWriter.setLineAggregator(new PassThroughLineAggregator<>());
        itemWriter.setLineAggregator(new CustomerLineAggregator());
        String customerOutputPath = File.createTempFile("customerOutput", ".out").getAbsoluteFile().getPath();
        System.out.println(">> Output Path " + customerOutputPath);
        itemWriter.setResource(new FileSystemResource(customerOutputPath));
        itemWriter.afterPropertiesSet();

        return itemWriter   ;
    }

    @Bean
    public StaxEventItemWriter<Customer> xmlItemWriter() throws Exception {

        XStreamMarshaller marshaller = new XStreamMarshaller();

        Map<String, Class> aliases = new HashMap<>();
        aliases.put("customer", Customer.class);

        marshaller.setAliases(aliases);

        StaxEventItemWriter<Customer> itemWriter = new StaxEventItemWriter<>();

        itemWriter.setRootTagName("customers");
        itemWriter.setMarshaller(marshaller);
        String customerOutputPath = File.createTempFile("customerOutput", ".xml").getAbsolutePath();
        System.out.println(">> Output Path: " + customerOutputPath);
        itemWriter.setResource(new FileSystemResource(customerOutputPath));

        itemWriter.afterPropertiesSet();

        return itemWriter;

    }

    @Bean
    public CompositeItemWriter<Customer> itemWriter() throws Exception {
        List<ItemWriter<? super Customer>> writers = new ArrayList<>();

        writers.add(xmlItemWriter());
        writers.add(customerItemWriter());

        CompositeItemWriter<Customer> itemWriter = new CompositeItemWriter<>();

        itemWriter.setDelegates(writers);
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }


    @Bean
    public Step step1() throws Exception {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");

        return stepBuilderFactory.get(">> Step 1 " + dateFormat.format(new Date(System.currentTimeMillis())))
                .<Customer, Customer>chunk(10)
                .reader(itemReader())
                .writer(itemWriter())
//                .stream(xmlItemWriter())
//                .stream(xmlItemWriter())
                .build();
    }


    @Bean
    public Job interfaceJob() throws Exception {
        return jobBuilderFactory.get("job")
                .start(step1()).build();

    }


}
