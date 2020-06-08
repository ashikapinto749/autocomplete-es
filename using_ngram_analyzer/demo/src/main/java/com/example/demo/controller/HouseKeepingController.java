package com.example.demo.controller;

import java.util.List;

import com.example.demo.service.BookService;
import com.example.demo.service.HouseKeepingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Book;
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


