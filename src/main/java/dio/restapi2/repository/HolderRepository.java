package dio.restapi2.repository;

import dio.restapi2.model.Holder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolderRepository extends JpaRepository<Holder, Long> {

    boolean existsByAccountNumber(String accountNumber);

    boolean existsByCardNumber(String number);
}
