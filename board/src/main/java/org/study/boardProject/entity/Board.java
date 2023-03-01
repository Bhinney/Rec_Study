package org.study.boardProject.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.study.boardProject.global.Auditable;

import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Board extends Auditable {

	public Board(){} /* 테스트를 위해 public */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long boardId;

	@Column(length = 50, nullable = false)
	private String nickName;

	@Column(length = 100, nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	@OneToMany(mappedBy = "board", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private List<Comment> commentList = new ArrayList<>();

	public void addComment(Comment comment) {
		commentList.add(comment);

		if (comment.getBoard() != this) {
			comment.setBoard(this);
		}
	}

	public void changeNickName(String nickName) {
		this.nickName = nickName;
	}

	public void changeTitle(String title) {
		this.title = title;
	}

	public void changeContent(String content) {
		this.content = content;
	}

	@Builder
	public Board(long boardId, String nickName, String title, String content) {
		this.boardId = boardId;
		this.nickName = nickName;
		this.title = title;
		this.content = content;
	}
}
