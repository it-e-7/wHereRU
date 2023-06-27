package kosa.hdit5spring.whereru.chat.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatSocketHandler extends TextWebSocketHandler {
	
	Logger log = LogManager.getLogger("case3");
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.debug("세션 사용자 연결" + session.toString());
		super.afterConnectionEstablished(session);
	}
	
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		log.debug("메세지" + message.toString());
		super.handleMessage(session, message);
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.debug("세션 종료" + session.toString());
		super.afterConnectionClosed(session, status);
	}
	
}
