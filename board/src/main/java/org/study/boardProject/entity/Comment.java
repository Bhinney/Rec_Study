package org.study.boardProject.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.study.boardProject.global.Auditable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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

	@Column(nullable = false)
	private long ref = 1;

	@ManyToOne
	@JoinColumn(name = "boardId")
	private Board board;

	@ManyToOne
	@JoinColumn(name = "parentId")
	private Comment parent;

	@OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private List<Comment> children = new ArrayList<>();

	public void setBoard (Board board) {
		this.board = board;

		if (!board.getCommentList().contains(this)) {
			board.getCommentList().add(this);
		}
	}

	public void setParent (Comment parent) {
		this.parent = parent;

		if (!parent.getChildren().contains(this)) {
			parent.getChildren().add(this);
		}
	}

	public void addChildren (Comment child) {
		children.add(child);

		if (child.getParent() != this) {
			child.setParent(this);
		}
	}
	@Builder
	public Comment(long commentId, String nickName, String content, long ref, Comment parent, List<Comment> children) {
		this.commentId = commentId;
		this.nickName = nickName;
		this.content = content;
		this.ref = ref;
		this.parent = parent;
		this.children = children;
	}

	public void changeContent(String content) {
		this.content = content;
	}
}
