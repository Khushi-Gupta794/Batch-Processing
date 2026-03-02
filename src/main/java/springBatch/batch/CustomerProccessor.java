package springBatch.batch;


import org.springframework.batch.item.ItemProcessor;
import springBatch.batch.Entity.Customer;

public class CustomerProccessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process( Customer customer) throws Exception {

        customer.setFirstName(customer.getFirstName().toUpperCase());
        customer.setLastName(customer.getLastName().toUpperCase());

        return customer;
    }
}
