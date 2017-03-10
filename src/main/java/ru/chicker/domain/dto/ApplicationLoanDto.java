package ru.chicker.domain.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class ApplicationLoanDto {
    @Min(1)
    private BigDecimal amount;

    @Min(1)
    private int term;

    @NotEmpty
    private String name;

    @NotEmpty
    private String surName;

    @NotEmpty
    private String personalId;

    public ApplicationLoanDto() {
    }

    public ApplicationLoanDto(BigDecimal amount, int term, String name, String surName,
                              String personalId) {
        this.amount = amount;
        this.term = term;
        this.name = name;
        this.surName = surName;
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

    @Override
    public String toString() {
        return "ApplicationLoanDto{" +
            "amount=" + amount +
            ", term=" + term +
            ", name='" + name + '\'' +
            ", surName='" + surName + '\'' +
            ", personalId='" + personalId + '\'' +
            '}';
    }
}
