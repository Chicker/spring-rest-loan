package ru.chicker.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.chicker.models.dao.DecisionOnLoanApplicationDao;
import ru.chicker.utils.JdbcTemplateUtils;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DecisionOnLoanApplicationRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public DecisionOnLoanApplicationDao findByLoanApplication(Long loanApplicationId) {
        String sql = "SELECT d.id, d.decision_date, d.approved, d.fk_loan_application " +
            "FROM DecisionsOnLoanApplication as d " +
            "WHERE d.fk_loan_application = ?";

        return JdbcTemplateUtils.queryForObjectOrNull(jdbcTemplate, sql, new Object[] {loanApplicationId},
            new DecisionOnLoanApplicationDao.MyRowMapper());
    }
    
    public List<DecisionOnLoanApplicationDao> findByApproved(boolean approved) {
        String sql = "SELECT d.id, d.decision_date, d.approved, d.fk_loan_application " +
            "FROM DecisionsOnLoanApplication as d " +
            "WHERE d.approved = ?";
        
        List<DecisionOnLoanApplicationDao> result = jdbcTemplate.query(sql, new Object[]{approved ? 1 : 0},
            new DecisionOnLoanApplicationDao.MyRowMapper());
        
        return result;
    }

    public void addDecision(Long loanApplicationId, LocalDateTime decisionDate, boolean approve) {
        String sql = "INSERT INTO DecisionsOnLoanApplication (decision_date, approved, fk_loan_application) " +
            "VALUES (?, ?, ?)";

        int rowsAffected = jdbcTemplate.update(sql, new Object[]{decisionDate, approve ? 1 : 0, loanApplicationId});
        
        assert rowsAffected == 1;
    }
}
