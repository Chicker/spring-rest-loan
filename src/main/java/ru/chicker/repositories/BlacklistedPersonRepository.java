package ru.chicker.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.chicker.models.dao.BlacklistedPersonDao;
import ru.chicker.utils.JdbcTemplateUtils;

import javax.sql.DataSource;

@Repository
public class BlacklistedPersonRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public BlacklistedPersonDao findOne(String personalId) {
        String sql = "SELECT * FROM BlacklistedPersons WHERE personal_id = ?";

        return JdbcTemplateUtils.queryForObjectOrNull(jdbcTemplate, sql,
            new Object[] {personalId}, new BlacklistedPersonDao.MyRowMapper());
    }
}
