DROP TABLE hotels CASCADE CONSTRAINTS;
DROP TABLE staff CASCADE CONSTRAINTS;
DROP TABLE rooms CASCADE CONSTRAINTS;
DROP TABLE room_categories CASCADE CONSTRAINTS;
DROP TABLE checkin_information CASCADE CONSTRAINTS;
DROP TABLE billing_information CASCADE CONSTRAINTS;
DROP TABLE customers CASCADE CONSTRAINTS;
DROP TABLE services CASCADE CONSTRAINTS;
DROP TABLE title_department CASCADE CONSTRAINTS;

DROP SEQUENCE hotel_seq;
DROP SEQUENCE staff_seq;
DROP SEQUENCE checkin_information_seq;
DROP SEQUENCE billing_information_seq;
DROP SEQUENCE customer_seq;
DROP SEQUENCE services_seq;

CREATE TABLE hotels (
  id int PRIMARY KEY,
  manager int,
  address varchar(128),
  name varchar(32),
  phone_number varchar(10)
);

CREATE TABLE title_department(
  title varchar(32) PRIMARY KEY,
  department varchar(32) NOT NULL
);

CREATE TABLE staff (
  id int PRIMARY KEY,
  name varchar(32),
  title varchar(32) NOT NULL REFERENCES title_department(title) ON DELETE CASCADE,
  ssn varchar(9),
  age int,
  gender char(1),
  address varchar(128),
  phone_number varchar(10),
  hotel_id NOT NULL CONSTRAINT fk_staff_hotel_id REFERENCES hotels(id)
);

ALTER TABLE hotels ADD (CONSTRAINT manager_is_staff FOREIGN KEY (manager) REFERENCES staff(id) ON DELETE CASCADE);

CREATE TABLE room_categories (
  category_name varchar(32),
  max_occupancy int,
  nightly_rate binary_float,
  CONSTRAINT pk_category_id PRIMARY KEY(category_name, max_occupancy)
);

CREATE TABLE rooms (
  hotel_id int REFERENCES hotels(id) ON DELETE CASCADE,
  room_number int,
  category_name varchar(32) NOT NULL,
  max_occupancy int NOT NULL,
  CONSTRAINT fk_room_category_id FOREIGN KEY (category_name, max_occupancy)  REFERENCES room_categories(category_name, max_occupancy),
  CONSTRAINT pk_room_hotel_id PRIMARY KEY(hotel_id, room_number)
);

CREATE TABLE billing_information (
  id int PRIMARY KEY,
  billing_address varchar(100) NOT NULL,
  ssn varchar(9) NOT NULL,
  payment_method varchar(20) NOT NULL,
  card_number varchar(16) NOT NULL,
  expiration_date date NOT NULL
);

CREATE TABLE customers (
  id int PRIMARY KEY,
  name varchar(30),
  gender varchar(1),
  phone_number varchar(10),
  email varchar(30),
  address varchar(100)
);

CREATE TABLE checkin_information (
  id int PRIMARY KEY,
  current_occupancy int,
  checkin_time TIMESTAMP(0) NOT NULL,
  checkout_time TIMESTAMP(0),
  billing_information_id int UNIQUE NOT NULL REFERENCES billing_information(id) ON DELETE CASCADE,
  hotel_id int NOT NULL,
  room_number int NOT NULL,
  customer_id int NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
  catering_staff_id int REFERENCES staff(id),
  room_service_staff_id int REFERENCES staff(id),
  CONSTRAINT fk_hotel_room FOREIGN KEY (hotel_id, room_number) REFERENCES rooms(hotel_id, room_number),
  CHECK(checkout_time > checkin_time OR checkout_time IS NULL)
);

CREATE TABLE services (
  id int primary key,
  description varchar(64),
  price binary_float,
  staff_id int NOT NULL CONSTRAINT fk_services_staff_id REFERENCES staff(id) ON DELETE CASCADE,
  checkin_id NOT NULL CONSTRAINT fk_services_checkin_id REFERENCES checkin_information(id) ON DELETE CASCADE
);

