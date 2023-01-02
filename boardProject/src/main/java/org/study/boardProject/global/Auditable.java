package org.study.boardProject.global;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable {

	/* 생성 시간 */
	@CreatedDate
	@Column(updatable = false)
	private LocalDate createdAt;

	/* 수정 시간 */
	@LastModifiedDate
	private LocalDate modifiedAt;

	public void setModifiedAt(LocalDate modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
}
