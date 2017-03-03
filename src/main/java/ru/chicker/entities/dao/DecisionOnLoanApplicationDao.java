package ru.chicker.entities.dao;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class DecisionOnLoanApplicationDao {
    private Long id;
    private LocalDateTime decisionDate;
    private int approved;
    private Long fkLoanApplication;

    public DecisionOnLoanApplicationDao(Long id, LocalDateTime decisionDate, int approved,
                                        Long fkLoanApplication) {
        this.id = id;
        this.decisionDate = decisionDate;
        this.approved = approved;
        this.fkLoanApplication = fkLoanApplication;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDecisionDate() {
        return decisionDate;
    }

    public boolean getApproved() {
        return approved != 0;
    }

    public Long getFkLoanApplication() {
        return fkLoanApplication;
    }

    public static class MyRowMapper implements RowMapper<DecisionOnLoanApplicationDao> {
        @Override
        public DecisionOnLoanApplicationDao mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DecisionOnLoanApplicationDao(
                rs.getLong("id"),
                rs.getTimestamp("decision_date").toLocalDateTime(),
                rs.getInt("approved"),
                rs.getLong("fk_loan_application")
            );
        }
    }
}
