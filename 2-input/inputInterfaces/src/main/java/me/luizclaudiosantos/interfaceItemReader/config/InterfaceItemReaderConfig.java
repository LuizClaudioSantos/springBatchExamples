package me.luizclaudiosantos.interfaceItemReader.config;

import me.luizclaudiosantos.interfaceItemReader.reader.StatelessItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class InterfaceItemReaderConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public StatelessItemReader statelessItemReader(){
        List<String> data = Stream
                .iterate(1, n -> n  + 1)
                .limit(1000)
                .map(i -> new Integer(i).toString())
                .collect(Collectors.toList());

        return new StatelessItemReader(data);
    }

    @Bean
    public Step step1(){

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return stepBuilderFactory.get(">> Step 1 " + time )
                .<String, String>chunk(3)
                .reader(statelessItemReader())
                .writer(
                        list -> {
                               System.out.println(">> Reading a new Chunk");
                               list.forEach(value -> System.out.println("curentItem = " + value));
                        }).build();
    }


    @Bean
    public Job interfaceJob(){
        return jobBuilderFactory.get("interfacesJob")
                .start(step1()).build();

    }
}
