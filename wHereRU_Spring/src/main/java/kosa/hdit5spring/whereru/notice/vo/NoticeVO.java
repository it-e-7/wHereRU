package kosa.hdit5spring.whereru.notice.vo;

import kosa.hdit5spring.whereru.main.vo.MissingBoardVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeVO {

	private int noticeSeq;
	private String notiSender;
	private String notiReceiver;
	private String notiMessage;
	private String notiTime;
	private String notiType;
	private String msgType;
}
