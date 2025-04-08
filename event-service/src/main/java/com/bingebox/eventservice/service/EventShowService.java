package com.bingebox.eventservice.service;

import com.bingebox.eventservice.repository.EventShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventShowService {

    private final EventShowRepository eventShowRepository;

}
