package com.circuitBreaker.controller;

import com.circuitBreaker.model.Activity;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
@RequestMapping("/activity")
public class ActivityController {

    private RestTemplate restTemplate;

    public ActivityController(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }
    private final String api = "https://www.boredapi.com/api/activity";

    private final String API = "https://www.youtube.com/";
    @GetMapping
    @CircuitBreaker(name ="randomActivity",fallbackMethod = "fallBackRandom") //for cascading failures
//    @Retry(name = "randomActivity",fallbackMethod = "fallBackRandom") //for transient failures
    public String getRandomActivity(){
        ResponseEntity<Activity> responseEntity= restTemplate.getForEntity(API, Activity.class);
        Activity activity = responseEntity.getBody();
        log.info("Activity Recieved: " + activity.getActivity());
        return activity.getActivity();

    }
    public String fallBackRandom(Throwable throwable){
        return "watch youtube";
    }
}
