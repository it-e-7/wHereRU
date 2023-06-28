package kosa.hdit5spring.whereru.chat.vo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatVO {
	
	String chatSender;
	String chatReceiver;
	String chatType;
	String chatContent;
	String chatDate;
}
