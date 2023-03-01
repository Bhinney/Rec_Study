package org.study.boardProject.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.study.boardProject.entity.Comment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class CommentDto {

	@Getter
	public static class Post {
		private long parentId;
		private String nickName;
		private String content;

		public Post(long parentId, String nickName, String content) {
			this.parentId = parentId;
			this.nickName = nickName;
			this.content = content;
		}
	}

	@Getter
	public static class Patch {
		private String content;

		@JsonCreator
		public Patch(@JsonProperty("content") String content) {
			this.content = content;
		}
	}

	@Getter
	public static class Response {
		private long boardId;
		private long commentId;
		private Comment parent;
		private String nickName;
		private String content;
		private long ref;
		private LocalDate createdAt;
		private LocalDate modifiedAt;

		@Setter
		private List<CommentDto.Response> children = new ArrayList<>();

		public Response() {}

		@Builder
		@QueryProjection
		public Response(long boardId, long commentId, Comment parent, String nickName, String content, long ref,
			LocalDate createdAt, LocalDate modifiedAt, List<CommentDto.Response> children) {
			this.boardId = boardId;
			this.commentId = commentId;
			this.parent = parent;
			this.nickName = nickName;
			this.content = content;
			this.ref = ref;
			this.createdAt = createdAt;
			this.modifiedAt = modifiedAt;
			this.children = children;
		}
	}

	@Getter
	public static class Child {
		private long parentId;
		private long childId;
		private String nickName;
		private String content;
		private LocalDate createdAt;
		private LocalDate modifiedAt;

		protected Child() {}

		@Builder
		public Child(long parentId, long childId, String nickName, String content, LocalDate createdAt,
			LocalDate modifiedAt) {
			this.parentId = parentId;
			this.childId = childId;
			this.nickName = nickName;
			this.content = content;
			this.createdAt = createdAt;
			this.modifiedAt = modifiedAt;
		}
	}
}
