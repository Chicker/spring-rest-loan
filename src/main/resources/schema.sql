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
