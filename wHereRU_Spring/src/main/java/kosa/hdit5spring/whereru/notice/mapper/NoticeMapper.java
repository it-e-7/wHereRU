package kosa.hdit5spring.whereru.notice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper {

	List<String> getTokenList(int userSeq);
	
}
