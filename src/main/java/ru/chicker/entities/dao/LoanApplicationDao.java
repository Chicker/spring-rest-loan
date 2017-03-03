package ru.chicker.entities.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.chicker.models.dto.ApplicationLoanDto;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class LoanApplicationDao {
    private Long id;
    private String name;
    private String surName;
    private String personalId;
    private BigDecimal amount;
    private int term;
    private String countryCode;
    private LocalDateTime created;
    private DecisionOnLoanApplicationDao decision;

    public LoanApplicationDao(Long id, String name, String surName, String personalId, BigDecimal amount,
                              int term, String countryCode, LocalDateTime created) {
        this.id = id;
        this.name = name;
        this.surName = surName;
        this.personalId = personalId;
        this.amount = amount;
        this.term = term;
        this.countryCode = countryCode;
        this.created = created;
    }

    public LoanApplicationDao(ApplicationLoanDto dto, String countryCode) {
        this.name = dto.getName();
        this.surName = dto.getSurName();
        this.personalId = dto.getPersonalId();
        this.amount = dto.getAmount();
        this.term = dto.getTerm();
        this.countryCode = countryCode;
        this.created = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurName() {
        return surName;
    }

    public String getPersonalId() {
        return personalId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getTerm() {
        return term;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public DecisionOnLoanApplicationDao getDecision() {
        return decision;
    }

    public void setDecision(DecisionOnLoanApplicationDao decision) {
        this.decision = decision;
    }

    public static class MyRowMapper implements RowMapper<LoanApplicationDao> {
        @Override
        public LoanApplicationDao mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new LoanApplicationDao(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("surname"),
                rs.getString("personal_id"),
                rs.getBigDecimal("amount"),
                rs.getInt("term"),
                rs.getString("country_code"),
                rs.getTimestamp("created").toLocalDateTime());
        }
    }

    public static class MyRowMapperWithDecision implements RowMapper<LoanApplicationDao> {
        @Override
        public LoanApplicationDao mapRow(ResultSet rs, int rowNum) throws SQLException {
            DecisionOnLoanApplicationDao decision =
                new DecisionOnLoanApplicationDao(
                    rs.getLong("decision_id"),
                    rs.getTimestamp("decision_date").toLocalDateTime(),
                    rs.getInt("approved"),
                    rs.getLong("fk_loan_application"));
            
            LoanApplicationDao loanApplicationDao = new LoanApplicationDao(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("surname"),
                rs.getString("personal_id"),
                rs.getBigDecimal("amount"),
                rs.getInt("term"),
                rs.getString("country_code"),
                rs.getTimestamp("created").toLocalDateTime());
            
            loanApplicationDao.setDecision(decision);
            
            return loanApplicationDao;
        }
    }
}
