package payment.help.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import payment.help.entity.Ord;

@Repository
public interface OrdRepository  extends JpaRepository<Ord,Long> {
}
