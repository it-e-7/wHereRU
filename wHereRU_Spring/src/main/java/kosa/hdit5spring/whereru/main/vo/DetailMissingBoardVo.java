package kosa.hdit5spring.whereru.main.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailMissingBoardVo {

	private int missingSeq;
	private String missingName;
	private int missingAge;
	private String missingSex;
	private String missingOutfit;
	private String missingTime;
	private String missingPoint;
	private int userSeq;
	
	private boolean isOwner;

	private String missingImgUrls;

}
