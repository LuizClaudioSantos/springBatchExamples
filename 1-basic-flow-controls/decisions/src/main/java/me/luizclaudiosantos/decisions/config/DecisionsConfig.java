package me.luizclaudiosantos.decisions.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class DecisionsConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Step startStep(){

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return stepBuilderFactory.get(">> Start Step " + time ).tasklet(
                (contribution, chunkContext) -> {
                    System.out.println(">> Start Step " + time);
                    return RepeatStatus.FINISHED;
                }
        ).build();
    }

    @Bean
    public Step evenStep(){

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return stepBuilderFactory.get(">> Step Even " + time).tasklet((contribution, chunckContext ) -> {
            System.out.println(">> Even Step " + time);
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public Step oddStep(){

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return stepBuilderFactory.get(">> Odd Step " + time).tasklet((contribution, chunckContext ) -> {
            System.out.println(">> Odd Step " + time);
            return RepeatStatus.FINISHED;
        }).build();
    }

    @Bean
    public JobExecutionDecider decider(){
        return new OddDecider();
    }

    @Bean
    public Job job(){
        return jobBuilderFactory.get("job")
                .start(startStep())
                .next(decider())
                .from(decider()).on("ODD").to(oddStep())
                .from(decider()).on("EVEN").to(evenStep())
                .from(decider()).on(("*")).to(decider())
                .end().build();
    }

    public static class OddDecider implements JobExecutionDecider {

        private int count = 0;

        @Override
        public FlowExecutionStatus decide(org.springframework.batch.core.JobExecution jobExecution, StepExecution stepExecution) {

            count++;

            if(count % 2 == 0){
                return new FlowExecutionStatus("EVEN");
            } else {
                return new FlowExecutionStatus("ODD");
            }

        }
    }
}
