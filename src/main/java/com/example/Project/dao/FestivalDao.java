package com.example.Project.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.Project.vo.Festival;
import com.example.Project.vo.FestivalForList;

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
			<script>
			SELECT f.eventSeq, f.themeCdNm, f.title, f.beginDt, f.endDt, IFNULL(SUM(l.point), 0) `likePoint`, f.viewCount
				FROM festivalList f
				LEFT OUTER JOIN likePoint l
				ON f.eventSeq = l.relId AND l.relTypeCode = 'festival'
				<if test="type != 0">
					<choose>
						<when test="type == 1">
							WHERE CURRENT_DATE() BETWEEN f.beginDt AND f.endDt
						</when>
						<when test="type == 2">
							WHERE DATEDIFF(f.beginDt, CURRENT_DATE()) > 0
						</when>
						<otherwise>
							WHERE DATEDIFF(CURRENT_DATE(), f.endDt) > 0
						</otherwise>
					</choose>
				</if>
                GROUP BY f.eventSeq
                <if test="type != 0">
					<choose>
						<when test="type == 1">
							ORDER BY endDt ASC
						</when>
						<when test="type == 2">
							ORDER BY beginDt ASC
						</when>
						<otherwise>
							ORDER BY endDt DESC
						</otherwise>
					</choose>
				</if>
			</script>
			""")
	public List<FestivalForList> festivalList(int type);

	@Select("""
			SELECT f.*, IFNULL(SUM(l.point), 0) `likePoint`
				FROM festivalList f
				LEFT OUTER JOIN likePoint l
					ON f.eventSeq = l.relId AND l.relTypeCode = 'festival'
				WHERE eventSeq = #{eventSeq}
				GROUP BY f.eventSeq
			""")
	public Festival festivalDetail(int eventSeq);

	@Update("""
			UPDATE FestivalList
				set viewCount = viewCount+1
				where eventSeq = #{eventSeq};
			""")
	public void viewCountPlus(int eventSeq);

}