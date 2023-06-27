package kosa.hdit5spring.whereru.chat.vo;

import org.springframework.web.socket.WebSocketSession;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocketSessionVO {
	
	private String userId;
	private WebSocketSession session;
}
