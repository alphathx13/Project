package com.example.Project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Project.dao.ReplyDao;
import com.example.Project.vo.Reply;

@Service
public class ReplyService {

	private ReplyDao replyDao;

	public ReplyService(ReplyDao replyDao) {
		this.replyDao = replyDao;
	}

	public void writeReply(int loginMemberNumber, String replyBody, String relTypeCode, int relId) {
		this.replyDao.writeReply(loginMemberNumber, replyBody, relTypeCode, relId);
	}
	
	public List<Reply> viewReply(String relTypeCode, int relId) {
		return this.replyDao.viewReply(relTypeCode, relId);
	}

	public void replyModify(int id, String replyBody) {
		replyDao.replyModify(id, replyBody);
	}

	public void replyDelete(int id) {
		replyDao.replyDelete(id);
	}

	public Reply getReplyBody(int id) {
		return replyDao.getReplyBody(id);
	}

}