package ru.chicker.entities;

import ru.chicker.models.dto.ApplicationLoanDto;

import javax.persistence.*;
import java.math.BigDecimal;


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

    protected LoanApplication() {
    }

    public LoanApplication(String name, String surName, String personalId, BigDecimal amount,
                           int term, String countryCode) {
        this.name = name;
        this.surName = surName;
        this.personalId = personalId;
        this.amount = amount;
        this.term = term;
        this.countryCode = countryCode;
    }
    
    public LoanApplication(ApplicationLoanDto dto, String countryCode) {
        this.name = dto.getName();
        this.surName = dto.getSurName();
        this.personalId = dto.getPersonalId();
        this.amount = dto.getAmount();
        this.term = dto.getTerm();
        this.countryCode = countryCode;
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
            '}';
    }
}
