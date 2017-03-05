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
import ru.chicker.entities.dao.LoanApplicationDao;
import ru.chicker.exceptions.LoanApplicationHasBeenResolvedException;
import ru.chicker.repositories.DecisionOnLoanApplicationRepositoryDao;
import ru.chicker.repositories.LoanApplicationRepositoryDao;
import ru.chicker.services.LoansService;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
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
    private LoanApplicationRepositoryDao loansRepo;

    @Autowired
    private DecisionOnLoanApplicationRepositoryDao decisionsRepo;

    @Autowired
    private LoansService loansService;
    
    @Autowired
    private LoanApplicationRepositoryDao loansRepoNew;
    
    @Test
    public void test1() throws Exception {
        List<LoanApplicationDao> loans = loansRepoNew.findByPersonalId("12345678sq");
        loans.size();
    }

    private LoanApplicationDao approveMarrysLoan() throws LoanApplicationHasBeenResolvedException {
        List<LoanApplicationDao> loans = loansRepo.findByPersonalId("12345678sq");

        // Marry Poppins has only one loan application.
        assertThat(loans.size(), is(1));

        Long loanId = loans.get(0).getId();
        LoanApplicationDao marryLoan = loans.get(0);

        loansService.resolveLoanApplication(marryLoan.getId(), true);
        return marryLoan;
    }
}