CREATE SEQUENCE hotel_seq MINVALUE 0 START WITH 0;
CREATE SEQUENCE staff_seq MINVALUE 0 START WITH 0;
CREATE SEQUENCE checkin_information_seq MINVALUE 0 START WITH 0;
CREATE SEQUENCE billing_information_seq MINVALUE 0 START WITH 0;
CREATE SEQUENCE customer_seq MINVALUE 0 START WITH 0;
CREATE SEQUENCE services_seq MINVALUE 0 START WITH 0;

INSERT INTO hotels VALUES (hotel_seq.nextval, null, '1011 Wolfvilla Dr. Raleigh NC, 27613', 'WolfVilla Raleigh', '9195550100');
INSERT INTO hotels VALUES (hotel_seq.nextval, null, '900 W. WolfVilla St. Charlotte NC, 27613', 'WolfVilla Charlotte', '7045550101');
INSERT INTO hotels VALUES (hotel_seq.nextval, null, '301 Wolfvilla Rd. Greensboro NC, 27613', 'WolfVilla Greensboro', '3365550102');
INSERT INTO hotels VALUES (hotel_seq.nextval, null, '166 E. Wolfvilla Ave. Wilmington NC, 27613', 'WolfVilla Wilmington', '9105550103');

INSERT INTO title_department VALUES('Admin', 'admin');
INSERT INTO title_department VALUES('Manager', 'Administration');
INSERT INTO title_department VALUES('Catering Staff', 'Catering');
INSERT INTO title_department VALUES('Service Staff', 'Service');
INSERT INTO title_department VALUES('Front Desk representative', 'Administration');

INSERT INTO staff VALUES (staff_seq.nextval, 'Admin Power', 'Admin', '523122111', 57, 'M', '200 Washington Sq. Greensboro NC, 27613', '9192013041', 0);
INSERT INTO staff VALUES (staff_seq.nextval, 'Antaua Flanko', 'Front Desk representative', '523122111', 57, 'M', '200 Washington Sq. Greensboro NC, 27613', '9192013041', 0);
INSERT INTO staff VALUES (staff_seq.nextval, 'Will George', 'Manager', '523122111', 57, 'M', '200 Washington Sq. Greensboro NC, 27613', '9192013041', 0);
INSERT INTO staff VALUES (staff_seq.nextval, 'Jeff Thomason', 'Manager', '349302932', 58, 'M', '301 Anderson Ct. Raleigh NC, 27613', '7045663088', 1);
INSERT INTO staff VALUES (staff_seq.nextval, 'Rose Teddi', 'Manager', '340902304', 30, 'F', '404 Anderson Ct. Raleigh NC, 27613', '9101236125', 2);
INSERT INTO staff VALUES (staff_seq.nextval, 'Linda Abrams', 'Manager', '604859301', 40, 'F', '500 Anderson Ct. Raleigh NC, 27613', '9191232041', 3);
INSERT INTO staff VALUES (staff_seq.nextval, 'Namy Notlikely', 'Catering Staff', '523122111', 37, 'M', '115 Washington Sq. Greensboro NC, 27613', '000000000', 0);
INSERT INTO staff VALUES (staff_seq.nextval, 'Hack Stabber', 'Catering Staff', '349302932', 29, 'M', '301 Anderson Ct. Raleigh NC, 27613', '7045663087', 1);
INSERT INTO staff VALUES (staff_seq.nextval, 'Unique Person', 'Catering Staff', '333224444', 31, 'F', '402 Anderson Ct. Raleigh NC, 27613', '9101236124', 2);
INSERT INTO staff VALUES (staff_seq.nextval, 'Another Name', 'Catering Staff', '011100100', 23, 'F', '400 Anderson Ct. Raleigh NC, 27613', '9191232040', 3);
INSERT INTO staff VALUES (staff_seq.nextval, 'Jay Perry', 'Service Staff', '987654321', 45, 'M', '1502 Perrywinkle Way, Raleigh, NC 27614',  '9191232041',0);
INSERT INTO staff VALUES (staff_seq.nextval, 'Veronica Vinkle', 'Service Staff', '273487628', 34, 'F', '666 Magical Ct, Raleigh, NC 27615',  '9191232042',1);
INSERT INTO staff VALUES (staff_seq.nextval, 'Robert Anderson', 'Service Staff', '001398250', 29, 'M', '135 Zipster Rd, Raleigh, NC 27613',  '9191232043',2);
INSERT INTO staff VALUES (staff_seq.nextval, 'Katie Ferguson', 'Service Staff', '456745656', 23, 'F', '102 Sailer Drive, Raleigh, NC 27614', '9191232044', 3);

