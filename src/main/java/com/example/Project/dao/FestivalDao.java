package com.example.Project.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.Project.vo.Festival;

@Mapper
public interface FestivalDao {

	@Select("""
			SELECT IFNULL(dataStnDt, "")
				FROM festivalList
				WHERE eventSeq = #{eventSeq};
			""")
	public String dataStnDtCheck(int eventSeq);

	@Insert("""
			INSERT INTO festivalList
				SET eventSeq = #{eventSeq}
					, contents = #{contents}
					, placeCdNm = #{placeCdNm}
					, targetCdNm = #{targetCdNm}
					, managementCdNm = #{managementCdNm}
					, themeCdNm = #{themeCdNm}
					, title = #{title}
					, beginDt = #{beginDt}
					, endDt = #{endDt}
					, themeCd = #{themeCd}
					, placeCd = #{placeCd}
					, imageLink = #{imageLink}
					, recommendationYn = #{recommendationYn}
					, hotYn = #{hotYn}
					, useYn = #{useYn}
					, hit = #{hit}
					, beginTm = #{beginTm}
					, endTm = #{endTm}
					, recommendationPoint = #{recommendationPoint}
					, notRecommandationPoint = #{notRecommandationPoint}
					, targetCd = #{targetCd}
					, managementCd = #{managementCd}
					, placeDetail = #{placeDetail}
					, dataStnDt = #{dataStnDt}
					, opmtnInstt = #{opmtnInstt}
					, chargeInfo = #{chargeInfo}
					, eventTm = #{eventTm}
					, prpleHoldYn = #{prpleHoldYn}
					, homepageAdd = #{homepageAdd}
			""")
	public void insert(Festival festival); 

	@Update("""
			UPDATE festivalList
				SET contents = #{contents}
					, placeCdNm = #{placeCdNm}
					, targetCdNm = #{targetCdNm}
					, managementCdNm = #{managementCdNm}
					, themeCdNm = #{themeCdNm}
					, title = #{title}
					, beginDt = #{beginDt}
					, endDt = #{endDt}
					, themeCd = #{themeCd}
					, placeCd = #{placeCd}
					, imageLink = #{imageLink}
					, recommendationYn = #{recommendationYn}
					, hotYn = #{hotYn}
					, useYn = #{useYn}
					, hit = #{hit}
					, beginTm = #{beginTm}
					, endTm = #{endTm}
					, recommendationPoint = #{recommendationPoint}
					, notRecommandationPoint = #{notRecommandationPoint}
					, targetCd = #{targetCd}
					, managementCd = #{managementCd}
					, placeDetail = #{placeDetail}
					, dataStnDt = #{dataStnDt}
					, opmtnInstt = #{opmtnInstt}
					, chargeInfo = #{chargeInfo}
					, eventTm = #{eventTm}
					, prpleHoldYn = #{prpleHoldYn}
					, homepageAdd = #{homepageAdd}
				WHERE eventSeq = ${eventSeq}
			""")
	public void update(Festival festival);

	@Select("""
			SELECT * FROM festivalList
				ORDER BY beginDt DESC
				LIMIT #{number};
			""")
	public List<Festival> festivalList(int number);

	@Select("""
			SELECT * FROM festivalList
				WHERE eventSeq = #{eventSeq}
			""")
	public Festival festivalDetail(int eventSeq);
}