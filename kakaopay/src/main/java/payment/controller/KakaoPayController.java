package payment.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import payment.help.OrdResponseDto;
import payment.help.entity.Ord;
import payment.response.ReadyResponse;
import payment.service.KakaoPayService;

@Slf4j
@Controller
@RequiredArgsConstructor
@Validated
public class KakaoPayController {

	private final KakaoPayService kakaopayService;

	/* 카카오 페이 결제 요청 */
	@GetMapping("/order/pay/{ord_id}")
	public ResponseEntity payReady(@PathVariable("ord_id") long ordId) {
		ReadyResponse response = kakaopayService.payReady(ordId);

		return ResponseEntity.ok(response);
	}

	/* 결제 승인 요청 */
	@GetMapping("/order/pay/completed/{ord_id}")
	public String payComplete(@RequestParam("pg_token") String pgToken, @PathVariable long ordId) {

		/* 카카오 결제 요청 */
		OrdResponseDto response = kakaopayService.payApprove(ordId, pgToken);

		URI uri = UriComponentsBuilder.fromUri(URI.create("http://localhost:3000/order/pay/completed"))
			.queryParam("ordId", ordId)
			.build().toUri();

		/* 프런트 페이지로 리다이렉트 */
		return "redirect:" + uri;
	}

	/* 결제 취소 요청 -> 주문 내역 삭제 */
	@GetMapping("/order/pay/cancel/{ord_id}")
	public String payCancel(@PathVariable("ord_id") long ordId) {
		kakaopayService.cancelOrFailPayment(ordId);
		log.info("결제 취소");

		/* 프런트 페이지로 리다이렉트 */
		return "redirect:http://localhost:3000/order/pay/cancel";
	}

	/* 결제 실패 */
	@GetMapping("/order/pay/fail/{ord_id}")
	public String payFail(@PathVariable("ord_id") long ordId) {
		kakaopayService.cancelOrFailPayment(ordId);
		log.info("결제 실패");

		/* 프런트 페이지로 리다이렉트 */
		return "redirect:http://localhost:3000/order/pay/fail";
	}
}
