package ru.chicker.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import ru.chicker.configs.ServiceConfig;
import ru.chicker.configs.TestAppConfig;
import ru.chicker.entities.DecisionOnLoanApplication;
import ru.chicker.entities.LoanApplication;
import ru.chicker.exceptions.LoanApplicationHasBeenResolvedException;
import ru.chicker.repositories.DecisionOnLoanApplicationRepository;
import ru.chicker.repositories.LoanApplicationRepository;
import ru.chicker.services.LoansService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfig.class, ServiceConfig.class})
@WebAppConfiguration
@Transactional
@Rollback
public class LoansControllerIntegrationTest {
    public static final String PERSONAL_ID_MARRY_POPPINS = "12345678sq";
    @Autowired
    private WebApplicationContext webApplicationContext;

    private static MockMvc mockMvc;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private DecisionOnLoanApplicationRepository decisionsRepo;

    @Autowired
    private LoansService loansService;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .build();

        createTestDataForApproveTests();
    }

    private void createTestDataForApproveTests() throws LoanApplicationHasBeenResolvedException {
        String nikelson = "999qsq";
        List<LoanApplication> maryLoans = loanApplicationRepository.findByPersonalId(PERSONAL_ID_MARRY_POPPINS);
        List<LoanApplication> nikelsonLoans = loanApplicationRepository.findByPersonalId(nikelson);

        // approve one loan created by Marry Poppins
        assertThat(maryLoans.size(), is(1));
        for (LoanApplication loan : maryLoans) {
            loansService.resolveLoanApplication(loan, true);
            loanApplicationRepository.getEntityManager().refresh(loan);
        }

        // decline all loans created by Robert Nikelson
        assertThat(nikelsonLoans.size(), is(2));
        for (LoanApplication loan : nikelsonLoans) {
            loansService.resolveLoanApplication(loan, false);
            loanApplicationRepository.getEntityManager().refresh(loan);
        }
    }

    @Test
    public void test_approve_loan_application() throws Exception {
        String personalId = "123456";

        // the last application that created by Peter Rodriges
        LoanApplication loanApplication =
            loanApplicationRepository.findFirst1ByPersonalIdOrderByIdDesc(personalId);

        mockMvc.perform(
            post(String.format("/loans/%s/approve", loanApplication.getId()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("approve", "true"))
            .andExpect(status().isOk());

        DecisionOnLoanApplication decision = decisionsRepo.findByLoanApplication(loanApplication);

        assertThat(decision, is(notNullValue()));
        assertThat(decision.getApproved(), is(1));
        assertThat(decision.getDecisionDate().toLocalDate(), is(LocalDateTime.now().toLocalDate()));
    }

    @Test
    public void when_loan_application_id_is_not_exist_should_return_error() throws Exception {
        mockMvc.perform(
            post(String.format("/loans/%s/approve", -5))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("approve", "true"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$['error']").value(containsString("is not exist!")));
    }

    @Test
    public void test_request_for_approved_loans() throws Exception {
        mockMvc.perform(
            get("/loans/approved/")
                .accept(MediaType.APPLICATION_JSON_UTF8))
//            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.error").doesNotExist())
            .andExpect(jsonPath("$.result.length()").value(1))
            .andExpect(jsonPath("$.result.[0].personalId").value("12345678sq"));
    }

    @Test
    public void test_request_for_approved_loans_by_client() throws Exception {
        mockMvc.perform(
            get(String.format("/clients/%s/loans/approved/", PERSONAL_ID_MARRY_POPPINS))
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.error").doesNotExist())
            .andExpect(jsonPath("$.result.length()").value(1))
            .andExpect(jsonPath("$.result.[0].personalId").value("12345678sq"));

    }
}
