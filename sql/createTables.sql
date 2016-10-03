CREATE TABLE hotels (id int PRIMARY KEY, address varchar(50), name varchar(30), phone_number varchar(10), manager_id int NOT NULL UNIQUE FOREIGN KEY REFERENCES managers(id));

CREATE TABLE managers (id int PRIMARY KEY REFERENCES staff(id));