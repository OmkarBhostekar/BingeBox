package com.bingebox.eventservice.controller;


import com.bingebox.eventservice.repository.EventShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/events/shows")
@RequiredArgsConstructor
public class EventShowController {

    private final EventShowRepository eventShowRepository;

//    @PostMapping
//    public ResponseEntity<>


}
