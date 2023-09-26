package chat.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import chat.entity.ChatRoom;
import chat.service.RedisSubscriber;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRepository {

	private final RedisMessageListenerContainer redisMessageListener; /* 채팅방(topic)에 발행되는 메시지를 처리할 Listener */
	private final RedisSubscriber redisSubscriber; /* 구독 처리 서비스 */
	private static final String CHAT_ROOMS = "CHAT_ROOM"; /* 레디스 */
	private final RedisTemplate<String, Object> redisTemplate;
	private HashOperations<String, String, ChatRoom> opsHashChatRoom;
	private Map<String, ChannelTopic> topics; /* 레디스 토픽, 룸 아이디로 찾을 수 있도록 */

	@PostConstruct
	private void init() {
		opsHashChatRoom = redisTemplate.opsForHash();
		topics = new HashMap<>();
	}

	public List<ChatRoom> findAllRoom() {
		return opsHashChatRoom.values(CHAT_ROOMS);
	}

	public ChatRoom findRoomById(String id) {
		return opsHashChatRoom.get(CHAT_ROOMS, id);
	}

	/* 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장 */
	public ChatRoom createChatRoom(String name) {
		ChatRoom chatRoom = ChatRoom.create(name);
		opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
		return chatRoom;
	}

	/* 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정 */
	public void enterChatRoom(String roomId) {
		ChannelTopic topic = topics.get(roomId);
		if (topic == null)
			topic = new ChannelTopic(roomId);
		redisMessageListener.addMessageListener(redisSubscriber, topic);
		topics.put(roomId, topic);
	}

	public ChannelTopic getTopic(String roomId) {
		return topics.get(roomId);
	}
}
