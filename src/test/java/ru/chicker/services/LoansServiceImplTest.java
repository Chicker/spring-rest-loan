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
import ru.chicker.entities.LimitOfRequests;
import ru.chicker.repositories.LimitOfRequestsRepository;

import java.time.LocalDateTime;

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
@Transactional
@Rollback
public class LoansServiceImplTest {

    @Autowired
    private LoansService loansService;

    @Autowired
    private LimitOfRequestsRepository limitOfRequestsRepository;

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
}
