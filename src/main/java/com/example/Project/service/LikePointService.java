package com.example.Project.service;

import org.springframework.stereotype.Service;

import com.example.Project.dao.LikePointDao;
import com.example.Project.vo.ResultData;

@Service
public class LikePointService {

	private LikePointDao likePointDao;

	public LikePointService(LikePointDao likePointDao) {
		this.likePointDao = likePointDao;
	}

	
	public void doLike(int memberNumber, int relId, String relTypeCode) {
		this.likePointDao.doLike(memberNumber, relId, relTypeCode);
	}

	public void undoLike(int memberNumber, int relId, String relTypeCode) {
		this.likePointDao.undoLike(memberNumber, relId, relTypeCode);
	}

	public int totalLikePoint(int relId, String relTypeCode) {
		return likePointDao.totalLikePoint(relId, relTypeCode);
	}

	public ResultData<Integer> likeCheck(int loginMemberNumber, int relId, String relTypeCode) {
		
		int likeCheck = likePointDao.likeCheck(loginMemberNumber, relId, relTypeCode);
		int totalLikeCount = likePointDao.totalLikePoint(relId, relTypeCode);

		if (likeCheck == 0) {
			return ResultData.from("F-1", "좋아요 없음", totalLikeCount);
		}
		
		return ResultData.from("S-1", "좋아요 있음", totalLikeCount);
	}
	
}
