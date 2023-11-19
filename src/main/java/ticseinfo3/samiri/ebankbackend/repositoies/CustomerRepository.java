package ticseinfo3.samiri.ebankbackend.repositoies;

import org.springframework.data.jpa.repository.JpaRepository;
import ticseinfo3.samiri.ebankbackend.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
