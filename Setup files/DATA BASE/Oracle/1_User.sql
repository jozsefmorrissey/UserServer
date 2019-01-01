
--==========================  USERS  =====================--

DROP USER UserSrvc CASCADE;

CREATE USER UserSrvc
IDENTIFIED BY '&1'
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp;

GRANT connect to UserSrvc;
GRANT resource to UserSrvc;
GRANT CREATE SESSION TO UserSrvc;
GRANT CREATE TABLE TO UserSrvc;
GRANT CREATE VIEW TO UserSrvc;
GRANT CREATE MATERIALIZED VIEW TO UserSrvc;
GRANT DEBUG CONNECT SESSION TO UserSrvc;
GRANT DEBUG ANY PROCEDURE TO UserSrvc;
GRANT create session to UserSrvc;
ALTER USER UserSrvc QUOTA 10m ON users;

conn UserSrvc/'&1'
