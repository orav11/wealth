package com.wealth.rating.service;

import com.wealth.rating.model.Person;
import com.wealth.rating.repository.RichPersonRepository;
import com.wealth.rating.model.PersonWealth;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class WealthRatingService {

    private final RichPersonRepository richPersonRepository;

    private final CentralBankService centralBankService;

    @Autowired
    public WealthRatingService(RichPersonRepository richPersonRepository, CentralBankService centralBankService) {
        this.richPersonRepository = richPersonRepository;
        this.centralBankService = centralBankService;
    }


    public List<PersonWealth> getAllRichPeople() {
        return richPersonRepository.findAll();
    }

    public PersonWealth getRichPersonById(long id) {
        return richPersonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find person with id " + id));
    }

    public ResponseEntity<String> calculateFortune(Person person) {
        BigInteger cityEvaluation = centralBankService.getCityAssetEvaluation(person.getPersonalInfo().getCity());
        BigInteger threshold = centralBankService.getWealthThreshold();


        int personAssets = person.getFinancialInfo().getNumberOfAssets();

        BigInteger fortune = person.getFinancialInfo().getCash().add(cityEvaluation.multiply(new BigInteger(String.valueOf(personAssets))));
        if(fortune.compareTo(threshold) >= 0){
            PersonWealth personWealth = createNewRichPerson(person);
            richPersonRepository.save(personWealth);
            return ResponseEntity.ok().body(String.format("New person was added to the db with fortune of %d while threshold is %d",fortune,threshold));
        }
        else if(isPresent(person)){
            removePersonFromDb(person.getId());
            return ResponseEntity.ok().body(String.format("Person was deleted from db since he's not rich anymore, threshold is %d and his fortune is %d",threshold,fortune));
        }
        return ResponseEntity.ok().body(String.format("Person was not added to the db since threshold is %d and his fortune is %d",threshold,fortune));
    }

    private boolean isPresent(Person person) {
        return richPersonRepository.findById(person.getId()).isPresent();
    }

    private void removePersonFromDb(long id){
        richPersonRepository.deleteById(id);
    }

    private PersonWealth createNewRichPerson(Person person){
        return new PersonWealth(person.getId(), person.getPersonalInfo().getFirstName(),
                person.getPersonalInfo().getLastName(), person.getFinancialInfo().getCash().toString());
    }

    public void removeAll(){ //only for testing
        richPersonRepository.deleteAll();
    }
}
