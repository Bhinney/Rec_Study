package org.study.boardProject.dto;

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
	@AllArgsConstructor
	public static class Patch {
		private String content;
	}

	@Getter
	@AllArgsConstructor
	public static class Response {
		private long boardId;
		private long commentId;
		private String nickName;
		private String content;
	}
}
