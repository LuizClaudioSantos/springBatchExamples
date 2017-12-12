package me.luizclaudiosantos.startAJob.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
public class JobLaunchingController {

    //@Autowired
    //private JobLauncher jobLauncher;

    @Autowired
    private JobOperator jobOperator;

    //@Autowired
    //private Job job;

    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void launch(@RequestParam("name") String name) throws Exception {
		//JobParameters jobParameters =
		//		new JobParametersBuilder()
		//				.addString("name", name)
		//				.toJobParameters();
		//this.jobLauncher.run(job, jobParameters);
        DateFormat df = new SimpleDateFormat("yyyy/mm/dd HH:MM:ss.mmm");
        this.jobOperator.start("job", String.format("name=%s", name + "_"  + df.format(new Date(System.currentTimeMillis()))));
    }
}
