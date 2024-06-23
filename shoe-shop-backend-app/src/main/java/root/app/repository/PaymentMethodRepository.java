package root.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.app.entity.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
}
