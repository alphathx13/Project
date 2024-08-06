package com.example.Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project.service.AWSSESService;

@RestController
public class AWSSESController {
    
    @Autowired
    private AWSSESService service;
    
    @GetMapping("/sendmail")
    public void sendMail(){
    }
}