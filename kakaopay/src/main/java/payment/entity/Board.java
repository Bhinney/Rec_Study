package payment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "board")
public class Board extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long boardId;

	@Column(length = 100, nullable = false)
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(nullable = false )
	private int reviewNum;

	@Column(nullable = false)
	private double reviewAvg ;

	/* 🍋게시판 - 상품 일대일 연관 관계 : 상품 참조*/
	@OneToOne
	@JoinColumn(name = "productId" , referencedColumnName = "productId")
	private Product product;

	/* 🍋게시판 - 상품 연관 관계 편의 메서드 */
	public void setBoard(Product product) {
		this.product = product;

		if (product.getBoard() != this) {
			product.setBoard(this);
		}
	}
}
