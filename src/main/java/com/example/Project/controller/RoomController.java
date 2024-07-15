package com.example.Project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Project.vo.ChatRoomRepository;
import com.example.Project.vo.Rq;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Log4j2
public class RoomController {

    private final ChatRoomRepository repository;
    
    @Autowired
    Rq rq;

    //채팅방 목록 조회
    @GetMapping(value = "/rooms")
    public ModelAndView rooms(){
        ModelAndView mv = new ModelAndView("chat/rooms");
        mv.addObject("list", repository.findAllRooms());

        return mv;
    }

    //채팅방 개설
    @PostMapping(value = "/room")
    public String create(@RequestParam String name, RedirectAttributes rttr){
        rttr.addFlashAttribute("roomName", repository.createChatRoom(name));
        return "redirect:/chat/rooms";
    }

    //채팅방 조회
    @GetMapping("/room")
    public String getRoom(String roomId, Model model){
        model.addAttribute("room", repository.findRoomById(roomId));
        model.addAttribute("nickname", rq.getLoginMemberNn());
        
        return "/chat/room";
    }
}