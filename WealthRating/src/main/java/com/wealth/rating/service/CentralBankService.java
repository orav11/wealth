package com.wealth.rating.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;

@Service
public class CentralBankService {

    private final RestTemplate restTemplate;


    @Autowired
    public CentralBankService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public BigInteger getCityAssetEvaluation(String city){
        return restTemplate.getForObject(String.format("central-bank/regional-info/evaluate?city=%s",city), BigInteger.class);
    }

    public BigInteger getWealthThreshold(){
        return restTemplate.getForObject("central-bank/wealth-threshold",BigInteger.class);
    }

}
