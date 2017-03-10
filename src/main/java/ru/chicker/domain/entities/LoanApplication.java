package ru.chicker.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.chicker.domain.dto.ApplicationLoanDto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "LoanApplications")
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "surname")
    private String surName;

    @Column(name = "personal_id")
    private String personalId;

    private BigDecimal amount;

    private int term;

    @Column(name = "country_code")
    private String countryCode;

    private LocalDateTime created;

    @JsonIgnore
    @OneToOne(mappedBy = "loanApplication")
    private DecisionOnLoanApplication decision;

    protected LoanApplication() {
    }

    public LoanApplication(String name, String surName, String personalId, BigDecimal amount,
                           int term, String countryCode, LocalDateTime created) {
        this.name = name;
        this.surName = surName;
        this.personalId = personalId;
        this.amount = amount;
        this.term = term;
        this.countryCode = countryCode;
        this.created = created;
    }

    public LoanApplication(ApplicationLoanDto dto, String countryCode) {
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

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public DecisionOnLoanApplication getDecision() {
        return decision;
    }

    public void setDecision(DecisionOnLoanApplication decision) {
        this.decision = decision;
    }

    @Override
    public String toString() {
        return "LoanApplication{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", surName='" + surName + '\'' +
            ", personalId='" + personalId + '\'' +
            ", amount=" + amount +
            ", term=" + term +
            ", countryCode='" + countryCode + '\'' +
            ", created=" + created +
            '}';
    }
}
