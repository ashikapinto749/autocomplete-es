package com.example.demo.controller;
import com.example.demo.service.HouseKeepingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HouseKeepingController {

    @Autowired
    private HouseKeepingService service;

    @GetMapping("/load")
    public String load(){
        service.load();
        return "data loaded";
    }
}


