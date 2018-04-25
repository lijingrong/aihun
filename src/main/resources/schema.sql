DROP TABLE IF EXISTS t_contact;
DROP TABLE IF EXISTS t_game_team;

CREATE TABLE t_contact (
  open_id   VARCHAR(100) NOT NULL PRIMARY KEY,
  zh_name   VARCHAR(20)  NOT NULL,
  telephone VARCHAR(20)  NOT NULL UNIQUE,
  address   VARCHAR(255) NOT NULL
);

CREATE TABLE t_game_team (
  id        VARCHAR(50) NOT NULL PRIMARY KEY,
  uid       VARCHAR(50) NOT NULL,
  follow_id VARCHAR(50),
  time      DATETIME DEFAULT now()
)