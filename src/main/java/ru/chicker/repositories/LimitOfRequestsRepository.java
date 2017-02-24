package ru.chicker.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.chicker.entities.LimitOfRequests;

import java.time.LocalDateTime;

public interface LimitOfRequestsRepository extends CrudRepository<LimitOfRequests, Long> {
    LimitOfRequests findByCountryCodeAndStartDateIsLessThanEqualAndEndDateIsGreaterThanEqual(
        String countryCode, LocalDateTime date1, LocalDateTime date2);
}
