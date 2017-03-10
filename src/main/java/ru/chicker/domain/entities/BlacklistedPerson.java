package ru.chicker.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "BlacklistedPersons")
public class BlacklistedPerson {
    @Id
    @Column(name = "personal_id")
    private String personalId;

    protected BlacklistedPerson() {
    }

    public BlacklistedPerson(String personalId) {
        this.personalId = personalId;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }
}
