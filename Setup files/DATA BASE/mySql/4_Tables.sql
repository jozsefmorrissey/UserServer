
-- ==========================  TABLES  =====================--

CREATE TABLE UUSER_ABS (
  ID BIGINT(16) PRIMARY KEY AUTO_INCREMENT,
  FULLNAME VARCHAR(64),
  EMAIL VARCHAR(128) UNIQUE,
  TOKEN VARCHAR(64),
  PASSWORD VARCHAR(128) DEFAULT '$2a$10$3NTPya6TN.11/VLxBhJjBekgWOmV.Y.OvvQOjy9y07hiNEKjQPp5S',
  PERMISSION_ID BIGINT(16) DEFAULT 1,
  DTYPE VARCHAR(8) DEFAULT 'UUser'
);

CREATE TABLE PERMISSION (
  ID BIGINT(16) PRIMARY KEY AUTO_INCREMENT,
  USER_ID BIGINT(16),
  APP_USER_ID BIGINT(16),
  REF_TYPE VARCHAR(32),
  REF_ID BIGINT(16),
  TYPE VARCHAR(32),
  ORIGIN_USER_ID BIGINT(16),
  GRANTED_FROM_USER_ID BIGINT(16),
  CONSTRAINT FK_PERMISSION_USER FOREIGN KEY (USER_ID) REFERENCES UUSER_ABS(ID),
  CONSTRAINT FK_PERMISSION_ORIGIN FOREIGN KEY (ORIGIN_USER_ID) REFERENCES UUSER_ABS(ID),
  CONSTRAINT FK_PERMISSION_GRANTED FOREIGN KEY (GRANTED_FROM_USER_ID) REFERENCES UUSER_ABS(ID)
);

CREATE TABLE USER_PHOTO (
  ID BIGINT(16) PRIMARY KEY AUTO_INCREMENT,
  USER_ID BIGINT(16),
  APP_USER_ID BIGINT(16),
  POSITION TINYINT,
  URL VARCHAR(256),
  CONSTRAINT FK_USER_PHOTO FOREIGN KEY (USER_ID) REFERENCES UUSER_ABS(ID)
);


CREATE TABLE REMARK(
  ID BIGINT(16) PRIMARY KEY,
  POSTER_ID BIGINT(16),
  CONVERSATION_ID BIGINT(16),
  CONTENT LONGTEXT,
  TIME_STAMP DATETIME(6),
  CONSTRAINT FK_POSTER_REMARK FOREIGN KEY (POSTER_ID) REFERENCES UUSER_ABS(ID)
);

CREATE TABLE HAS_READ(
  REMARK_ID BIGINT(16),
  USER_ID BIGINT(16),
  CONSTRAINT FK_REMARK_HAS_READ FOREIGN KEY (REMARK_ID) REFERENCES REMARK(ID),
  CONSTRAINT FK_USER_HAS_READ FOREIGN KEY (USER_ID) REFERENCES UUSER_ABS(ID),
  CONSTRAINT COMP_PK_HAS_READ PRIMARY KEY (REMARK_ID, USER_ID)
);

CREATE TABLE COLLAPSE_USER(
  FOR_USER_ID BIGINT(16),
  TARGET_USER_ID BIGINT(16),
  CONSTRAINT FK_FOR_USER FOREIGN KEY (FOR_USER_ID) REFERENCES UUSER_ABS(ID),
  CONSTRAINT FK_TARGET_USER FOREIGN KEY (TARGET_USER_ID) REFERENCES UUSER_ABS(ID),
  CONSTRAINT COMP_PK_COLLAPSE_USER PRIMARY KEY (FOR_USER_ID, TARGET_USER_ID)
);
