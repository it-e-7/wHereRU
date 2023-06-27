package kosa.hdit5spring.whereru.main.vo;

import lombok.Data;

@Data
public class MissingBoardDetailVO {
	
	private int missingSeq;
	private String missingName;
	private int missingAge;
	private String missingSex;
	private String missingOutfit;
	private String missingTime;
	private String missingPoint;
	private String userSeq;
	private boolean isOwner;
}
