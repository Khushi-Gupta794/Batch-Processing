package springBatch.batch.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springBatch.batch.Entity.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Long> {
}
