package ru.chicker.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.chicker.entities.BlacklistedPerson;

@Repository
public interface BlacklistedPersonRepository extends CrudRepository<BlacklistedPerson, String> {

}
