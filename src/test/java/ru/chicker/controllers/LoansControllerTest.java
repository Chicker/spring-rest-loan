package ru.chicker.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.chicker.configs.TestAppConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
@WebAppConfiguration
public class LoansControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .build();
    }

    @Test
    public void when_param_is_not_correct_it_should_return_bad_request()
    throws Exception {
        String wrongParamsInBody = "amount=1234.80&term=-2&name=Sebastian&surName=Rodriges" +
            "&personalId=12121"; // param "term" should be greater than 1

        mockMvc.perform(
            post("/loans/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content(wrongParamsInBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void when_got_wrong_name_of_param_it_should_return_bad_request() throws Exception {
        // wrong name of param "personal_id", it must be named as "personalId"
        String wrongParamsInBody = "amount=1234.80&term=365&name=Sebastian&surName=Rodriges" +
            "&personal_id=1234bcd578";

        mockMvc.perform(
            post("/loans/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content(wrongParamsInBody))
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
}
