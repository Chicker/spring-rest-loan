package ru.chicker.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.chicker.configs.PersistenceConfig;
import ru.chicker.configs.ServiceConfig;
import ru.chicker.configs.TestAppConfig;
import ru.chicker.configs.TestDataSourceConfig;
import ru.chicker.entities.DecisionOnLoanApplication;
import ru.chicker.entities.LimitOfRequests;
import ru.chicker.entities.LoanApplication;
import ru.chicker.entities.dao.DecisionOnLoanApplicationDao;
import ru.chicker.entities.dao.LoanApplicationDao;
import ru.chicker.exceptions.LoanApplicationHasBeenResolvedException;
import ru.chicker.repositories.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
    TestAppConfig.class,
    TestDataSourceConfig.class,
    PersistenceConfig.class,
    ServiceConfig.class
})
@WebAppConfiguration
@Transactional
@Rollback
public class LoansServiceImplTest {

    @Autowired
    private LoansService loansService;

    @Autowired
    private LimitOfRequestsRepository limitOfRequestsRepository;

    @Autowired
    private LoanApplicationRepositoryDao loanApplicationRepository;

    @Autowired
    private DecisionOnLoanApplicationRepositoryDao decisionsRepository;

    private final static LocalDateTime on23Feb = LocalDateTime.of(2017, 02, 23, 0, 0);

    @Test
    public void when_personal_id_is_in_blacklist_should_return_true() throws Exception {
        assertThat(loansService.personalIdIsInBlackList("blacklist"), is(true));
    }

    @Test
    public void when_personal_id_is_not_in_blacklist_should_return_false() throws Exception {
        assertThat(loansService.personalIdIsInBlackList("7777"), is(false));
    }

    @Test
    public void when_limit_is_not_exceeded_should_return_false() throws Exception {
        assertThat(loansService.checkLimitAndIncrement("ru"), is(false));
    }

    @Test
    public void when_limit_is_exceeded_should_return_true() throws Exception {
        assertThat(loansService.checkLimitAndIncrement("pl"), is(true));
    }

    @Test
    public void when_limit_is_not_specified_for_given_country_code_should_return_false()
    throws Exception {
        assertThat(loansService.checkLimitAndIncrement("us"), is(false));
    }

    @Test
    public void when_new_request_comes_in_should_increment_requested() throws Exception {
        String countryCode = "ru";

        LimitOfRequests limitOfRequests = loansService.getLimitOfRequestsOnDate(countryCode,
            on23Feb);
        long currentRequested = limitOfRequests.getRequested();

        loansService.checkLimitAndIncrement(countryCode);

        LimitOfRequests limitOfRequests2 = loansService.getLimitOfRequestsOnDate(countryCode,
            on23Feb);
        long newRequested = limitOfRequests2.getRequested();

        assertThat(newRequested - currentRequested, is(1L));
    }

    @Test
    public void when_limit_is_exceeded_but_period_of_time_is_over_should_return_false()
    throws Exception {
        String countryCode = "uk";

        long oldRequested = loansService.getLimitOfRequestsOnDate("uk", on23Feb).getRequested();

        assertThat(loansService.checkLimitAndIncrement(countryCode), is(false));

        // no database changes should be
        long newRequested = loansService.getLimitOfRequestsOnDate("uk", on23Feb).getRequested();

        assertThat(oldRequested == newRequested, is(true));

    }

    @Test
    public void test_accept_loan_application() throws Exception {
        String personalId = "123456";
        List<LoanApplicationDao> loanApplicationList = loanApplicationRepository.findByPersonalId(personalId);

        // Peter Rodriges (personal id is equal 123456) created 2 applications.
        assertThat(loanApplicationList.size(), is(2));

        LoanApplicationDao loanApplication =
            loanApplicationRepository.findFirst1ByPersonalIdOrderByIdDesc(personalId);

        loansService.resolveLoanApplication(loanApplication.getId(), true);

        DecisionOnLoanApplicationDao approvedLoan = decisionsRepository.findByLoanApplication(loanApplication.getId());

        assertThat(approvedLoan, is(notNullValue()));
        assertThat(approvedLoan.getApproved(), is(true));
    }

    @Test(expected = LoanApplicationHasBeenResolvedException.class)
    public void when_try_to_resolve_loan_app_again_should_throw_error() throws Exception {
        String personalId = "123456";

        LoanApplicationDao loanApplication =
            loanApplicationRepository.findFirst1ByPersonalIdOrderByIdDesc(personalId);

        loansService.resolveLoanApplication(loanApplication.getId(), true);

        DecisionOnLoanApplicationDao approvedLoan = decisionsRepository.findByLoanApplication(loanApplication.getId());

        assertThat(approvedLoan, is(notNullValue()));

        // if try to resolve same loan application again then an exception should be thrown
        loansService.resolveLoanApplication(loanApplication.getId(), false);
    }

    @Test
    public void test_list_all_approved_loans() throws Exception {
        String client1Id = "123456";
        String client2Id = "12345678sq";
        List<LoanApplicationDao> loansClient1 = loanApplicationRepository.findByPersonalId(client1Id);
        List<LoanApplicationDao> loansClient2 = loanApplicationRepository.findByPersonalId(client2Id);

        // check the test data
        assertThat(loansClient1.size(), is(2));
        assertThat(loansClient2.size(), is(1));

        // prepare test data
        // N\ow we will approve 3 loans and after this we will test them 
        for (LoanApplicationDao loan : loansClient1) {
            loansService.resolveLoanApplication(loan.getId(),true);
        }

        for (LoanApplicationDao loan : loansClient2) {
            loansService.resolveLoanApplication(loan.getId(),true);
        }
        
        List<LoanApplicationDao> approvedLoans = loansService.getLoansByApproved(true);

        assertThat(approvedLoans.size(), is(3));
    }
}
