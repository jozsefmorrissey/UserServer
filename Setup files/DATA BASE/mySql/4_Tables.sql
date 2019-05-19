
-- ==========================  TABLES  =====================--

CREATE TABLE UUSER_ABS (
  ID BIGINT(20) PRIMARY KEY,
  FULL_NAME VARCHAR(64),
  EMAIL VARCHAR(128) UNIQUE,
  USER_TOKEN VARCHAR(64),
  PASSWORD VARCHAR(128),
  DTYPE VARCHAR(8) DEFAULT 'UUser'
);

CREATE TABLE USER_PHOTO (
  ID BIGINT(20) PRIMARY KEY,
  USER_ID BIGINT(20),
  PHOTO LONGBLOB,
  CONSTRAINT FK_USER_PHOTO FOREIGN KEY (USER_ID) REFERENCES UUSER_ABS(ID)
);


CREATE TABLE REMARK(
  ID BIGINT(20) PRIMARY KEY,
  POSTER_ID BIGINT(20),
  CONVERSATION_ID BIGINT(20),
  CONTENT LONGTEXT,
  TIME_STAMP DATETIME(6),
  CONSTRAINT FK_POSTER_REMARK FOREIGN KEY (POSTER_ID) REFERENCES UUSER_ABS(ID)
);

CREATE TABLE HAS_READ(
  REMARK_ID BIGINT(20),
  USER_ID BIGINT(20),
  CONSTRAINT FK_REMARK_HAS_READ FOREIGN KEY (REMARK_ID) REFERENCES REMARK(ID),
  CONSTRAINT FK_USER_HAS_READ FOREIGN KEY (USER_ID) REFERENCES UUSER_ABS(ID),
  CONSTRAINT COMP_PK_HAS_READ PRIMARY KEY (REMARK_ID, USER_ID)
);

CREATE TABLE COLLAPSE_USER(
  FOR_USER_ID BIGINT(20),
  TARGET_USER_ID BIGINT(20),
  CONSTRAINT FK_FOR_USER FOREIGN KEY (FOR_USER_ID) REFERENCES UUSER_ABS(ID),
  CONSTRAINT FK_TARGET_USER FOREIGN KEY (TARGET_USER_ID) REFERENCES UUSER_ABS(ID),
  CONSTRAINT COMP_PK_COLLAPSE_USER PRIMARY KEY (FOR_USER_ID, TARGET_USER_ID)
);