UPDATE hotels SET manager = 2 WHERE id = 0;
UPDATE hotels SET manager = 3 WHERE id = 1;
UPDATE hotels SET manager = 4 WHERE id = 2;
UPDATE hotels SET manager = 5 WHERE id = 3;

INSERT INTO room_categories VALUES ('Economy', 1, 100);
INSERT INTO room_categories VALUES ('Economy', 2, 115);
INSERT INTO room_categories VALUES ('Economy', 3, 130);
INSERT INTO room_categories VALUES ('Economy', 4, 150);
INSERT INTO room_categories VALUES ('Deluxe', 1, 150);
INSERT INTO room_categories VALUES ('Deluxe', 2, 170);
INSERT INTO room_categories VALUES ('Deluxe', 3, 190);
INSERT INTO room_categories VALUES ('Deluxe', 4, 210);
INSERT INTO room_categories VALUES ('Executive Suite', 1, 225);
INSERT INTO room_categories VALUES ('Executive Suite', 2, 250);
INSERT INTO room_categories VALUES ('Executive Suite', 3, 275);
INSERT INTO room_categories VALUES ('Executive Suite', 4, 300);
INSERT INTO room_categories VALUES ('Presidential Suite', 1, 300);
INSERT INTO room_categories VALUES ('Presidential Suite', 2, 350);
INSERT INTO room_categories VALUES ('Presidential Suite', 3, 400);
INSERT INTO room_categories VALUES ('Presidential Suite', 4, 450);

