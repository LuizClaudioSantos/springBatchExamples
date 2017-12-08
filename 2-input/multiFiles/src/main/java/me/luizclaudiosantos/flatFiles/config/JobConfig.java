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


@Configuration
public class JobConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Value("classpath*:/data/customer*.csv")
    private Resource[] inputFiles;

    public MultiResourceItemReader<Customer> multiResourceItemReader(){

        MultiResourceItemReader<Customer> reader = new MultiResourceItemReader<>();

        reader.setDelegate(customerItemReader());
        reader.setResources(inputFiles);

        return reader;
    }

    @Bean
    public FlatFileItemReader<Customer> customerItemReader() {

        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();

        DefaultLineMapper<Customer> customerLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[] {"id", "firstName", "lastName", "birthdate"});

        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new CustomerFieldSetMapper());
        customerLineMapper.afterPropertiesSet();

        reader.setLineMapper(customerLineMapper);

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
                .reader(multiResourceItemReader())
                .writer(customerItemWriter())
                .build();
    }


    @Bean
    public Job interfaceJob(){
        return jobBuilderFactory.get("job")
                .start(step1()).build();

    }


}
