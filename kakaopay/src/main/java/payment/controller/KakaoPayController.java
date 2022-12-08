package payment.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import payment.help.OrdResponseDto;
import payment.help.entity.Ord;
import payment.response.ApproveResponse;
import payment.response.ReadyResponse;
import payment.service.KakaoPayService;

@Slf4j
@RestController
@RequestMapping("/order/pay")
@RequiredArgsConstructor
@Validated
public class KakaoPayController {

	private final KakaoPayService kakaopayService;

	/* 카카오 페이 결제 요청 */
	@GetMapping("/{ord_id}")
	public ReadyResponse payReady(@PathVariable("ord_id") long ordId) {
		log.info("# 카카오 페이 결제 요청 컨트롤러 접근");
		Ord ord = kakaopayService.findVerifiedOrd(ordId);
		ReadyResponse response = kakaopayService.payReady(ord);

		return response;
	}

	/* 결제 승인 요청 */
	@GetMapping("/completed/{ord_id}")
	public String payComplete(@RequestParam("pg_token") String pgToken, @PathVariable long ordId) {

		/* 카카오 결제 요청 */
		OrdResponseDto response = kakaopayService.payApprove(ordId, pgToken);

		return "http://localhost:3000/order/pay/completed";
	}

	/* 결제 취소 요청 -> 주문 내역 삭제 */
	@GetMapping("/cancel/{ord_id}")
	public String payCancel(@PathVariable("ord_id") long ordId) {
		kakaopayService.cancelOrFailPayment(ordId);
		log.info("결제 취소");

		return "http://localhost:3000/order/pay/cancel";
	}

	/* 결제 실패 */
	@GetMapping("/fail/{ord_id}")
	public String payFail(@PathVariable("ord_id") long ordId) {
		kakaopayService.cancelOrFailPayment(ordId);
		log.info("결제 실패");

		return "http://localhost:3000/order/pay/fail";
	}
}
