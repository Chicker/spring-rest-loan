package ru.chicker.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LimitsCountryRequests")
public class LimitOfRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "date_start")
    private LocalDateTime startDate;

    @Column(name = "date_end")
    private LocalDateTime endDate;

    @Column(name = "requests_limit")
    private long requestsLimit;

    private long requested;

    protected LimitOfRequests() {
    }

    public LimitOfRequests(String countryCode, LocalDateTime startDate, LocalDateTime endDate,
                           long requestsLimit, long requested) {
        this.countryCode = countryCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requestsLimit = requestsLimit;
        this.requested = requested;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public long getRequestsLimit() {
        return requestsLimit;
    }

    public void setRequestsLimit(long requestsLimit) {
        this.requestsLimit = requestsLimit;
    }

    public long getRequested() {
        return requested;
    }

    public void setRequested(long requested) {
        this.requested = requested;
    }

    @Override
    public String toString() {
        return "LimitOfRequests{" +
            "id=" + id +
            ", countryCode='" + countryCode + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", requestsLimit=" + requestsLimit +
            ", requested=" + requested +
            '}';
    }
}
