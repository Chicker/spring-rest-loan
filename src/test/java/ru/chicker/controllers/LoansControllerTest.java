package ru.chicker.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.chicker.configs.TestAppConfig;
import ru.chicker.configs.TestServiceConfig;
import ru.chicker.services.ILoansService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfig.class, TestServiceConfig.class})
@WebAppConfiguration
public class LoansControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private static MockMvc mockMvc;

    @Autowired
    private ILoansService mockedILoansService;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .build();

        Mockito.reset(mockedILoansService);
    }

    @Test
    public void when_param_is_not_correct_it_should_return_bad_request()
    throws Exception {
        mockMvc.perform(
            post("/loans/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("amount", "1234.80")
                .param("term", "-2") // wrong param "term" should be greater than 1
                .param("name", "Sebastian")
                .param("surName", "Rodriges")
                .param("personal_id", "1234bcd578"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void when_got_wrong_name_of_param_it_should_return_bad_request_status()
    throws Exception {
        mockMvc.perform(
            post("/loans/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("amount", "1234.80")
                .param("term", "365")
                .param("name", "Sebastian")
                .param("surName", "Rodriges")
                .param("personal_id", "1234bcd578")) // wrong name, it must be named as "personalId"
            .andExpect(status().isBadRequest());
    }

    @Test
    public void when_request_is_correct_it_should_return_created_status() throws Exception {
        String body = "amount=1234.80&term=365&name=Sebastian&surName=Rodriges" +
            "&personalId=1234bcd578";

        mockMvc.perform(
            post("/loans/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content(body))
            .andExpect(status().isCreated());
    }

    @Test
    public void when_person_in_black_list_should_return_precondition_failed_status()
    throws Exception {
        when(mockedILoansService.personalIdIsInBlackList(anyString())).thenReturn(true);
        
        mockMvc.perform(
            post("/loans/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("amount", "1234.80")
                .param("term", "2")
                .param("name", "Sebastian")
                .param("surName", "Rodriges")
                .param("personalId", "blacklist"))  // person is in a black list
            .andExpect(status().isPreconditionFailed());

        verify(mockedILoansService, times(1)).personalIdIsInBlackList(anyString());
    }
}
