
--==========================  TABLES  =====================--

CREATE TABLE UUSER_ABS (
  ID NUMBER PRIMARY KEY,
  FULL_NAME VARCHAR2(64),
  EMAIL VARCHAR2(128) UNIQUE,
  USER_TOKEN VARCHAR2(64),
  PASSWORD VARCHAR(128),
  DTYPE VARCHAR(8) DEFAULT 'UUser'
);

CREATE TABLE USER_PHOTO (
  ID NUMBER PRIMARY KEY,
  USER_ID NUMBER,
  PHOTO BLOB,
  CONSTRAINT FK_USER_PHOTO FOREIGN KEY (USER_ID) REFERENCES UUSER_ABS(ID)
);


CREATE TABLE REMARK(
  ID NUMBER PRIMARY KEY,
  POSTER_ID NUMBER,
  CONVERSATION_ID NUMBER,
  CONTENT CLOB,
  TIME_STAMP TIMESTAMP,
  CONSTRAINT FK_POSTER_REMARK FOREIGN KEY (POSTER_ID) REFERENCES UUSER_ABS(ID)
);

CREATE TABLE HAS_READ(
  REMARK_ID NUMBER,
  USER_ID NUMBER,
  CONSTRAINT FK_REMARK_HAS_READ FOREIGN KEY (REMARK_ID) REFERENCES REMARK(ID),
  CONSTRAINT FK_USER_HAS_READ FOREIGN KEY (USER_ID) REFERENCES UUSER_ABS(ID),
  CONSTRAINT COMP_PK_HAS_READ PRIMARY KEY (REMARK_ID, USER_ID)
);

CREATE TABLE COLLAPSE_USER(
  FOR_USER_ID NUMBER,
  TARGET_USER_ID NUMBER,
  CONSTRAINT FK_FOR_USER FOREIGN KEY (FOR_USER_ID) REFERENCES UUSER_ABS(ID),
  CONSTRAINT FK_TARGET_USER FOREIGN KEY (TARGET_USER_ID) REFERENCES UUSER_ABS(ID),
  CONSTRAINT COMP_PK_COLLAPSE_USER PRIMARY KEY (FOR_USER_ID, TARGET_USER_ID)
);
