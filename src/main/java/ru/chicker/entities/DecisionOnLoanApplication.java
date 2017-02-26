package ru.chicker.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DecisionsOnLoanApplication")
public class DecisionOnLoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "decision_date")
    private LocalDateTime decisionDate;

    private int approved;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_loan_application")
    private LoanApplication loanApplication;

    protected DecisionOnLoanApplication() {
    }

    public DecisionOnLoanApplication(LocalDateTime decisionDate, int approved,
                                     LoanApplication loanApplication) {
        this.decisionDate = decisionDate;
        this.approved = approved;
        this.loanApplication = loanApplication;
    }

    public LocalDateTime getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(LocalDateTime decisionDate) {
        this.decisionDate = decisionDate;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public LoanApplication getLoanApplication() {
        return loanApplication;
    }

    public void setLoanApplication(LoanApplication loanApplication) {
        this.loanApplication = loanApplication;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "DecisionOnLoanApplication{" +
            "id=" + id +
            ", decisionDate=" + decisionDate +
            ", approved=" + approved +
            ", loanApplication=" + loanApplication +
            '}';
    }
}
