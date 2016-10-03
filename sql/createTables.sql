CREATE TABLE hotels (id int PRIMARY KEY, address varchar(50), name varchar(30), phone_number varchar(10), manager_id int NOT NULL FOREIGN KEY REFERENCES managers(id));

CREATE TABLE managers (id int PRIMARY KEY);
