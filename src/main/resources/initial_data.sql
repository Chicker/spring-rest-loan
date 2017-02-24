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