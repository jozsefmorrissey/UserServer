
--==========================  USERS  =====================--

DROP USER ${user} CASCADE;

CREATE USER ${user}
IDENTIFIED BY ${password}
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp;

GRANT connect to ${user};
GRANT resource to ${user};
GRANT CREATE SESSION TO ${user};
GRANT CREATE TABLE TO ${user};
GRANT CREATE VIEW TO ${user};
GRANT CREATE MATERIALIZED VIEW TO ${user};
GRANT DEBUG CONNECT SESSION TO ${user};
GRANT DEBUG ANY PROCEDURE TO ${user};
GRANT create session to ${user};
ALTER USER ${user} QUOTA 10m ON users;

conn ${user}/${password}
