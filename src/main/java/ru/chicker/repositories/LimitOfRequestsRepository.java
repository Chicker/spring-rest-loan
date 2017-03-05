package ru.chicker.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.chicker.models.dao.LimitOfRequestsDao;
import ru.chicker.utils.JdbcTemplateUtils;

import javax.sql.DataSource;
import java.time.LocalDateTime;

@Repository
public class LimitOfRequestsRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public LimitOfRequestsDao findByCountryCodeAndBetweenDates(String countryCode,
                                                            LocalDateTime startDate,
                                                            LocalDateTime endDate) {
        String sql = "SELECT * " +
            "FROM LimitsCountryRequests as lim " +
            "WHERE lim.country_code = ? " +
            "      and lim.date_start <= ? " +
            "      and ? <= lim.date_end";

        return JdbcTemplateUtils.queryForObjectOrNull(jdbcTemplate, sql,
            new Object[] {countryCode, startDate, endDate}, new LimitOfRequestsDao.MyRowMapper());
    }

    public void update(LimitOfRequestsDao limitOfRequests) {
        String sql = "UPDATE LimitsCountryRequests SET country_code = ?, date_start = ?, " +
                                                      "date_end = ?, requests_limit = ?, " +
                                                      "requested = ? " +
                     "WHERE id = ?";

        int rowsAffected = jdbcTemplate.update(sql, new Object[]{
            limitOfRequests.getCountryCode(),
            limitOfRequests.getStartDate(), limitOfRequests.getEndDate(),
            limitOfRequests.getLimit(), limitOfRequests.getRequested(),
            limitOfRequests.getId()
        });

        assert rowsAffected == 1;
    }
}
