package org.study.boardProject.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CommentDto {

	@Getter
	@AllArgsConstructor
	public static class Post {
		private String nickName;
		private String content;
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
	@AllArgsConstructor
	public static class Response {
		private long boardId;
		private long commentId;
		private String nickName;
		private String content;
		private LocalDate createdAt;
		private LocalDate modifiedAt;
	}
}
