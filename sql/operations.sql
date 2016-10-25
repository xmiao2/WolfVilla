/**
 * @filename Operations.sql
 */

/*getStaff*/
SELECT *
FROM staff 
WHERE id = @staff_id;

/*listStaffs*/
SELECT *
FROM staff 
WHERE hotel_id = @hotel_id;

/*listStaffsByTitle*/
SELECT *
FROM staff 
WHERE hotel_id = @hotel_id AND title = @job_title;

/*createStaff*/
INSERT INTO staff 
VALUES (staff_seq.nextval, @name, @job_title, @ssn, @age, @gender, @address, @phone_number, @hotel_id);

/*updateStaff*/
UPDATE staff SET ssn = @ssn, name = @name, age=@age, gender=@gender,job_title=@job_title, phone_number=@phone_number, address=@address, hotel_id=@hotel_id
WHERE id = @staff_id;

/*deleteStaff*/
DELETE FROM staff 
WHERE id = @staff_id;

/*listRooms*/
SELECT room_number 
FROM rooms 
WHERE hotel_id = @hotel_id;

/*createRoom*/
INSERT INTO rooms 
VALUES(@hotel_id, @room_number, @category_name, @max_occupancy);

/*updateRoom*/
UPDATE rooms 
SET category_name = @category_name, max_occupancy = @max_occupancy
WHERE hotel_id = @hotel_id AND room_number = @room_number;

/*deleteRoom*/
DELETE FROM rooms
WHERE hotel_id = @hotel_id AND room_number = @room_number;

/*createRoomCategory*/
INSERT INTO room_categories
VALUES (@category_name, @max_occupancy, @nightly_rate);

/*updateRoomCategory*/
UPDATE room_categories
SET nightly_rate = @nightly_rate
WHERE category_name = @category_name AND max_occupancy = @max_occupancy;

/*deleteRoomCategory*/
DELETE FROM room_categories
WHERE category_name = @category_name AND max_occupancy  = @max_occupancy;

/*getCustomer*/
SELECT *
FROM customers
WHERE id = @customer_id;

/*createCustomer*/
INSERT INTO customers
VALUES (@customer_id, @name, @gender, @phone_number, @email, @address);

/*updateCustomer*/
UPDATE customers
SET name = @name, gender = @gender, phone_number = @phone_number, email = @email, address = @address
WHERE id = @customer_id;

/*deleteCustomer*/
DELETE from customers
WHERE id = @customer_id;

/*getHotel*/
SELECT *
FROM hotels 
WHERE id = @hotel_id;

/*createHotel*/
INSERT INTO hotels 
VALUES(hotel_seq.nextval, @address, @name, @phone_number);

/*updateHotel*/
UPDATE hotels 
SET name = @name, phone_number = @phone_number, address = @address 
WHERE id = @hotel_id;

/*deleteHotel*/
DELETE FROM hotels 
WHERE id = @hotel_id;

/*createCheckingEvent*/
INSERT INTO billing_information 
VALUES(billing_information_seq.nextval,  @billing_address, @payer_ssn, @payment_type, @card_number, @expiration_date);

INSERT INTO checkin_information VALUES(checkin_information_seq.nextval , @occupancy_number, @check_in_time, @checkout_time, 99, @hotel_id, @room_number, @customer_id, @catering_staff, @room_service_staff);

/*getCheckingEvent*/
SELECT *
FROM checkin_information 
WHERE id = @checking_event_id;

/*listCheckingEvents*/
SELECT * 
FROM checkin_information 
WHERE hotel_id = @hotel_id;

/*updateCheckingEvent*/
UPDATE checkin_information 
SET hotel_id = @hotel_id,  room_number=@room_number, current_occupancy=@current_occupancy, checkin_time = @checkin_time, checkout_time=@checkout_time, catering_staff_id=@catering_staff, room_service_staff_id=@room_service_staff 
WHERE id=@checking_event_id;

/*deleteCheckingEvent*/
DELETE from checkin_information
WHERE id = @checking_event_id;

