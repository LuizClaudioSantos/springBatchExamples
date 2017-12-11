package me.luizclaudiosantos.flatFiles.config;

import me.luizclaudiosantos.flatFiles.batch.UpperCaseItemProcessor;
import me.luizclaudiosantos.flatFiles.model.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public StaxEventItemWriter<Customer> customerItemWriter() throws Exception {

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
    public UpperCaseItemProcessor itemProcessor(){ return new UpperCaseItemProcessor();}

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
