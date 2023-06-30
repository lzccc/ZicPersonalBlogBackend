-- Drop user first if they exist
DROP USER if exists 'springzic'@'localhost' ;

-- Now create user with prop privileges--
-- IDENTIFIED BY -- followed by password
CREATE USER 'springzic'@'localhost' IDENTIFIED BY 'springzic';

GRANT ALL PRIVILEGES ON * . * TO 'springzic'@'localhost';