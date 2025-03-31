package com.vijay.matching.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatchingClientController {

    @GetMapping(value = "/getTest", produces = MediaType.TEXT_PLAIN_VALUE)
    public String submitOrder(){
        return "test";
    }
}
