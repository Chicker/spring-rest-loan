package ru.chicker.services;

import javaslang.control.Try;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.chicker.configs.PersistenceConfig;
import ru.chicker.configs.ServiceConfig;
import ru.chicker.configs.TestAppConfig;
import ru.chicker.configs.TestDataSourceConfig;
import ru.chicker.services.internal.InfoByIpFreeGeoIpProvider;
import ru.chicker.services.internal.InfoByIpIpApiProvider;

import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class InfoByIpServiceIntegrationTest {

    @Configuration
    @Import({TestDataSourceConfig.class, PersistenceConfig.class, ServiceConfig.class})
    static class TestConfig {
        @Bean
        public InfoByIpIpApiProvider getInfoByIpIpApiProvider() {
            return mock(InfoByIpIpApiProvider.class);
        }

        @Bean
        public InfoByIpFreeGeoIpProvider getInfoByIpFreeGeoIpProvider() {
            return mock(InfoByIpFreeGeoIpProvider.class);
        }
    }

    @Autowired
    private InfoByIpService infoByIpService;

    @Autowired
    private InfoByIpFreeGeoIpProvider freeGeoIpProvider;

    @Autowired
    private InfoByIpIpApiProvider infoByIpIpApiProvider;

    @Before
    public void setUp() throws Exception {
        Mockito.reset(freeGeoIpProvider);
        Mockito.reset(infoByIpIpApiProvider);
    }

    @Test
    public void should_return_result_from_any_of_services() throws Exception {
        when(freeGeoIpProvider.getCountryCode(anyString())).thenReturn(Try.success("ru"));
        when(freeGeoIpProvider.getCountryCode(anyString())).thenReturn(Try.success("ru"));

        assertThat(infoByIpService.getCountryCode(Optional.of("any IP")), is("ru"));
    }

    @Test
    public void when_the_first_service_is_not_accessible_should_return_result_from_other_service()
    throws Exception {
        String testApi = "83.12.21.0";
        
        when(freeGeoIpProvider.getCountryCode(anyString())).thenReturn(Try.failure(new IOException()));
        when(infoByIpIpApiProvider.getCountryCode(anyString())).thenReturn(Try.success("ru"));

        assertThat(infoByIpService.getCountryCode(Optional.of(testApi)), is("ru"));

        verify(freeGeoIpProvider, times(1)).getCountryCode(testApi);
        verify(infoByIpIpApiProvider, times(1)).getCountryCode(testApi);
    }

    @Test
    public void when_all_services_are_not_accessible_should_return_fallback_country_code()
    throws Exception {
        String testApi = "83.12.21.0";
        
        when(freeGeoIpProvider.getCountryCode(anyString()))
            .thenReturn(Try.failure(new IOException()));
        when(infoByIpIpApiProvider.getCountryCode(anyString()))
            .thenReturn(Try.failure(new IOException()));

        assertThat(infoByIpService.getCountryCode(Optional.of(testApi)), is("lv"));
        
        verify(freeGeoIpProvider, times(1)).getCountryCode(testApi);
        verify(infoByIpIpApiProvider, times(1)).getCountryCode(testApi);
    }

    @Test
    public void when_ip_address_is_not_present_should_return_fallback_country_code()
    throws Exception {
        assertThat(infoByIpService.getCountryCode(Optional.empty()), is("lv"));
        
        verifyZeroInteractions(freeGeoIpProvider);
        verifyZeroInteractions(infoByIpIpApiProvider);
    }
}
