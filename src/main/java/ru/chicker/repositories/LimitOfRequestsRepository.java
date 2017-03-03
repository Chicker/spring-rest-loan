package ru.chicker.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.chicker.entities.LimitOfRequests;

import java.time.LocalDateTime;

@Repository
public interface LimitOfRequestsRepository extends CrudRepository<LimitOfRequests, Long> {
    LimitOfRequests findByCountryCodeAndStartDateIsLessThanEqualAndEndDateIsGreaterThanEqual(
        String countryCode, LocalDateTime date1, LocalDateTime date2);
}
