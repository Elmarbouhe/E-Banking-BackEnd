package ticseinfo3.samiri.ebankbackend.repositoies;

import org.springframework.data.jpa.repository.JpaRepository;
import ticseinfo3.samiri.ebankbackend.entities.AccountOperation;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
}
