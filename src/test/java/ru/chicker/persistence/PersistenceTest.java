package ru.chicker.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.chicker.configs.PersistenceConfig;
import ru.chicker.configs.ServiceConfig;
import ru.chicker.configs.TestAppConfig;
import ru.chicker.configs.TestDataSourceConfig;
import ru.chicker.entities.DecisionOnLoanApplication;
import ru.chicker.entities.LoanApplication;
import ru.chicker.entities.Person;
import ru.chicker.exceptions.LoanApplicationHasBeenResolvedException;
import ru.chicker.repositories.DecisionOnLoanApplicationRepository;
import ru.chicker.repositories.LoanApplicationRepository;
import ru.chicker.repositories.PersonRepository;
import ru.chicker.services.LoansService;
import ru.chicker.utils.CollectionUtils;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
    TestAppConfig.class,
    PersistenceConfig.class,
    ServiceConfig.class,
    TestDataSourceConfig.class})
@WebAppConfiguration
@Transactional
@Rollback
public class PersistenceTest {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    private LoanApplicationRepository loansRepo;

    @Autowired
    private DecisionOnLoanApplicationRepository decisionsRepo;

    @Autowired
    private LoansService loansService;

    //    @Test
    public void testSave() throws Exception {
        Person person1 = new Person("Katy", "Jhonson");
        Person person2 = new Person("Elena", "Markova");

        personRepository.save(person1);
        personRepository.save(person2);

        // The size should be 4, because the table "Persons" already contains 2 
        // persons initially
        assertThat(CollectionUtils.sizeOfIterable(personRepository.findAll())
            , is(4));
    }

    //    @Test
    public void when_loan_app_is_deleted_should_decision_will_be_deleted_also() throws Exception {
        List<LoanApplication> loans = loansRepo.findByPersonalId("12345678sq");

        // Marry Poppins has only one loan application.
        assertThat(loans.size(), is(1));

        Long loanId = loans.get(0).getId();
        LoanApplication marryLoan = loans.get(0);

        loansService.resolveLoanApplication(marryLoan, true);

        List<DecisionOnLoanApplication> decisions = getApprovedDecisionsByLoanId(loanId);

        // Once loan has been approved above, Mary Poppins has only one approval decision
        assertThat(decisions.size(), is(1));

        loansRepo.delete(marryLoan);
        loansRepo.flush();

        List<DecisionOnLoanApplication> decision2 = getApprovedDecisionsByLoanId(loanId);

        assertThat(decision2.size(), is(0));
    }

    private List<DecisionOnLoanApplication> getApprovedDecisionsByLoanId(Long loanId) {
        List<DecisionOnLoanApplication> approved = decisionsRepo.findByApproved(1);

        return approved
            .stream()
            .filter(dec -> dec.getLoanApplication().getId().equals(loanId))
            .collect(Collectors.toList());
    }

    @Test
    public void with_refresh() throws Exception {
        LoanApplication marryLoan = approveMarrysLoan();

        EntityManager entityManager = loansRepo.getEntityManager();

        entityManager.refresh(marryLoan);

        List<LoanApplication> my = loansRepo.findByPersonalId("12345678sq");

        assertThat(my.get(0).getDecision(), is(notNullValue()));
    }

    private LoanApplication approveMarrysLoan() throws LoanApplicationHasBeenResolvedException {
        List<LoanApplication> loans = loansRepo.findByPersonalId("12345678sq");

        // Marry Poppins has only one loan application.
        assertThat(loans.size(), is(1));

        Long loanId = loans.get(0).getId();
        LoanApplication marryLoan = loans.get(0);

        loansService.resolveLoanApplication(marryLoan, true);
        return marryLoan;
    }
}
