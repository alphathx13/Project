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

	public void update(int eventSeq, String dataStnDt) {
		festivalDao.update(eventSeq, dataStnDt);
	}

	public void detailInsert(int eventSeq) {
		// TODO Auto-generated method stub
	}

	public void detailUpdate(int eventSeq) {
		// TODO Auto-generated method stub
	}

	public  List<Festival> festivalList(int number) {
		return festivalDao.festivalList(number);
	}

	public Festival festivalDetail(int eventSeq) {
		return festivalDao.festivalDetail(eventSeq);
	}

}