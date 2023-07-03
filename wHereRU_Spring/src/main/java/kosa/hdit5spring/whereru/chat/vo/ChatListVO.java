package kosa.hdit5spring.whereru.chat.vo;

import java.util.List;

import kosa.hdit5spring.whereru.user.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatListVO {

	private int roomSeq;
	private String senderId;
	private String senderImg;
	private String senderName;
	private String lastChatType;
	private String lastChatContent;
	private String lastChatDate;
	private String chatCount;
}
