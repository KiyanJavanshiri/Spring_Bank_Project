INSERT INTO abstract_entity (id) VALUES (1);
INSERT INTO abstract_entity (id) VALUES (2);
INSERT INTO abstract_entity (id) VALUES (3);
INSERT INTO abstract_entity (id) VALUES (4);
INSERT INTO abstract_entity (id) VALUES (5);

INSERT INTO customers (id, name, email, age) VALUES (1, 'John', 'john@mail.com', 25);
INSERT INTO customers (id, name, email, age) VALUES (2, 'Alice', 'alice@mail.com', 30);

INSERT INTO employers (id, name, address) VALUES (3, 'Google', 'USA');
INSERT INTO employers (id, name, address) VALUES (4, 'Amazon', 'USA');

INSERT INTO abstract_entity (id) VALUES (6);
INSERT INTO accounts (id, number, balance, currency, customer_id)
VALUES (6, 'ACC-1', 1000, 'USD', 1);

INSERT INTO customer_employer (customer_id, employer_id) VALUES (1, 3);
INSERT INTO customer_employer (customer_id, employer_id) VALUES (1, 4);
INSERT INTO customer_employer (customer_id, employer_id) VALUES (2, 4);