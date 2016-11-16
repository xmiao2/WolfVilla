INSERT INTO hotels
VALUES (hotel_seq.nextval, NULL, '27 Timber Dr, Garner, NC 27529', 'Wolfvilla', '9767281980');

INSERT INTO title_department
VALUES('Front Desk representative', 'Administration');

INSERT INTO title_department
VALUES('Catering Staff', 'Catering');

INSERT INTO title_department
VALUES('Manager', 'Administration');

/* THIS IS NOT PART OF THE NARRATIVE BUT WE NEED IT */
INSERT INTO title_department
VALUES('Room Service Staff', 'Room Service');

INSERT INTO title_department
VALUES('Admin', 'admin');

INSERT INTO staff
VALUES (staff_seq.nextval, 'David D. Clukey', 'Front Desk representative', '409021234',
NULL, 'M', '106, Cloverdale Ct, Raleigh, NC, 27607', '9801311238', 0);

INSERT INTO staff
VALUES (staff_seq.nextval, 'James M. Gooden', 'Catering Staff', '143229089', NULL, 'M',
'109, Cloverdale Ct, Raleigh, NC, 27607', '9801871983', 0);

INSERT INTO staff
VALUES (staff_seq.nextval, 'Todd C. Chen', 'Manager', '132674793', NULL, 'M',
'1048, Avent Ferry Road, Raleigh, NC, 27606', '9767281980', 0);

/* THIS IS NOT PART OF THE NARRATIVE BUT WE NEED IT */
INSERT INTO staff
VALUES (staff_seq.nextval, 'Donald J Trump', 'Room Service Staff', '143229088', NULL, 'M',
'109, Initiative Ct, Raleigh, NC, 27607', '9801871657', 0);

INSERT INTO staff
VALUES (staff_seq.nextval, 'Admin Power', 'Admin', '143229089', NULL, 'M',
'110, Initiative Ct, Raleigh, NC, 27608', '9801871659', 0);

UPDATE hotels
SET manager=2
WHERE id=0;

INSERT INTO room_categories
VALUES ('Economy', 2, 150);

INSERT INTO room_categories
VALUES ('Executive Suite', 2, 250);

INSERT INTO room_categories
VALUES ('Deluxe', 2, 350);

INSERT INTO rooms
VALUES(0, 101, 'Economy', 2);

INSERT INTO rooms
VALUES(0, 201, 'Executive Suite', 2);

INSERT INTO rooms
VALUES(0, 301, 'Deluxe', 2);

/* These dates are NOT VALID, as none were provided. Using temp dates for the moment*/
INSERT INTO billing_information
VALUES (billing_information_seq.nextval, '881 Java Lane Graniteville, SC 29829', '144549090',
'Credit Card', '5184950505589328', DATE '2020-05-30');

INSERT INTO billing_information
VALUES (billing_information_seq.nextval, '2697 Stroop Hill Road Atlanta, GA 30342', '678900900',
'Credit Card', '5196591432385020', DATE '2020-05-30');

INSERT INTO customers
VALUES (customer_seq.nextval, 'Carl T. Ashcraft', 'M', NULL, 'carlashcraft@kmail.us',
'881 Java Lane Graniteville, SC 29829');

INSERT INTO customers
VALUES (customer_seq.nextval, 'Angela J. Roberts', 'F', NULL, 'angelaroberts@kmail.us',
'2697 Stroop Hill Road Atlanta, GA 30342');

INSERT INTO checkin_information
VALUES (checkin_information_seq.nextval, 1, TIMESTAMP '2016-11-12 12:00:00', NULL, 0, 0, 101,
0, NULL, NULL);

INSERT INTO checkin_information
VALUES (checkin_information_seq.nextval, 1,  TIMESTAMP '2016-11-14 12:00:00', NULL, 1, 0, 201,
1, NULL, NULL);

INSERT INTO services
VALUES (services_seq.nextval, 'Restaurant bill 1', 30, 1, 0);

INSERT INTO services
VALUES (services_seq.nextval, 'Restaurant bill 2', 35, 1, 0);

INSERT INTO services
VALUES (services_seq.nextval, 'Laundry bill 1', 15, 3, 0);

INSERT INTO services
VALUES (services_seq.nextval, 'Restaurant bill 1', 40, 1, 1);

INSERT INTO services
VALUES (services_seq.nextval, 'Laundry bill 1', 15, 3, 1);

INSERT INTO services
VALUES (services_seq.nextval, 'Laundry bill 2', 10, 3, 1);