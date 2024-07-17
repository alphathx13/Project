package com.example.Project.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.WebUtils;

import com.example.Project.chat.ChatRoomRepository;
import com.example.Project.service.FestivalService;
import com.example.Project.util.Util;
import com.example.Project.vo.Festival;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FestivalController {
	
	private FestivalService festivalService;
	private final ChatRoomRepository chatRoomRepository;

	public FestivalController(FestivalService festivalService, ChatRoomRepository chatRoomRepository) {
		this.festivalService = festivalService;
		this.chatRoomRepository = chatRoomRepository;
	}
	
	@Value("${custom.api.key}")
	private String apiKey;
	
	@Value("${custom.kakao.key}")
	private String kakaoKey;
	
	@GetMapping("/user/festival/list")
	public String list(Model model) {
		model.addAttribute("festivalList", festivalService.festivalList(500));
		model.addAttribute("today", Util.today());
		return "/user/festival/list";
	}
	
	@GetMapping("/user/festival/detail")
	public String detail(HttpServletRequest request, HttpServletResponse response, Model model, int eventSeq) {
		
		boolean isViewed = false;	
		
		if (WebUtils.getCookie(request, "viewedFestival_"+ eventSeq) != null) {
			isViewed = true;
		}

		if (!isViewed) {
			festivalService.viewCountPlus(eventSeq);
			Cookie cookie = new Cookie("viewedFestival_" + eventSeq, "true");
			cookie.setMaxAge(5);
			response.addCookie(cookie);
		}
		
		model.addAttribute("festival", festivalService.festivalDetail(eventSeq));
		model.addAttribute("kakaoKey", kakaoKey);
		model.addAttribute("weatherMid", eventSeq);
		
		return "/user/festival/detail";
	}
	
	// 이건 테스트용으로 남겨둔것
	@GetMapping("/user/festival/festivalUpdate")
	public String festivalUpdate() throws IOException, ParseException {
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6300000/eventDataService/eventDataListJson"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + apiKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("500", "UTF-8")); /**/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /**/
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
        rd.close();
        conn.disconnect();
        
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
        
        ObjectMapper objectMapper = new ObjectMapper();
        List<Festival> festivalList = objectMapper.readValue(jsonObject.get("msgBody").toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Festival.class));
        
        for (Festival festival : festivalList) {
        	String dataStnDtCheck = festivalService.dataStnDtCheck(festival.getEventSeq());
        	if (dataStnDtCheck == null) {
        		festivalService.insert(festival);
        		chatRoomRepository.createChatRoom(String.valueOf(festival.getEventSeq()));
        		// 이전에 기록이 없는 새로운 행사인 경우, 목록조회 DB에 추가
        	} else if (dataStnDtCheck != festival.getDataStnDt()) {
        		festivalService.update(festival);
        		// 이미 존재하는 행사에서 데이터기준일 변경 => 변경된 정보가 있으니 업데이트
        	}
        }
        return "/user/home/main";
	}
	
	
	
}