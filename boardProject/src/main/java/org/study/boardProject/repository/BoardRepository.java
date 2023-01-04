package org.study.boardProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.boardProject.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}
