package org.study.boardProject.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

public class CommentDto {

	@Getter
	public static class Post {
		private String nickName;
		private String content;

		public Post(String nickName, String content) {
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
		private String nickName;
		private String content;
		private LocalDate createdAt;
		private LocalDate modifiedAt;

		protected Response() {}

		@Builder
		@QueryProjection
		public Response(long boardId, long commentId, String nickName, String content, LocalDate createdAt,
			LocalDate modifiedAt) {
			this.boardId = boardId;
			this.commentId = commentId;
			this.nickName = nickName;
			this.content = content;
			this.createdAt = createdAt;
			this.modifiedAt = modifiedAt;
		}
	}
}