/*checkout*/
UPDATE checkin_information
SET checkout_time = @checkout_time
WHERE id = @checking_event_id;

/* assignManager(manage_id, hotel_id) */
DELETE FROM manager
WHERE hotel_id = @hotel_id;

INSERT INTO manager
VALUES(@manager_id, @hotel_id);

/* addService */
INSERT INTO services
VALUES (services_seq.nextval, @name, @price, @staff_id, @checking_event_id);

/*deleteService*/
DELETE FROM services
WHERE id = @service_id;

/*updateService*/
UPDATE services
SET staff_id = @staff_id, description = @description, price = @price, checkin_id  = @checking_event_id 
WHERE id = @service_id;

/*totalServiceCosts*/
SELECT SUM(price) 
FROM services 
WHERE checkin_id = @checking_event_id;

/* updateBillingInformation(checking_event_id, customer_id, payer_ssn, payment_method, billing_address, card_number, expiration_date) */
UPDATE billing_information 
SET billing_address=@billing_address, ssn=@payer_ssn, payment_method=@payment_method, card_number=@card_number, expiration_date = @expiration_date 
WHERE id = (SELECT billing_information_id FROM checkin_information WHERE id = @checking_event_id);

/* getBillTotal */
DECLARE @service_price;
SELECT @service_price = SUM(price) FROM services WHERE  checkin_id = @checking_event_id;

DECLARE @room_rate;
SELECT @room_rate = price FROM room_categories WHERE (category_name, max_occupancy) = (SELECT category_name, max_occupancy FROM rooms WHERE (hotel_id, room_number) = (SELECT hotel_id, room_number FROM checkin_information WHERE id = @checking_event_id));

DECLARE @room_price;

DECLARE @days_stayed;
SELECT @days_stayed = checkout_time - checkin_time;
SELECT @room_price = @room_rate*(checkout_time - checkin_time) WHERE id = @checking_event_id;

DECLARE @room_price;
SELECT @room_price = price*(checkout_time - checkin_time) FROM room_categories, checkin_information WHERE id = @checking_event_id AND (category_name, max_occupancy) = (SELECT category_name, max_occupancy FROM rooms WHERE (hotel_id, room_number) = ( hotel_id, room_number));

/*Report available*/
SELECT *
FROM rooms 
WHERE hotel_id = @hotel_id AND max_occupancy > @current_occupants AND room_type = @room_type AND room_number NOT IN (SELECT room_number 
FROM checkin_information 
WHERE checkin_information.hotel_id = hotel_id AND (@date_start < date_end OR date_end IS NULL) AND date_start < @date_end);

/*Report occupied*/
SELECT room_number 
FROM checkin_information 
WHERE (date_end IS NULL OR @date_start < date_end) AND date_start < @date_end AND checkin_information.hotel_id = hotel_id;

/*reportUnoccupanied*/
SELECT *
FROM rooms 
WHERE hotel_id = @hotel_id AND room_number NOT IN (SELECT room_number 
FROM checkin_information 
WHERE checkin_information.hotel_id = hotel_id AND (@date_start < date_end OR date_end IS NULL) AND date_start < @date_end);

/*reportOccupants*/
SELECT customer_id
FROM checkin_information 
WHERE (date_end IS NULL OR @date_start < date_end) AND date_start < @date_end AND checkin_information.hotel_id = hotel_id;

/*percentOccupied*/

DECLARE @occupied;
SELECT @occupied = count( DISTINCT room_number) 
FROM checkin_information 
WHERE (date_end IS NULL OR @date_start < date_end) AND date_start < @date_end AND checkin_information.hotel_id = hotel_id;

DECLARE @total
SELECT @total = count(DISTINCT room_number)
FROM rooms
WHERE hotel_id = @hotel_id;

/*getStaffByRole*/
SELECT *
FROM staff
WHERE title = @job_title;

/*getCustomersOfStaff*/
SELECT customer_id
FROM services
WHERE staff_id = @staffID;
