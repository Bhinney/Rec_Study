package org.study.boardProject.mapper;

import org.mapstruct.Mapper;
import org.study.boardProject.dto.CommentDto;
import org.study.boardProject.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

	default Comment postCommentDtoToComment(CommentDto.Post post) {
		if ( post == null ) return null;

		Comment.CommentBuilder comment = Comment.builder();

		comment.nickName( post.getNickName() );
		comment.content( post.getContent() );

		return comment.build();
	}
	Comment patchCommentDtoToComment(CommentDto.Patch patch);
	CommentDto.Response commentToCommentResponseDto(Comment comment, long parentId, long boardId);
}
