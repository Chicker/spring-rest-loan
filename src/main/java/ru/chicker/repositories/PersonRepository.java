package ru.chicker.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.chicker.entities.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
}
