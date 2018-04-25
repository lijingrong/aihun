DROP TABLE IF EXISTS t_contact;
DROP TABLE IF EXISTS t_game_team;
DROP TABLE IF EXISTS t_user_info;
DROP TABLE IF EXISTS t_wechat_jsapi_ticket;
DROP TABLE IF EXISTS t_wechat_oauth_code;

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
);

CREATE TABLE t_user_info (
  user_id VARCHAR(100) NOT NULL COMMENT '用户Id',
  open_id VARCHAR(100) NOT NULL COMMENT '微信openId',
  nick_name VARCHAR(255) NOT NULL COMMENT '昵称',
  gender TINYINT NOT NULL COMMENT '性别，0-女，1-男',
  country VARCHAR(100) NULL COMMENT '国家',
  province VARCHAR(255) NULL COMMENT '省份',
  city VARCHAR(255) NULL COMMENT '城市',
  avatar_url VARCHAR(255) NULL COMMENT '用户头像',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (user_id),
  UNIQUE KEY t_user_info_open_id (open_id) USING BTREE
);

CREATE TABLE t_wechat_jsapi_ticket (
  ticket_id VARCHAR(100) NOT NULL COMMENT '微信基本accessToken和jsApi 表 id',
  access_token VARCHAR(1024) DEFAULT '' COMMENT '微信基本凭证',
  jsapi_ticket VARCHAR(512) DEFAULT '' COMMENT '公众号用于调用微信JS接口的临时票据',
  expires_in INT(11) DEFAULT '0' COMMENT '凭证有效期',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (ticket_id)
);

CREATE TABLE t_wechat_oauth_code (
  code_id VARCHAR(100) NOT NULL COMMENT 'openId与code映射 表 Id',
  wx_code VARCHAR(128) NOT NULL COMMENT '获取网页授权access_token的票据',
  access_token VARCHAR(1024) DEFAULT '' COMMENT '网页授权access_token',
  union_id VARCHAR(100) DEFAULT '' COMMENT '微信用户的UnionId',
  open_id VARCHAR(100) DEFAULT '' COMMENT '微信用户的 openId',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (code_id),
  UNIQUE KEY t_wechat_oauth_code_code (wx_code) USING BTREE
);


