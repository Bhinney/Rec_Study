package org.study.boardProject.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.study.boardProject.dto.CommentDto;
import org.study.boardProject.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

	Comment postCommentDtoToComment(CommentDto.Post post);
	Comment patchCommentDtoToComment(CommentDto.Patch patch);
	CommentDto.Response commentToCommentResponseDto(Comment comment);
	List<CommentDto.Response> commentToCommentResponseDto(List<Comment> commentList);
}
