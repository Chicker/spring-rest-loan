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
import ru.chicker.repositories.DecisionOnLoanApplicationRepository;
import ru.chicker.repositories.LoanApplicationRepository;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfig.class, ServiceConfig.class})
@WebAppConfiguration
@Transactional
@Rollback
public class LoansControllerIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private static MockMvc mockMvc;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private DecisionOnLoanApplicationRepository decisionsRepo;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .build();
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
}
