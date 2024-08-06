package com.example.Project.schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.Project.chat.ChatRoomRepository;
import com.example.Project.service.FestivalService;
import com.example.Project.service.FileService;
import com.example.Project.service.MemberService;
import com.example.Project.service.WeatherService;
import com.example.Project.util.Util;
import com.example.Project.vo.Festival;
import com.example.Project.vo.WeatherMidRain;
import com.example.Project.vo.WeatherMidTemp;
import com.example.Project.vo.WeatherShort;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Scheduler {
	
	private FestivalService festivalService;
	private WeatherService weatherService;
	private MemberService memberService;
	private FileService fileService;
	private String date;
	private final ChatRoomRepository chatRoomRepository;

	public Scheduler(FestivalService festivalService, WeatherService weatherService, MemberService memberService, FileService fileService, ChatRoomRepository chatRoomRepository) {
		this.festivalService = festivalService;
		this.weatherService = weatherService;
		this.memberService = memberService;
		this.fileService = fileService;
		this.chatRoomRepository = chatRoomRepository;
	}
	
	{
		date = Util.today();
	}

	@Value("${custom.api.key}")
	private String apiKey;
	
	@Scheduled(cron = "5 0 0 * * *")
	public void dateUpdate() {
		date = Util.today();
	}

	// 행사 업데이트
	@Scheduled(cron = "5 0 5 * * *")
	public void festivalUpdate() throws IOException, ParseException {
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6300000/eventDataService/eventDataListJson"); 
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + apiKey); 
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("500", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); 
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
        	if (festival.getEndDt().compareTo(Util.today()) < 0) {
        		continue;
        	}

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
    }
    
	// 중기 날씨 업데이트
    @Scheduled(cron = "0 5 6 * * *")
	public void weatherMidUpdate() throws IOException, ParseException {
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst"); 
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + apiKey); 
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("500", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("regId","UTF-8") + "=" + URLEncoder.encode("11C20000", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("tmFc","UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + URLEncoder.encode("0600", "UTF-8"));
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
        weatherMidRain.setUpdateTime(date);
        
        // 기온 업데이트
        urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa"); 
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + apiKey); 
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("500", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("regId","UTF-8") + "=" + URLEncoder.encode("11C20401", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("tmFc","UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + URLEncoder.encode("0600", "UTF-8")); 
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
        weatherMidTemp.setUpdateTime(date);
        
        weatherService.weatherMidRainUpdate(weatherMidRain);
        weatherService.weatherMidTempUpdate(weatherMidTemp);
        
	}
	
    // 단기 날씨 업데이트
    @Scheduled(cron = "0 5 5 * * *")
	public void weatherShortUpdate() throws IOException, ParseException {
		String[] category = {"TMP", "SKY", "POP"};
		
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); 
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + apiKey); 
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); 
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")); 
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
	}
	
    // 탈퇴신청 계정 삭제
    @Scheduled(cron = "0 0 6 * * *")
	public void memberDelete() {
    	List<Integer> memberImgList = memberService.getDeleteMemberImg();

    	fileService.memberImgDelete(memberImgList);
    	memberService.memberDelete();
    }
    
}