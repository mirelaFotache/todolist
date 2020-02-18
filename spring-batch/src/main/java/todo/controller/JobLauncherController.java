package todo.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Set property:  <b>spring.batch.job.enabled: false</b>  in application.yaml in order to use
 * call from controller and job not start automatically
 */
@RestController
@RequestMapping("/jobs")
public class JobLauncherController {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job demoJob;

    @Autowired
    Job demo2Job;

    @RequestMapping("/demo_job")
    public ResponseEntity<String> handleDemoJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(demoJob, params);
        return new ResponseEntity<String>("Job demoJob executed successfully!", HttpStatus.OK);
    }

    @RequestMapping("/demo2_job")
    public ResponseEntity<String> handleDemo2Job() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(demo2Job, params);
        return new ResponseEntity<String>("Job demo2Job executed successfully!", HttpStatus.OK);
    }
}
