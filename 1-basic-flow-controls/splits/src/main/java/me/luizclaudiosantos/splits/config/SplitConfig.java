package me.luizclaudiosantos.splits.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class SplitConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;


    public Tasklet tasklet(){
        return new CountingTasklet();
    }


    @Bean
    public Flow flow1(){
        return new FlowBuilder<Flow>("flow1")
                .start(step1()).build();
    }

    public Flow flow2(){
        return new FlowBuilder<Flow>("flow2")
                .start(step2())
                .next(step3())
                .build();
    }

    @Bean
    public Step step1(){

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return stepBuilderFactory.get(">> Step 1 " + time ).tasklet(tasklet()).build();
    }

    @Bean
    public Step step2(){

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return stepBuilderFactory.get(">> Step 2 " + time).tasklet( tasklet()).build();
    }

    @Bean
    public Step step3(){

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return stepBuilderFactory.get(">> Step 3 " + time).tasklet(tasklet()).build();
    }

    @Bean
    public Job splitInFileSystemJob(){
        return jobBuilderFactory.get("splitJob")
                .start(flow1())
                .split(new SimpleAsyncTaskExecutor()).add(flow2())
                .end()
                .build();
    }

    public static class CountingTasklet implements Tasklet {

        @Override
        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
            System.out.println(String.format("%s has been executed on thread %s", chunkContext.getStepContext().getStepName(), Thread.currentThread().getName()));
            return RepeatStatus.FINISHED;
        }
    }
}
