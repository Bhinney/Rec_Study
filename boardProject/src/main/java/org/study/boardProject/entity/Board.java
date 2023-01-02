package org.study.boardProject.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.study.boardProject.global.Auditable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Board extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long boardId;

	@Column(length = 50, nullable = false)
	private String nickName;

	@Column(length = 100, nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
