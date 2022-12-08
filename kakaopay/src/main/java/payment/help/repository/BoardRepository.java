package payment.help.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import payment.help.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}
