package springBatch.batch.Controller;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.*;
import springBatch.batch.Entity.Customer;

@RestController
@RequestMapping("/jobs")
public class JobController {
   // private final JobOperator jobOperator;
   private final JobLauncher jobLauncher;
    private final Job job;
    public JobController(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @PostMapping("/importData")
    public String startJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters params = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(job, params);
        return jobExecution.getStatus().toString();

//    try{
//        JobExecution jobExecution = jobLauncher.run(job, params);
//        return jobExecution.getStatus().toString();
//    }
//     catch (Exception e){
//         e.printStackTrace();
//     return "job execution failed" +e.getMessage();
//     }

//-------------------------------------------
//        try {
//            Long executionId = jobOperator.start("importJob", "startAt=" + System.currentTimeMillis());
//
//            return "Job started with execution id: " + executionId;

//        }catch (Exception e){
//         e.printStackTrace();
//         return "job execution failed" +e.getMessage();
//        }
    }



}
