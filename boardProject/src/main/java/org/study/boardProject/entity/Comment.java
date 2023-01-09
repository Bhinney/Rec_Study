package org.study.boardProject.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.study.boardProject.global.Auditable;

import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Comment extends Auditable {

	protected Comment() {}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long commentId;

	@Column(nullable = false)
	private String nickName;

	@Column(nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn(name = "boardId")
	private Board board;

	public void setBoard (Board board) {
		this.board = board;

		if (!board.getCommentList().contains(this)) {
			board.getCommentList().add(this);
		}
	}
	@Builder
	public Comment(long commentId, String nickName, String content) {
		this.commentId = commentId;
		this.nickName = nickName;
		this.content = content;
	}

	public void changeContent(String content) {
		this.content = content;
	}
}
