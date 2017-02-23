CREATE TABLE Persons (
  id         INTEGER      NOT NULL IDENTITY ,
  first_name VARCHAR(255) NOT NULL,
  last_name  VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE BlacklistedPersons (
  personal_id varchar(255) NOT NULL, 
  PRIMARY KEY (personal_id)
);

CREATE TABLE LoanApplications (
  id           varchar(255) NOT NULL IDENTITY,
  name         varchar(255) NOT NULL,
  surname      varchar(255) NOT NULL,
  personal_id  varchar(255) NOT NULL,
  amount       numeric(19, 0) NOT NULL,
  term         int NOT NULL,
  country_code varchar(255) NOT NULL,
  PRIMARY KEY (id));
