package kosa.hdit5spring.whereru.chat.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import kosa.hdit5spring.whereru.chat.vo.SocketSessionVO;

public class ChatSocketHandler extends TextWebSocketHandler {
	
	Logger log = LogManager.getLogger("case3");
	List<SocketSessionVO> socketSessionList = new ArrayList<SocketSessionVO>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.debug("세션 사용자 연결" + session.toString());
		
		SocketSessionVO userSocket = new SocketSessionVO("userId", session);
		socketSessionList.add(userSocket);
		
		super.afterConnectionEstablished(session);
	}
	
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		log.debug("메세지" + message.toString());
		session.sendMessage(message);
		super.handleMessage(session, message);
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.debug("세션 종료" + session.toString());
		
		int userIndex = -1;
		for(int i = 0; i < socketSessionList.size(); i++) {
			if(socketSessionList.get(i).getSession().getId().equals(session.getId())) {
				userIndex = i;
				break;
			}
		}
		if(userIndex > -1) {
			socketSessionList.remove(userIndex);
		}
		
		super.afterConnectionClosed(session, status);
	}
	
}
