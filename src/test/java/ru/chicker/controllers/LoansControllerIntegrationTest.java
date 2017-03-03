package ru.chicker.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import ru.chicker.configs.PersistenceConfig;
import ru.chicker.configs.ServiceConfig;
import ru.chicker.configs.TestAppConfig;
import ru.chicker.configs.TestDataSourceConfig;
import ru.chicker.entities.dao.DecisionOnLoanApplicationDao;
import ru.chicker.entities.dao.LoanApplicationDao;
import ru.chicker.exceptions.LoanApplicationHasBeenResolvedException;
import ru.chicker.repositories.DecisionOnLoanApplicationRepositoryDao;
import ru.chicker.repositories.LoanApplicationRepositoryDao;
import ru.chicker.services.InfoByIpService;
import ru.chicker.services.LoansService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration
@Transactional
@Rollback
public class LoansControllerIntegrationTest {
    @Configuration
    @Import({TestAppConfig.class, TestDataSourceConfig.class, PersistenceConfig.class, ServiceConfig.class})
    static class TestConfig {
        @Bean
        InfoByIpService getInfoByIpService() {
            return mock(InfoByIpService.class);
        }
    }

    public static final String PERSONAL_ID_MARRY_POPPINS = "12345678sq";
    @Autowired
    private WebApplicationContext webApplicationContext;

    private static MockMvc mockMvc;

    @Autowired
    private LoanApplicationRepositoryDao loanApplicationRepository;

    @Autowired
    private DecisionOnLoanApplicationRepositoryDao decisionsRepo;

    @Autowired
    private LoansService loansService;

    @Autowired
    private InfoByIpService infoByIpService;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .build();

        createTestDataForApproveTests();
    }

    private void createTestDataForApproveTests() throws LoanApplicationHasBeenResolvedException {
        String nikelson = "999qsq";
        List<LoanApplicationDao> maryLoans = loanApplicationRepository.findByPersonalId(PERSONAL_ID_MARRY_POPPINS);
        List<LoanApplicationDao> nikelsonLoans = loanApplicationRepository.findByPersonalId(nikelson);

        // approve one loan created by Marry Poppins
        assertThat(maryLoans.size(), is(1));
        for (LoanApplicationDao loan : maryLoans) {
            loansService.resolveLoanApplication(loan.getId(), true);
        }

        // decline all loans created by Robert Nikelson
        assertThat(nikelsonLoans.size(), is(2));
        for (LoanApplicationDao loan : nikelsonLoans) {
            loansService.resolveLoanApplication(loan.getId(), false);
        }
    }

    @Test
    public void when_loan_application_is_correct_should_save_it_to_db() throws Exception {
        String testClientIp = "83.12.21.0";
        String testPersonalId = "1234bcd578";

        when(infoByIpService.getCountryCode(Optional.of(testClientIp))).thenReturn("ru");

        long countBefore = loanApplicationRepository.count();

        mockMvc.perform(
            post("/loans/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header("X-Forwarded-For", testClientIp)
                .param("amount", "1234.80")
                .param("term", "365")
                .param("name", "Sebastian")
                .param("surName", "Rodriges")
                .param("personalId", testPersonalId))
            .andExpect(status().isCreated());

        assertThat(loanApplicationRepository.count(), is(countBefore + 1));
    }

    @Test
    public void when_input_data_is_correct_should_correctly_determines_code_of_country()
    throws Exception {
        String testClientIp = "83.12.21.0";
        String testPersonalId = "1234bcd578";

        when(infoByIpService.getCountryCode(Optional.of(testClientIp))).thenReturn("ru");

        mockMvc.perform(
            post("/loans/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header("X-Forwarded-For", testClientIp)
                .param("amount", "1234.80")
                .param("term", "365")
                .param("name", "Sebastian")
                .param("surName", "Rodriges")
                .param("personalId", testPersonalId))
            .andExpect(status().isCreated());

        LoanApplicationDao loanApplication = loanApplicationRepository.findFirst1ByPersonalIdOrderByIdDesc(testPersonalId);
        assertThat(loanApplication != null, is(true));
        assertThat(loanApplication.getCountryCode(), is("ru"));
        assertThat(loanApplication.getCreated().toLocalDate(), is(LocalDateTime.now().toLocalDate()));
    }

    @Test
    public void test_approve_loan_application() throws Exception {
        String personalId = "123456";

        // the last application that created by Peter Rodriges
        LoanApplicationDao loanApplication =
            loanApplicationRepository.findFirst1ByPersonalIdOrderByIdDesc(personalId);

        mockMvc.perform(
            post(String.format("/loans/%s/approve", loanApplication.getId()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("approve", "true"))
            .andExpect(status().isOk());

        DecisionOnLoanApplicationDao decision = decisionsRepo.findByLoanApplication(loanApplication.getId());

        assertThat(decision, is(notNullValue()));
        assertThat(decision.getApproved(), is(true));
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
