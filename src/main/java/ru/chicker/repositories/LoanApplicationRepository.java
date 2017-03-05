package ru.chicker.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.chicker.models.dao.LoanApplicationDao;
import ru.chicker.utils.JdbcTemplateUtils;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class LoanApplicationRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public LoanApplicationDao findFirst1ByPersonalIdOrderByIdDesc(String personalId) {
        String sql = "SELECT TOP(1) * " +
            "FROM LoanApplications as l " +
            "  WHERE l.personal_id = ? " +
            "ORDER BY l.ID DESC";
        
        return JdbcTemplateUtils.queryForObjectOrNull(jdbcTemplate, sql, new Object[] {personalId},
            new LoanApplicationDao.MyRowMapper());
    }
    
    public List<LoanApplicationDao> findByPersonalId(String personalId) {
        String sql = "select l.id, l.name, l.surname, l.personal_id, l.amount, l.term, l.country_code, l.created " +
            "from LoanApplications as l where l.personal_id = ?";
        
        List<LoanApplicationDao> result = jdbcTemplate.query(sql, new Object[] {personalId},
            new LoanApplicationDao.MyRowMapper());
        
        return result;
    }
    
    public List<LoanApplicationDao> findLoansByClientAndByApproved(String personalId, boolean approved) {
        String sql = "SELECT l.*, d.id as decision_id, d.decision_date, d.approved, d.fk_loan_application " +
            "FROM LoanApplications as l " +
            "LEFT JOIN DecisionsOnLoanApplication as d on d.fk_loan_application = l.id and d.approved = ? " +
            "WHERE l.personal_id = ? ";

        List<LoanApplicationDao> result = jdbcTemplate.query(sql, new Object[] {approved ? 1 : 0, personalId},
            new LoanApplicationDao.MyRowMapperWithDecision());

        return result;
    }

    public List<LoanApplicationDao> findLoansByApproved(boolean approved) {
        String sql = "SELECT l.*, d.id as decision_id, d.decision_date, d.approved, d.fk_loan_application " +
            "FROM DecisionsOnLoanApplication as d " +
            "INNER JOIN LoanApplications as l on l.id = d.fk_loan_application " +
            "WHERE d.approved = ?";
        
        List<LoanApplicationDao> result = jdbcTemplate.query(sql, new Object[] {approved ? 1 : 0},
            new LoanApplicationDao.MyRowMapperWithDecision());

        return result;
    }

    public LoanApplicationDao findById(Long loanApplicationId) {
        String sql = "SELECT l.id, l.name, l.surname, l.personal_id, l.amount, l.term, l.country_code, l.created " +
            "FROM LoanApplications as l " +
            "WHERE l.id = ?";

        return JdbcTemplateUtils.queryForObjectOrNull(jdbcTemplate, sql, new Object[] {loanApplicationId},
            new LoanApplicationDao.MyRowMapper());
    }

    public long count() {   
        String sql = "SELECT count(*) FROM LoanApplications";

        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        
        return count;
    }

    public void addLoanApplication(LoanApplicationDao loanApplication) {
        String sql = "INSERT INTO LoanApplications (name, surname, personal_id, amount, term, " +
            "                                       country_code, created) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

        int rowsAffected = jdbcTemplate.update(sql, new Object[]{
            loanApplication.getName(),
            loanApplication.getSurName(),
            loanApplication.getPersonalId(),
            loanApplication.getAmount(),
            loanApplication.getTerm(),
            loanApplication.getCountryCode(),
            loanApplication.getCreated()
        });

        assert rowsAffected == 1;
    }
}
