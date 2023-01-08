package org.study.boardProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.boardProject.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
