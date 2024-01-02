package com.wealth.rating.controller;

import com.wealth.rating.model.Person;
import com.wealth.rating.model.PersonWealth;
import com.wealth.rating.service.WealthRatingService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wealth-rating")
@EnableCaching
public class WealthRatingController {

    private final WealthRatingService wealthRatingService;

    public WealthRatingController(WealthRatingService wealthRatingService) {
        this.wealthRatingService = wealthRatingService;
    }

    @GetMapping
    public List<PersonWealth> getAllRichPeople(){
        return wealthRatingService.getAllRichPeople();
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#id", value = "personId")
    public PersonWealth getRichPersonById(@PathVariable long id){
        return wealthRatingService.getRichPersonById(id);
    }

    @PostMapping("/analyse_status")
    @CacheEvict(key = "#person.id", value = "personId")
    public ResponseEntity<String> calculateFortune(@RequestBody Person person){
         return wealthRatingService.calculateFortune(person);
    }
}
