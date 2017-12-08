package me.luizclaudiosantos.xmlFiles.config;

import me.luizclaudiosantos.xmlFiles.domain.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class JobConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public StaxEventItemReader<Customer> customerItemReader() {

        XStreamMarshaller unmashaller = new XStreamMarshaller();

        Map<String, Class> aliases = new HashMap<>();
        aliases.put("customer", Customer.class);

        unmashaller.setAliases(aliases);

        StaxEventItemReader<Customer> reader = new StaxEventItemReader<>();

        reader.setResource(new ClassPathResource("/data/customers.xml"));
        reader.setFragmentRootElementName("customer");
        reader.setUnmarshaller(unmashaller);

        return reader;
    }

    @Bean
    public ItemWriter<Customer> customerItemWriter() {
        return items -> {
            for (Customer item : items) {
                System.out.println(item.toString());
            }
        };
    }


    @Bean
    public Step step1(){

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return stepBuilderFactory.get(">> Step 1 " + time )
                .<Customer, Customer>chunk(10)
                .reader(customerItemReader())
                .writer(customerItemWriter())
                .build();
    }


    @Bean
    public Job interfaceJob(){
        return jobBuilderFactory.get("job")
                .start(step1()).build();

    }


}
