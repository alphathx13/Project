package com.example.Project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Project.dao.FestivalDao;
import com.example.Project.vo.Festival;

@Service
public class FestivalService {

	private FestivalDao festivalDao;

	public FestivalService(FestivalDao festivalDao) {
		this.festivalDao = festivalDao;
	}

	public String dataStnDtCheck(int eventSeq) {
		return festivalDao.dataStnDtCheck(eventSeq);
	}

	public void insert(Festival festival) {
		festivalDao.insert(festival);
	}

	public void update(Festival festival) {
		festivalDao.update(festival);
	}

	public  List<Festival> festivalList(int type) {
		return festivalDao.festivalList(type);
	}

	public Festival festivalDetail(int eventSeq) {
		return festivalDao.festivalDetail(eventSeq);
	}

	public void viewCountPlus(int eventSeq) {
		festivalDao.viewCountPlus(eventSeq);
	}

}