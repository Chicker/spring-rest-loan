package ru.chicker.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.chicker.configs.PersistenceConfig;
import ru.chicker.configs.ServiceConfig;
import ru.chicker.configs.TestAppConfig;
import ru.chicker.configs.TestDataSourceConfig;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
    TestAppConfig.class,
    TestDataSourceConfig.class,
    PersistenceConfig.class,
    ServiceConfig.class
})
@WebAppConfiguration
public class LoansServiceTest {

    @Autowired
    private ILoansService loansService;

    @Test
    public void when_personal_id_is_in_blacklist_should_return_true() throws Exception {
        assertThat(loansService.personalIdIsInBlackList("blacklist"), is(true));
    }

    @Test
    public void when_personal_id_is_not_in_blacklist_should_return_false() throws Exception {
        assertThat(loansService.personalIdIsInBlackList("7777"), is(false));
    }
}
