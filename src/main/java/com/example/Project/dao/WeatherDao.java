package com.example.Project.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.Project.vo.WeatherMid;
import com.example.Project.vo.WeatherMidRain;
import com.example.Project.vo.WeatherMidTemp;
import com.example.Project.vo.WeatherShort;

@Mapper
public interface WeatherDao {

//	@Select("""
//			SELECT IFNULL(dataStnDt, "")
//				FROM festivalList
//				WHERE eventSeq = #{eventSeq};
//			""")
//	public String dataStnDtCheck(int eventSeq);
//
//	@Insert("""
//			INSERT INTO festivalList
//				SET eventSeq = #{eventSeq}
//					, contents = #{contents}
//					, placeCdNm = #{placeCdNm}
//					, targetCdNm = #{targetCdNm}
//					, managementCdNm = #{managementCdNm}
//					, themeCdNm = #{themeCdNm}
//					, title = #{title}
//					, beginDt = #{beginDt}
//					, endDt = #{endDt}
//					, themeCd = #{themeCd}
//					, placeCd = #{placeCd}
//					, imageLink = #{imageLink}
//					, recommendationYn = #{recommendationYn}
//					, hotYn = #{hotYn}
//					, useYn = #{useYn}
//					, hit = #{hit}
//					, beginTm = #{beginTm}
//					, endTm = #{endTm}
//					, recommendationPoint = #{recommendationPoint}
//					, notRecommandationPoint = #{notRecommandationPoint}
//					, targetCd = #{targetCd}
//					, managementCd = #{managementCd}
//					, placeDetail = #{placeDetail}
//					, dataStnDt = #{dataStnDt}
//					, opmtnInstt = #{opmtnInstt}
//					, chargeInfo = #{chargeInfo}
//					, eventTm = #{eventTm}
//					, prpleHoldYn = #{prpleHoldYn}
//					, homepageAdd = #{homepageAdd}
//			""")
//	public void insert(Festival festival); 
//
//	@Update("""
//			UPDATE festivalList
//				SET contents = #{contents}
//					, placeCdNm = #{placeCdNm}
//					, targetCdNm = #{targetCdNm}
//					, managementCdNm = #{managementCdNm}
//					, themeCdNm = #{themeCdNm}
//					, title = #{title}
//					, beginDt = #{beginDt}
//					, endDt = #{endDt}
//					, themeCd = #{themeCd}
//					, placeCd = #{placeCd}
//					, imageLink = #{imageLink}
//					, recommendationYn = #{recommendationYn}
//					, hotYn = #{hotYn}
//					, useYn = #{useYn}
//					, hit = #{hit}
//					, beginTm = #{beginTm}
//					, endTm = #{endTm}
//					, recommendationPoint = #{recommendationPoint}
//					, notRecommandationPoint = #{notRecommandationPoint}
//					, targetCd = #{targetCd}
//					, managementCd = #{managementCd}
//					, placeDetail = #{placeDetail}
//					, dataStnDt = #{dataStnDt}
//					, opmtnInstt = #{opmtnInstt}
//					, chargeInfo = #{chargeInfo}
//					, eventTm = #{eventTm}
//					, prpleHoldYn = #{prpleHoldYn}
//					, homepageAdd = #{homepageAdd}
//				WHERE eventSeq = ${eventSeq}
//			""")
//	public void update(Festival festival);
//
//	@Select("""
//			SELECT * FROM festivalList
//				ORDER BY eventSeq DESC
//				LIMIT #{number};
//			""")
//	public List<Festival> festivalList(int number);
//
//	@Select("""
//			SELECT * FROM festivalList
//				WHERE eventSeq = #{eventSeq}
//			""")
//	public Festival festivalDetail(int eventSeq);

	@Update("""
			UPDATE WeatherMidFcst
				SET updateTime = #{updateTime}
					, rnSt3Am = #{rnSt3Am}
					, rnSt3Pm = #{rnSt3Pm}
					, rnSt4Am = #{rnSt4Am}
					, rnSt4Pm = #{rnSt4Pm}
					, rnSt5Am = #{rnSt5Am}
					, rnSt5Pm = #{rnSt5Pm}
					, rnSt6Am = #{rnSt6Am}
					, rnSt6Pm = #{rnSt6Pm}
					, rnSt7Am = #{rnSt7Am}
					, rnSt7Pm = #{rnSt7Pm}
					, wf3Am = #{wf3Am}
					, wf3Pm = #{wf3Pm}
					, wf4Am = #{wf4Am}
					, wf4Pm = #{wf4Pm}
					, wf5Am = #{wf5Am}
					, wf5Pm = #{wf5Pm}
					, wf6Am = #{wf6Am}
					, wf6Pm = #{wf6Pm}
					, wf7Am = #{wf7Am}
					, wf7Pm = #{wf7Pm}
			""")
	public void weatherMidRainUpdate(WeatherMidRain weatherMidRain);
	
	@Update("""
			UPDATE WeatherMidFcst
				SET updateTime = #{updateTime}
					, taMin3 = #{taMin3}	
					, taMax3 = #{taMax3}
					, taMin4 = #{taMin4}	
					, taMax4 = #{taMax4}
					, taMin5 = #{taMin5}	
					, taMax5 = #{taMax5}
					, taMin6 = #{taMin6}	
					, taMax6 = #{taMax6}
					, taMin7 = #{taMin7}	
					, taMax7 = #{taMax7}
			""")
	public void weatherMidTempUpdate(WeatherMidTemp weatherMidTemp);

	@Update("""
			TRUNCATE TABLE WeatherShortFcst
			""")
	public void weatherShortDelete();

	@Insert("""
			INSERT INTO WeatherShortFcst
				SET baseDate = #{baseDate}
					, baseTime = #{baseTime}
					, category = #{category}
					, fcstDate = #{fcstDate}
					, fcstTime = #{fcstTime}
					, fcstValue = #{fcstValue}
			""")
	public void weatherShortInsert(WeatherShort weatherShort);

	@Select("""
			SELECT * FROM WeatherMidFcst
			""")
	public WeatherMid weatherMidView();

	@Select("""
			SELECT category, fcstDate, fcstTime, fcstValue FROM WeatherShortFcst
				WHERE fcstTime IN (0600, 0900, 1200, 1500, 1800)
			""")
	public List<WeatherShort> weatherShortView();
}