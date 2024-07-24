package com.example.Project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Project.service.ReplyService;
import com.example.Project.util.Util;
import com.example.Project.vo.Reply;
import com.example.Project.vo.ResultData;
import com.example.Project.vo.Rq;

@Controller
public class ReplyController {
	
	private ReplyService replyService;
	private Rq rq;

	public ReplyController(ReplyService replyService, Rq rq) {
		this.replyService = replyService;
		this.rq = rq;
	}

	@PostMapping("/user/reply/doWrite")
	@ResponseBody
	public String doWrite(String replyBody, String relTypeCode, int relId) {

		replyService.writeReply(rq.getLoginMemberNumber(), replyBody, relTypeCode, relId);

		if (relTypeCode.equals("festival"))
			return Util.jsReplace(String.format("댓글을 작성했습니다"), String.format("../festival/detail?eventSeq=%d", relId));

		return Util.jsReplace(String.format("댓글을 작성했습니다"), String.format("../article/detail?id=%d", relId));
	}

	@PostMapping("/user/reply/viewReply")
	@ResponseBody
	public ResultData <List<Reply>> viewReply(String relTypeCode, int relId) {

		List<Reply> replyList = replyService.viewReply(relTypeCode, relId);
		
		return ResultData.from("S-1", "", replyList);
	}
	
	@PostMapping("/user/reply/replyModify")
	@ResponseBody
	public ResultData replyModify(int id, String body) {
		replyService.replyModify(id, body);
		
		return ResultData.from("S-1", "", true);
	}
	
	@GetMapping("/user/reply/replyDelete")
	@ResponseBody
	public String replyDelete(int id, int eventSeq) {
		replyService.replyDelete(id);
		
		return Util.jsReplace(String.format("댓글을 삭제했습니다"), String.format("../festival/detail?eventSeq=%d", eventSeq));
	}
	
	@PostMapping("/user/reply/getReplyBody")
	@ResponseBody
	public String getReplyBody(int id) {
		
		Reply reply = replyService.getReplyBody(id);
		
		return reply.getBody();
	}

}