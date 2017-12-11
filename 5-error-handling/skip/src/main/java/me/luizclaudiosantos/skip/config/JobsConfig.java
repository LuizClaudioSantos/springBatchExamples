package me.luizclaudiosantos.skip.config;

import me.luizclaudiosantos.skip.batch.CustomRetryException;
import me.luizclaudiosantos.skip.batch.SkipItemProcessor;
import me.luizclaudiosantos.skip.batch.SkipItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

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
    public SkipItemProcessor processor(@Value("#{jobParameters['skip']}") String skip) {
        SkipItemProcessor processor = new SkipItemProcessor();

        processor.setSkip(StringUtils.hasText(skip) && skip.equalsIgnoreCase("processor"));

        return processor;
    }

    @Bean
    @StepScope
    public SkipItemWriter writer(@Value("#{jobParameters['skip']}") String skip){
        SkipItemWriter writer = new SkipItemWriter();

        writer.setSkip(StringUtils.hasText(skip) && skip.equalsIgnoreCase("writer"));

        return writer;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(10)
                .reader(reader())
                .processor(processor(null))
                .writer(writer(null))
                .faultTolerant()
                .skip(CustomRetryException.class)
                .skipLimit(15)
                .build();
    }

    @Bean
    public Job derbyInFileSystemJob(){
        return jobBuilderFactory.get("job")
                .start(step1())
                .build();
    }
}
