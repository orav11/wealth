package com.wealth.rating.repository;

import com.wealth.rating.model.PersonWealth;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RichPersonRepository extends ListCrudRepository<PersonWealth, Long> {
}
