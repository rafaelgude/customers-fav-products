INSERT INTO user(id, email, password)
SELECT 1, "admin@example.com", "$2a$10$NWg0po03v15WpQV2De2i2uvuQaAj3cGtw.PmSNG0Jn4GBqKT/Gtoy"
WHERE NOT EXISTS (SELECT 1 FROM user where id = 1);

INSERT INTO user_roles(user_id, role_id)
SELECT 1, 1
WHERE NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = 1);

INSERT INTO user(id, email, password)
SELECT 2, "user@example.com", "$2a$10$NWg0po03v15WpQV2De2i2uvuQaAj3cGtw.PmSNG0Jn4GBqKT/Gtoy"
WHERE NOT EXISTS (SELECT 1 FROM user where id = 2);

INSERT INTO user_roles(user_id, role_id)
SELECT 2, 0
WHERE NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = 2);