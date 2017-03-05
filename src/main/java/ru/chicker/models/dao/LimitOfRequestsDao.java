package ru.chicker.models.dao;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class LimitOfRequestsDao {
    private Long id;
    private String countryCode;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long limit;
    private Long requested;

    public LimitOfRequestsDao(Long id, String countryCode, LocalDateTime startDate,
                              LocalDateTime endDate, Long limit, Long requested) {
        this.id = id;
        this.countryCode = countryCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.limit = limit;
        this.requested = requested;
    }

    public Long getId() {
        return id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Long getLimit() {
        return limit;
    }

    public Long getRequested() {
        return requested;
    }

    public void setRequested(Long requested) {
        this.requested = requested;
    }

    @Override
    public String toString() {
        return "LimitOfRequestsDao{" +
            "id=" + id +
            ", countryCode='" + countryCode + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", limit=" + limit +
            ", requested=" + requested +
            '}';
    }
    
    public static class MyRowMapper implements RowMapper<LimitOfRequestsDao> {
        @Override
        public LimitOfRequestsDao mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new LimitOfRequestsDao(
                rs.getLong("id"),
                rs.getString("country_code"),
                rs.getTimestamp("date_start").toLocalDateTime(),
                rs.getTimestamp("date_end").toLocalDateTime(),
                rs.getLong("requests_limit"),
                rs.getLong("requested")
            );
        }
    }
}
