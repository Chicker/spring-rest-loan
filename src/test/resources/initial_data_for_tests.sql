INSERT INTO LimitsCountryRequests (country_code, date_start, date_end,
                                   requests_limit, requested)
  VALUES ('pl',
          '2017-02-23', -- in the format: yyyy-MM-dd
          '2017-03-24',
          1500, 1500);

INSERT INTO LimitsCountryRequests (country_code, date_start, date_end,
                                   requests_limit, requested)
VALUES ('ru', '2017-02-23', '2017-03-24', 2000, 5);

INSERT INTO LimitsCountryRequests (country_code, date_start, date_end,
                                   requests_limit, requested)
VALUES ('uk', '2017-01-01', '2017-02-23', 2000, 2000);

INSERT INTO LoanApplications (name, surname, personal_id, amount, term,
  country_code, created)
  VALUES ('Peter', 'Rodriges', '123456', 1200, 365, 'uk', '2017-02-25');

INSERT INTO LoanApplications (name, surname, personal_id, amount, term,
                              country_code, created)
VALUES ('Peter', 'Rodriges', '123456', 1000, 120, 'uk', '2017-02-26');


INSERT INTO LoanApplications (name, surname, personal_id, amount, term,
                              country_code, created)
  VALUES ('Mary', 'Poppins', '12345678sq', 100, 28, 'uk', '2017-01-01');

INSERT INTO LoanApplications (name, surname, personal_id, amount, term,
                              country_code, created)
  VALUES ('Robert', 'Nikelson', '999qsq', 10000, 28, 'pl', '2017-02-26');

INSERT INTO LoanApplications (name, surname, personal_id, amount, term,
                              country_code, created)
  VALUES ('Robert', 'Nikelson', '999qsq', 10000, 28, 'pl', '2017-02-27');

-- INSERT INTO DecisionsOnLoanApplication (decision_date, approved, fk_loan_application)
--     VALUES ('2017-02-27', 1, 3)
