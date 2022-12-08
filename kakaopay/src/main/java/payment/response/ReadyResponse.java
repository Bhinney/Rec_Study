package payment.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/* 결제 요청할 때 사용 */
@Getter
@Setter
@ToString
public class ReadyResponse {
	private String tid; /* 결제 고유 번호 */
	private String next_redirect_pc_url; /* 카카오 결제 창이 나오는 url */
	private String partner_order_id; /* 가맹점 주문 번호 (최대 100자) */
}
