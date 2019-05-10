
-- ==========================  USERS  =====================--

DROP DATABASE IF EXISTS ${database};
DROP USER IF EXISTS '${user}'@'localhost';

CREATE USER '${user}'@'${host}' IDENTIFIED BY '${password}';

SYSTEM mysql -u ${user} -p${password}

CREATE DATABASE ${database};

GRANT ALL PRIVILEGES ON ${database}.* TO '${user}'@'${host}';

USE ${database};
