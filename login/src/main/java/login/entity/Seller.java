package login.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Setter
@NoArgsConstructor
public class Seller {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sellerId;

	@Column
	private String introduce;

	@Column
	private String imageUrl;

	/* 🌸 판매자 - 회원 일대일 연관 관계 : 회원 참조 */
	@OneToOne
	@JoinColumn(name = "member_id")
	private Member member;

	/* 🌸판매자 - 회원 연관 관계 편의 메서드 */
	public void setMember(Member member) {
		this.member = member;

		if (member.getSeller() != this) {
			member.setSeller(this);
		}
	}

	@Builder
	public Seller(Long sellerId, String introduce, String imageUrl) {
		this.sellerId = sellerId;
		this.introduce = introduce;
		this.imageUrl = imageUrl;
	}

}
