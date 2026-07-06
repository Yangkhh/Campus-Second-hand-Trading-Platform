UPDATE users
SET password = '$2a$10$k6nbvkf08GjeABDnIQpzbeCSrmis0oSr7OAHMao1UAlCz1rJ6ria.'
WHERE username IN ('alice', 'bob', 'admin');
