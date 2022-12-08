package payment.help.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Ord extends Auditable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ordId;

	@Column(length = 50, nullable = false)
	private String address;

	@Column(length = 13, nullable = false)
	private String phone;

	@Column
	private int quantity;

	@Column
	private int totalPrice;

	@Column
	private String tid;

	/* 🍑상품 - 주문 일대일 연관 관계 : 상품 참조 */
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column
	@Enumerated(EnumType.STRING)
	private OrdStatus status = OrdStatus.ORD_REQUEST;

	public enum OrdStatus {

		ORD_REQUEST(1, "주문 요청"),
		PAY_COMPLETE(2, "결제 완료");

		@Getter
		private int number;
		@Getter
		private String description;

		OrdStatus(int number, String description) {
			this.number = number;
			this.description = description;
		}
	}
}

