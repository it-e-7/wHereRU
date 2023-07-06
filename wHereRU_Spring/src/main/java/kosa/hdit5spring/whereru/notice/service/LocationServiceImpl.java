package kosa.hdit5spring.whereru.notice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kosa.hdit5spring.whereru.notice.mapper.NoticeMapper;
import kosa.hdit5spring.whereru.notice.vo.LocationVO;

@Service
public class LocationServiceImpl implements LocationService {

	@Autowired
	NoticeMapper mapper;

	@Override
	public boolean setLocation(LocationVO locvo) {

		int cnt = mapper.setLocation(locvo);
		if (cnt == 1) {
			return true;
		}
		return false;
	}

	@Override
	public List<String> getTokenList(int userSeq){
		List<String> targetList = new ArrayList<String>();
		//내 토큰과 위치값 가져오기
		LocationVO myLocVo = getMyLoc(userSeq);
		//내 토큰 들고가서 나를 제외한 타겟 토큰과 위치값 가져오기
		List<LocationVO> targetLocList = getTargetLoc(myLocVo.getUserToken());
		//내 위치와 비교>>범위내 타겟만 리스트로 만들기
		for(LocationVO vo: targetLocList) {
			if(!vo.getUserToken().equals(myLocVo.getUserToken())) {
				if(myLocVo.getLatitude()-0.01 < vo.getLatitude() && vo.getLatitude() < myLocVo.getLatitude()+0.01 &&
						   myLocVo.getLongitude()-0.01 < vo.getLongitude() && vo.getLongitude()	< myLocVo.getLongitude()+0.01) {
							targetList.add(vo.getUserToken());
							System.out.println(vo.getUserToken());
						}
			} 	
		}
		return targetList;
	}
	
	
	@Override
	public LocationVO getMyLoc(int userSeq) {
		LocationVO myLocvo = mapper.getMyLoc(userSeq);
		return myLocvo;
	}

	@Override
	public List<LocationVO> getTargetLoc(String token) {
		List<LocationVO> targetLocList = mapper.getTargetLoc(token);
		return targetLocList;
	}
}
