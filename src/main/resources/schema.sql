USE aihun;
DROP TABLE IF EXISTS t_contact;

CREATE TABLE t_contact (
  open_id VARCHAR (100) NOT NULL PRIMARY KEY ,
  zh_name VARCHAR (20) NOT NULL ,
  telephone VARCHAR (20) NOT NULL UNIQUE ,
  address VARCHAR (255) NOT NULL
);

