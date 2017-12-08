package me.luizclaudiosantos.jobParameters.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JobParametersConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    @StepScope
    public Tasklet helloWorldTasklet(@Value("#{jobParameters['message']}") String message){
        return (StepContribution contribution, ChunkContext chunkContex) -> {
            System.out.println(message);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step step1(){

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return stepBuilderFactory.get(">> Step 1 " + time ).tasklet( helloWorldTasklet(null)).build();
    }


    @Bean
    public Job jobParametersJob(){
        return jobBuilderFactory.get("jobParametersJob")
                .start(step1())
                .build();
    }
}
