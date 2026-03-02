package springBatch.batch.Config;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SchedulerConfig {

    private final JobLauncher jobLauncher;
    private final Job job;

    public SchedulerConfig(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void runJob() throws Exception {

        JobParameters parameters = new JobParameters(
                Map.of("startAt", new JobParameter<>(System.currentTimeMillis(), Long.class))
        );

        jobLauncher.run(job, parameters);
    }
}