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
  manager int UNIQUE,
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