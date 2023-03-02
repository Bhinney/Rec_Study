package chat.entity;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChatRoom implements Serializable {
	private static final long serialVersionUID = 6494678977089006639L;

	private String roomId;
	private String roomName;


	public static ChatRoom create(String name) {
		ChatRoom room = new ChatRoom();
		room.roomId = UUID.randomUUID().toString();
		room.roomName = name;
		return room;
	}
}

