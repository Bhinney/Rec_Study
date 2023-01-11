package org.study.boardProject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.study.boardProject.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository,
	QuerydslPredicateExecutor<Comment> {
	Page<Comment> findByBoard_BoardId(long boardId, Pageable pageable);
}
