package org.study.boardProject.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.study.boardProject.global.Auditable;

import com.sun.istack.NotNull;

import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Comment extends Auditable {

	protected Comment() {}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long commentId;

	@NotNull
	private String name;

	@NotNull
	private String content;

	@Builder
	public Comment(long commentId, String nickName, String content) {
		this.commentId = commentId;
		this.name = nickName;
		this.content = content;
	}

	public void changeName(String name) {
		this.name = name;
	}

	public void changeContent(String content) {
		this.content = content;
	}
}
