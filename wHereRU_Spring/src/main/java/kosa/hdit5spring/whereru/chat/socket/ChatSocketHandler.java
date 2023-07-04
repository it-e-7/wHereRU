package kosa.hdit5spring.whereru.chat.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kosa.hdit5spring.whereru.chat.service.ChatService;
import kosa.hdit5spring.whereru.chat.vo.ChatVO;
import kosa.hdit5spring.whereru.chat.vo.SocketSessionVO;
import kosa.hdit5spring.whereru.notice.service.NoticeService;
import kosa.hdit5spring.whereru.notice.service.SetNoticeService;

public class ChatSocketHandler extends TextWebSocketHandler {
	
	Logger log = LogManager.getLogger("case3");
	List<SocketSessionVO> socketSessionList = new ArrayList<SocketSessionVO>();
	Map<String, WebSocketSession> socketSessionMap = new HashMap<String, WebSocketSession>();
	
	@Autowired
	ChatService service;
	@Autowired
	NoticeService noticeService;
	@Autowired
	SetNoticeService setNoticeService;
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.debug("세션 사용자 연결" + session.toString());
		
		SocketSessionVO userSocket = new SocketSessionVO("userId", session);
//		socketSessionList.add(userSocket);
		
		super.afterConnectionEstablished(session);
	}
	
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		
		
		try {
			JsonObject parsedChat = JsonParser.parseString(message.getPayload().toString()).getAsJsonObject();
			
			if(parsedChat.get("type") != null) {
				socketSessionMap.put(parsedChat.get("user").toString(), session);
			} else {
				ChatVO chat = new ChatVO();
				
				String chatSender = parsedChat.get("chatSender").getAsString();
				String chatReceiver = parsedChat.get("chatReceiver").getAsString();
				String chatContent = parsedChat.get("chatContent").getAsString();
				String chatType = parsedChat.get("chatType").getAsString();
				String chatDate = parsedChat.get("chatDate").getAsString();
				int missingSeq = parsedChat.get("missingSeq").getAsInt();
				
				chat.setChatSender(chatSender);
				chat.setChatReceiver(chatReceiver);
				chat.setChatContent(chatContent);
				chat.setChatType(chatType);
				chat.setChatDate(chatDate);
				chat.setMissingSeq(missingSeq);
				
				service.addChat(chat);
				
				// echo
				session.sendMessage(message);
				
				// receiver에게 채팅 보내기
				socketSessionMap.put(parsedChat.get("chatSender").toString(), session);
				
				WebSocketSession receiverSession = socketSessionMap.get(parsedChat.get("chatReceiver").toString());
				
				if(receiverSession != null) {
					receiverSession.sendMessage(message);
					noticeService.sendingToOne(chat.getChatReceiver());
					setNoticeService.setNoticeByChat(chat);
				}
				
			}
			
		} catch (Exception err) {
			log.error(err.getMessage());
		}
		
		super.handleMessage(session, message);
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.debug("세션 종료" + session.toString());
		
		String userId = "";
		for(String key : socketSessionMap.keySet()) {
			if(socketSessionMap.get(key).getId().equals(session.getId())) {
				userId = key;
				break;
			}
		}
		if(userId.length() > 0) {
			socketSessionMap.remove(userId);
		}
		
		super.afterConnectionClosed(session, status);
	}
	
}
