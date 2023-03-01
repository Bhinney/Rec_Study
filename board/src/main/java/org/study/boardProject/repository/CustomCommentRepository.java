package org.study.boardProject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.study.boardProject.dto.CommentDto;
import org.study.boardProject.entity.Comment;

public interface CustomCommentRepository {
	Page<CommentDto.Response> findBoardComments(long boardId, Pageable pageable);
}
