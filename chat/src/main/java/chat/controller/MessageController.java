package chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import chat.entity.ChatMessage;
import chat.service.RedisPublisher;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MessageController {

	private final RedisPublisher redisPublisher;
	private final SimpMessageSendingOperations sendingOperations;

	@MessageMapping("/chat/message")
	public void enter(ChatMessage message) {
		if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
			message.setMessage(message.getSender()+"님이 입장하였습니다.");
		}
		sendingOperations.convertAndSend("/topic/chat/room/"+message.getRoomId(),message);
	}
}
