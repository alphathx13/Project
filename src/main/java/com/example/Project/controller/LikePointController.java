package com.example.Project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Project.service.LikePointService;
import com.example.Project.vo.ResultData;
import com.example.Project.vo.Rq;

@Controller
public class LikePointController {

	private LikePointService likePointService;
	private Rq rq;

	public LikePointController(LikePointService likePointService, Rq rq) {
		this.likePointService = likePointService;
		this.rq = rq;
	}
	
	@GetMapping("/user/likePoint/doLike")
	@ResponseBody
	public ResultData <Integer> doLike(int relId, String relTypeCode, boolean likeCheck) {
	
		if (likeCheck) {
			likePointService.undoLike(rq.getLoginMemberNumber(), relId, relTypeCode);
			return ResultData.from("undoLike", "좋아요 취소", likePointService.totalLikePoint(relId, relTypeCode));
		}
		
		likePointService.doLike(rq.getLoginMemberNumber(), relId, relTypeCode);
		
		return ResultData.from("doLike", "좋아요 추가", likePointService.totalLikePoint(relId, relTypeCode));
	}
	
	@GetMapping("/user/likePoint/likeCheck")
	@ResponseBody
	public ResultData <Integer> likeCheck(int relId, String relTypeCode) {
		return likePointService.likeCheck(rq.getLoginMemberNumber(), relId, relTypeCode);
	}
	
}