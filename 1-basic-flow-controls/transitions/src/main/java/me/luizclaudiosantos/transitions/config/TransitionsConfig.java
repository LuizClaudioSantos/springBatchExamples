package me.luizclaudiosantos.transitions.config;

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

@Configuration
public class TransitionsConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Step step1(){

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return stepBuilderFactory.get(">> Step 1 " + time ).tasklet(
                new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">> Step 1 " + time);
                        return RepeatStatus.FINISHED;
                    }
                }
        ).build();
    }

    @Bean
    public Step step2(){

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return stepBuilderFactory.get(">> Step 2 " + time).tasklet((contribution, chunckContext ) -> {
            System.out.println(">> Step 2 " + time);
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step step3(){

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return stepBuilderFactory.get(">> Step 3 " + time).tasklet((contribution, chunckContext ) -> {
            System.out.println(">> Step 3 " + time);
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Job derbyInFileSystemJob(){
        return jobBuilderFactory.get("transitionJobNext")
                .start(step1())
                .on("COMPLETED").to(step2())
                .from(step2()).on("COMPLETED").to(step3())
                .from(step3()).end()
                .build();
    }
}
