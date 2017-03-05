package ru.chicker.models.dao;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BlacklistedPersonDao {
    private String personalId;

    public BlacklistedPersonDao(String personalId) {
        this.personalId = personalId;
    }

    public String getPersonalId() {
        return personalId;
    }

    @Override
    public String toString() {
        return "BlacklistedPersonDao{" +
            "personalId='" + personalId + '\'' +
            '}';
    }
    
    public static class MyRowMapper implements RowMapper<BlacklistedPersonDao> {

        @Override
        public BlacklistedPersonDao mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BlacklistedPersonDao(rs.getString("personal_id"));
        }
    }
}
