package com.example.Project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Project.dao.WeatherDao;
import com.example.Project.vo.WeatherMid;
import com.example.Project.vo.WeatherMidRain;
import com.example.Project.vo.WeatherMidTemp;
import com.example.Project.vo.WeatherShort;

@Service
public class WeatherService {

	private WeatherDao weatherDao;

	public WeatherService(WeatherDao weatherDao) {
		this.weatherDao = weatherDao;
	}

	public void weatherMidRainUpdate(WeatherMidRain weatherMidRain) {
		weatherDao.weatherMidRainUpdate(weatherMidRain);
	}
	
	public void weatherMidTempUpdate(WeatherMidTemp weatherMidTemp) {
		weatherDao.weatherMidTempUpdate(weatherMidTemp);
	}

	public void weatherShortDelete() {
		weatherDao.weatherShortDelete();
	}

	public void weatherShortInsert(WeatherShort weatherShort) {
		weatherDao.weatherShortInsert(weatherShort);
	}
	
	public WeatherMid weatherMidView() {
		return weatherDao.weatherMidView();
	}

	public List<WeatherShort> weatherShortView() {
		return weatherDao.weatherShortView();
	}

//	public String dataStnDtCheck(int eventSeq) {
//		return festivalDao.dataStnDtCheck(eventSeq);
//	}
//
//	public void insert(Festival festival) {
//		festivalDao.insert(festival);
//	}
//
//	public void update(Festival festival) {
//		festivalDao.update(festival);
//	}
//
//	public  List<Festival> festivalList(int number) {
//		return festivalDao.festivalList(number);
//	}
//
//	public Festival festivalDetail(int eventSeq) {
//		return festivalDao.festivalDetail(eventSeq);
//	}

}