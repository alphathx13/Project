package com.example.Project.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Project.service.WeatherService;
import com.example.Project.vo.WeatherMid;
import com.example.Project.vo.WeatherMidRain;
import com.example.Project.vo.WeatherMidTemp;
import com.example.Project.vo.WeatherShort;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class WeatherController {
	
	private String updateTime;
	
	{
		updateTime = "20240710";
	}
	
	private WeatherService weatherService;

	public WeatherController(WeatherService weatherService) {
		this.weatherService = weatherService;
	}
//	
//	@GetMapping("/weatherList")
//	public String list(Model model) {
//		model.addAttribute("festivalList", weatherController.weatherList());
//		return "/list";
//	}
	
	@Value("${custom.api.key}")
	private String apiKey;
	
	@GetMapping("/user/weatherMidUpdate")
	public String weatherMidUpdate() throws IOException, ParseException {
		// 강수 업데이트
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst"); 
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + apiKey); 
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("500", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("regId","UTF-8") + "=" + URLEncoder.encode("11C20000", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("tmFc","UTF-8") + "=" + URLEncoder.encode(updateTime, "UTF-8") + URLEncoder.encode("0600", "UTF-8"));
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
        	sb.append(line);
        }
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(sb.toString());
        JsonNode itemNode = rootNode.path("response").path("body").path("items").path("item");
        WeatherMidRain weatherMidRain = objectMapper.treeToValue(itemNode.get(0), WeatherMidRain.class);
        weatherMidRain.setUpdateTime(updateTime);
        
        // 기온 업데이트
        urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa"); 
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + apiKey); 
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("500", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("regId","UTF-8") + "=" + URLEncoder.encode("11C20401", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("tmFc","UTF-8") + "=" + URLEncoder.encode(updateTime, "UTF-8") + URLEncoder.encode("0600", "UTF-8")); 
        url = new URL(urlBuilder.toString());
        conn = (HttpURLConnection) url.openConnection();
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        sb = new StringBuilder();
        while ((line = rd.readLine()) != null) {
        	sb.append(line);
        }
        rd.close();
        conn.disconnect();
        
        rootNode = objectMapper.readTree(sb.toString());
        itemNode = rootNode.path("response").path("body").path("items").path("item");
        WeatherMidTemp weatherMidTemp = objectMapper.treeToValue(itemNode.get(0), WeatherMidTemp.class);
        weatherMidTemp.setUpdateTime(updateTime);
        
        weatherService.weatherMidRainUpdate(weatherMidRain);
        weatherService.weatherMidTempUpdate(weatherMidTemp);
        
        return "/user/home/main";
	}
	
	@GetMapping("/weatherShortUpdate")
	public String weatherShortUpdate() throws IOException, ParseException {
		String[] category = {"TMP", "SKY", "POP"};
//		String[] category = {"TMP", "SKY", "POP", "PCP", "SNO"};
		
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); 
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + apiKey); 
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(updateTime, "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode("0500", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("67", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("100", "UTF-8"));
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(sb.toString());
        JsonNode itemNode = rootNode.path("response").path("body").path("items").path("item");
        
        weatherService.weatherShortDelete();
        
        List<WeatherShort> weatherShortList = new ArrayList<>();

        for (int i = 0; i < itemNode.size(); i++) {
        	weatherShortList.add(objectMapper.treeToValue(itemNode.get(i), WeatherShort.class));
        }
        
        for (WeatherShort weatherShort : weatherShortList) {
        	for (String values : category) {
        		if (weatherShort.getCategory().equals(values)) {
        			if(weatherShort.getCategory().equals("SKY")) {
        				if(weatherShort.getFcstValue().equals("3")) {
        					weatherShort.setFcstValue("구름많음");
        				} else if(weatherShort.getFcstValue().equals("4")) {
        					weatherShort.setFcstValue("흐림");
        				} else {
        					weatherShort.setFcstValue("맑음");
        				}
        			}
        		
            		weatherService.weatherShortInsert(weatherShort);
        		}
        	}
        }
        
        return "/user/home/main";
	}
	
	@GetMapping("/weatherMidView")
	@ResponseBody
	public WeatherMid weatherMidView(){
		return weatherService.weatherMidView();
	}

	@GetMapping("/weatherShortView")
	@ResponseBody
	public List<WeatherShort> weatherShortView(){
		return weatherService.weatherShortView();
	}
}