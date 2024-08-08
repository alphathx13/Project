package com.example.Project.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.Project.service.AWSSESService;

@RestController
public class AWSSESController {
    
    private AWSSESService service;
    
    public AWSSESController (AWSSESService service) {
    	this.service = service;
    }
    
}