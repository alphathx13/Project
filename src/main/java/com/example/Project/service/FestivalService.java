package com.example.Project.service;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Project.dao.FestivalDao;
import com.example.Project.vo.Festival;
import com.example.Project.vo.FestivalForList;

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

	public  List<FestivalForList> festivalList(int type, int searchType, String searchText) {
		return festivalDao.festivalList(type, searchType, searchText);
	}

	public Festival festivalDetail(int eventSeq) {
		return festivalDao.festivalDetail(eventSeq);
	}

	public void viewCountPlus(int eventSeq) {
		festivalDao.viewCountPlus(eventSeq);
	}

	public int festivalListCount(int type, int searchType, String searchText) {
		return festivalDao.festivalListCount(type, searchType, searchText);
	}
	
	public void imageDelete(String path) {
		File file = new File(path);
        file.delete();
	}

}