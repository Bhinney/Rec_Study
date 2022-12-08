package payment.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	@Column(nullable = false)
	private int price;

	@Column
	@Enumerated(EnumType.STRING)
	private ProductStatus status = ProductStatus.PRD_SELLING;

	@Column(nullable = false)
	private int stock;

	@Column(nullable = false)
	private int leftStock;

	@Column(nullable = false)
	private int category;

	@Column
	private String mainImage;

	/* 🍋게시판 - 상품 일대일 연관 관계 : 상품 참조*/
	@OneToOne(mappedBy = "product",cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, targetEntity = Board.class )
	private Board board;

	/* 🍋게시판 - 상품 연관 관계 메서드 */
	public void setProduct(Board board) {
		this.board = board;

		if (board.getProduct() != this) {
			board.setProduct(this);
		}
	}

	/* 🍑상품 - 주문 일대일 연관 관계 : 상품 참조 */
	@OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private List<Ord> ordList = new ArrayList<>();

	public void addOrd(Ord ord) {
		ordList.add(ord);

		if (ord.getProduct() != this) {
			ord.setProduct(this);
		}
	}

	public enum ProductStatus{
		PRD_SELLING("1", "판매중"),
		PRD_SOLDOUT("2", "매진");

		private String code;
		private String value;

		ProductStatus(String value, String code) {
			this.code =code;
			this.value = value;
		}

		public String getCode(){
			return code;
		}
		public String getValue(){
			return value;
		}

	}
}

