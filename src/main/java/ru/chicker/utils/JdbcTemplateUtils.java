package ru.chicker.utils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public final class JdbcTemplateUtils {
    private JdbcTemplateUtils() { }

    public static <T> T queryForObjectOrNull(JdbcTemplate jdbcTemplate, String sql, Object[] args,
                                             RowMapper<T> rowMapper) {
        List<T> result = jdbcTemplate.query(sql, args, rowMapper);

        if (result.isEmpty()) return null;
        else return result.get(0);
    }
}
