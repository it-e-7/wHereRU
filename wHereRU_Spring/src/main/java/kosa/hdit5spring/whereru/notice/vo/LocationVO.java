package kosa.hdit5spring.whereru.notice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationVO {

	private String userToken;
	private double longitude;
	private double latitude;
	
}
