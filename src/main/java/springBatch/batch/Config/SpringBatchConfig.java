package springBatch.batch.Config;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import springBatch.batch.CustomerProccessor;
import springBatch.batch.Entity.Customer;
import springBatch.batch.Repo.CustomerRepo;



@Configuration
public class SpringBatchConfig {

    @Autowired
    private CustomerRepo customerRepo;

    @Bean
    public Job job(JobRepository jobRepository, Step step){
        return new JobBuilder("importData",jobRepository).start(step).build();

    }

    @Bean
    public FlatFileItemReader<Customer> reader(){
        return new FlatFileItemReaderBuilder<Customer>().name("customerReader").resource(new ClassPathResource("customers-100.csv")).linesToSkip(1).lineMapper(lineMapper()).targetType(Customer.class).build();
    }

   private LineMapper<Customer> lineMapper(){
       DefaultLineMapper<Customer> defaultLineMapper = new DefaultLineMapper<>();

       DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
       lineTokenizer.setDelimiter(",");
       lineTokenizer.setStrict(false);
       lineTokenizer.setNames("index","customerId","firstName","lastName","company","city","country","phone1","phone2","email","subscriptionDate","website");

       BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
       fieldSetMapper.setTargetType(Customer.class);

       defaultLineMapper.setLineTokenizer(lineTokenizer);
       defaultLineMapper.setFieldSetMapper(fieldSetMapper);

       return defaultLineMapper;
   }

   @Bean
   CustomerProccessor processor(){
        return new CustomerProccessor();
   }

    @Bean
    RepositoryItemWriter<Customer> writer() {
        RepositoryItemWriter<Customer> writer = new RepositoryItemWriter<>();
        writer.setRepository(customerRepo);
        writer.setMethodName("save");
        return writer;
    }


    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager){ //chunk(int) deprecated
        return new StepBuilder("csv-import-data", jobRepository).<Customer,Customer>chunk(10,transactionManager).reader(reader()).processor(processor()).writer(writer()).build();
    }
}
