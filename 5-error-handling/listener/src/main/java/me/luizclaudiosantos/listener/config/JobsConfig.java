package me.luizclaudiosantos.listener.config;

import me.luizclaudiosantos.listener.batch.CustomException;
import me.luizclaudiosantos.listener.batch.CustomSkipListener;
import me.luizclaudiosantos.listener.batch.SkipItemProcessor;
import me.luizclaudiosantos.listener.batch.SkipItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class JobsConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public ListItemReader reader(){

        List<String> items = Stream
                .iterate(1, i -> i +1)
                .map(value -> new Integer(value).toString())
                .limit(100)
                .collect(Collectors.toList());

        ListItemReader<String> itemReader = new ListItemReader<>(items);

        return itemReader;
    }

    @Bean
    @StepScope
    public SkipItemProcessor processor() {
        return  new SkipItemProcessor();
    }

    @Bean
    @StepScope
    public SkipItemWriter writer(){
        return new SkipItemWriter();
    }

    @Bean
    public SkipListener skipListener(){
        return new CustomSkipListener();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .faultTolerant()
                .skip(CustomException.class)
                .skipLimit(15)
                .listener(skipListener())
                .build();
    }

    @Bean
    public Job derbyInFileSystemJob(){
        return jobBuilderFactory.get("job")
                .start(step1())
                .build();
    }
}
