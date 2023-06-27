package kosa.hdit5spring.whereru.main.mapper;

import org.apache.ibatis.annotations.Mapper;

import kosa.hdit5spring.whereru.main.vo.MissingBoardVo;

@Mapper
public interface MissingBoardMapper {

   public void writeMissingBoard(MissingBoardVo missingBoardVo);
   MissingBoardVo selectMissingBoardDetail(int missingSeq);
   void deleteMissingBoard(int missingSeq);
   void updateMissingBoard(MissingBoardVo missingBoardVo);   
}