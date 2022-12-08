package payment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import payment.help.OrdResponseDto;
import payment.help.entity.Board;
import payment.help.entity.Ord;
import payment.help.repository.BoardRepository;
import payment.help.repository.OrdRepository;
import payment.response.ApproveResponse;
import payment.response.ReadyResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoPayService {

	private final OrdRepository ordRepository;
	private final BoardRepository boardRepository;
	@Value("${spring.security.oauth2.client.registration.kakao.adminKey}")
	private String adminKey;

	/* 결제 준비 */
	public ReadyResponse payReady(long ordId) {
		Ord ord = findVerifiedOrd(ordId);

		/* 카카오가 요구한 결제 요청 */
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("cid", "TC0ONETIME");  /* 테스트 용 가맹점 코드 */
		param.add("partner_order_id", String.valueOf(ord.getOrdId())); /* 가맹점 주문번호 -> String 형 */
		param.add("partner_user_id", "17farm");
		param.add("item_name", ord.getProduct().getBoard().getTitle());
		param.add("quantity", String.valueOf(ord.getQuantity())); /* 개수는 상수형 */
		param.add("total_amount", String.valueOf(ord.getTotalPrice())); /* 상품 총액 */
		param.add("tax_free_amount", "0"); /* 상품 비과세 금액 */

		param.add("approval_url", "http://localhost:8080/order/pay/completed" + ord.getOrdId()); /* 결제 승인 url */
		param.add("cancel_url", "http://localhost:8080/order/pay/cancel" + ord.getOrdId()); /* 결제 취소 url */
		param.add("fail_url", "http://localhost:8080/order/pay/fail" + ord.getOrdId()); /* 결제 실패 url */

		HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<>(param, this.getHeaders());

		RestTemplate template = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/ready";

		ReadyResponse readyResponse = template.postForObject(url, requestEntity, ReadyResponse.class);
		log.info("결제 준비 응답 객체 확인 : " + readyResponse);

		ord.setTid(readyResponse.getTid());
		ordRepository.save(ord);

		return readyResponse;
	}

	/* 승인 요청 메서드 */
	public OrdResponseDto payApprove(Long ordId, String pgToken) {

		Ord findOrd = findVerifiedOrd(ordId);

		/* 요청 값 */
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("cid", "TC0ONETIME");
		param.add("tid", findOrd.getTid()); /* 결제 고유번호, 결제 준비 API 응답에 포함 */
		param.add("partner_order_id", String.valueOf(findOrd.getOrdId())); /* 가맹점 주문번호, 결제 준비 API 요청과 일치해야 함 */
		param.add("partner_user_id", "17farm"); /* 가맹점 회원 id, 결제 준비 API 요청과 일치해야 함 */
		param.add("pg_token", pgToken); /* 결제승인 요청을 인증하는 토큰 */

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(param, this.getHeaders());

		/* 외부 url 통신 */
		RestTemplate template = new RestTemplate();

		String url = "https://kapi.kakao.com/v1/payment/approve";

		ApproveResponse approveResponse = template.postForObject(url, requestEntity, ApproveResponse.class);
		log.info("결제 승인 응답 객체 확인: " + approveResponse);

		/* 주문 상태 변경 */
		findOrd.setStatus(Ord.OrdStatus.PAY_COMPLETE);
		ordRepository.save(findOrd);

		return OrdResponseDto.builder()
			.ordId(ordId)
			.build();
	}

	/* 결제 취소 혹은 삭제 */
	public void cancelOrFailPayment(Long ordId) {

		/* 결제 취소 or 결제 실패 -> 재고 수정 */
		Ord findOrd = findVerifiedOrd(ordId);
		Board findBoard = findOrd.getProduct().getBoard();

		findBoard.getProduct().setLeftStock(findBoard.getProduct().getLeftStock() + findOrd.getQuantity());
		boardRepository.save(findBoard);
		ordRepository.delete(findOrd);
	}

	/* 여기에는 OrdService 를 안만들었으므로 여기서 확인 */
	public Ord findVerifiedOrd(long ordId) {
		Ord findOrd = ordRepository.findById(ordId)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 주문입니다."));

		return findOrd;
	}

	/* 서버에 요청할 헤더 */
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK " + adminKey);
		headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		return headers;
	}
}