INSERT INTO rooms VALUES (0, 1, 'Economy', 4);
INSERT INTO rooms VALUES (0, 2, 'Economy', 4);
INSERT INTO rooms VALUES (0, 3, 'Economy', 3);
INSERT INTO rooms VALUES (0, 4, 'Economy', 3);
INSERT INTO rooms VALUES (0, 5, 'Economy', 2);
INSERT INTO rooms VALUES (0, 6, 'Economy', 2);
INSERT INTO rooms VALUES (0, 7, 'Economy', 1);
INSERT INTO rooms VALUES (0, 8, 'Economy', 1);
INSERT INTO rooms VALUES (0, 101, 'Deluxe', 4);
INSERT INTO rooms VALUES (0, 102, 'Deluxe', 4);
INSERT INTO rooms VALUES (0, 103, 'Deluxe', 3);
INSERT INTO rooms VALUES (0, 104, 'Deluxe', 3);
INSERT INTO rooms VALUES (0, 105, 'Deluxe', 2);
INSERT INTO rooms VALUES (0, 106, 'Deluxe', 2);
INSERT INTO rooms VALUES (0, 107, 'Deluxe', 1);
INSERT INTO rooms VALUES (0, 108, 'Deluxe', 1);
INSERT INTO rooms VALUES (0, 201, 'Executive Suite', 4);
INSERT INTO rooms VALUES (0, 202, 'Executive Suite', 4);
INSERT INTO rooms VALUES (0, 203, 'Executive Suite', 3);
INSERT INTO rooms VALUES (0, 204, 'Executive Suite', 3);
INSERT INTO rooms VALUES (0, 205, 'Executive Suite', 2);
INSERT INTO rooms VALUES (0, 206, 'Executive Suite', 2);
INSERT INTO rooms VALUES (0, 207, 'Executive Suite', 1);
INSERT INTO rooms VALUES (0, 208, 'Executive Suite', 1);
INSERT INTO rooms VALUES (0, 301, 'Presidential Suite', 4);
INSERT INTO rooms VALUES (0, 302, 'Presidential Suite', 4);
INSERT INTO rooms VALUES (0, 303, 'Presidential Suite', 3);
INSERT INTO rooms VALUES (0, 304, 'Presidential Suite', 3);
INSERT INTO rooms VALUES (0, 305, 'Presidential Suite', 2);
INSERT INTO rooms VALUES (0, 306, 'Presidential Suite', 2);
INSERT INTO rooms VALUES (0, 307, 'Presidential Suite', 1);
INSERT INTO rooms VALUES (0, 308, 'Presidential Suite', 1);
INSERT INTO billing_information VALUES (billing_information_seq.nextval, '3 Prime Street, CityTown, USState, 12345', '157111317', 'credit', '1991288237734664', DATE '2020-05-30');
INSERT INTO billing_information VALUES (billing_information_seq.nextval, '23 Prime Street, CityTown, USState, 12345', '101293137', 'debit', '4143475359616771', DATE '2019-04-11');
INSERT INTO billing_information VALUES (billing_information_seq.nextval, '79 Prime Street, CityTown, USState, 12345', '103838997', 'debit', '1009101310191021', DATE '2018-11-02');
INSERT INTO billing_information VALUES (billing_information_seq.nextval, '10 triple Street, Pythagorean, USState, 13100', '106172025', 'debit', '2629343740414550', DATE '2017-10-01');
INSERT INTO customers VALUES (customer_seq.nextval, 'Edsger Dijkstra', 'M', '9192221111', 'csc_professor_1@ncsu.edu', '401 Sydney ct. Raleigh NC, 27608');
INSERT INTO customers VALUES (customer_seq.nextval, 'Alan Turing', 'M', '9192221112', 'csc_professor_2@ncsu.edu', '402 Sydney ct. Raleigh NC, 27608');
INSERT INTO customers VALUES (customer_seq.nextval, 'Grace Hopper', 'F', '9192221113', 'csc_professor_3@ncsu.edu', '403 Sydney ct. Raleigh NC, 27608');
INSERT INTO customers VALUES (customer_seq.nextval, 'Donald Knuth', 'M', '9192221114', 'csc_professor_4@ncsu.edu', '404 Sydney ct. Raleigh NC, 27608');

INSERT INTO checkin_information VALUES (checkin_information_seq.nextval, 2, TIMESTAMP '2017-1-01 17:38:11', TIMESTAMP '2017-1-11 12:38:11', 0, 0 , 2, 1, NULL, NULL);
INSERT INTO checkin_information VALUES (checkin_information_seq.nextval, 2, TIMESTAMP '2017-1-02 17:38:11', TIMESTAMP '2017-1-12 19:38:11', 1, 0, 3, 2, NULL, NULL);
INSERT INTO checkin_information VALUES (checkin_information_seq.nextval, 3, TIMESTAMP '2017-1-03 07:38:11', TIMESTAMP '2017-1-13 17:38:11', 2, 0, 305, 3, 4, 8);
INSERT INTO checkin_information VALUES (checkin_information_seq.nextval, 3, TIMESTAMP '2017-1-04 17:38:13', NULL, 3, 0, 306, 2, 5, 9);

INSERT INTO services VALUES (services_seq.nextval, 'Changing bedsheets', 1.98, 8, 0);
INSERT INTO services VALUES (services_seq.nextval, 'Refilling fridge contents', 15.26, 9, 1);
INSERT INTO services VALUES (services_seq.nextval, 'Gluten free meal for 2', 59.99, 4, 1);
INSERT INTO services VALUES (services_seq.nextval, 'Southern BBQ catering', 299.99, 5, 2);
