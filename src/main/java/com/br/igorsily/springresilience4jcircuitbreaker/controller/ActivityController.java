package com.br.igorsily.springresilience4jcircuitbreaker.controller;


import com.br.igorsily.springresilience4jcircuitbreaker.model.Activity;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/activity")
@RestController
public class ActivityController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;

    private Integer count = 0;

    public ActivityController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CircuitBreaker(name = "randomActivity", fallbackMethod = "fallbackRandomActivity")
    public ResponseEntity<Activity> getActivity() {
        logger.info("Requesting activity");
        String API_OFFLINE = "https://www.boredapi.com:8081/api/activity";
        String BORED_API = "https://www.boredapi.com/api/activity";
        ResponseEntity<Activity> response = restTemplate.getForEntity(count % 2 == 0 ? BORED_API : API_OFFLINE, Activity.class);
        Activity activity = response.getBody();
        logger.info("Activity: {}", activity);
        count++;
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }

    public ResponseEntity<Activity> fallbackRandomActivity(Exception e) {
        logger.error("Error: {}", e.getMessage());
        Activity activity = new Activity("No activity today", "No type", 0, 0, "No link", "No key", "No accessibility");
        count++;
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }

}
