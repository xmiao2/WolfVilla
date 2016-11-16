package edu.ncsu.csc440.teamk.wolfvilla.model;

import java.sql.Timestamp;

/**
 * Created by Joshua on 10/24/2016.
 *
 * An object to hold all the information about a Check in held in the database.
 */
public class CheckInInformation {
    private long id;
    private Integer currentOcupancy;
    private Timestamp checkinTime;
    private Timestamp checkoutTime;
    private long billingInformationId;
    private long hotelId;
    private long roomNumber;
    private long customerId;
    private Long cateringStaffId;
    private Long roomServiceStaffId;

    public CheckInInformation() {
        this(-1, 0, new Timestamp(new java.util.Date().getTime()), null, -1, -1, -1, -1, null, null);
    }

    public CheckInInformation(long id, Integer currentOcupancy, Timestamp checkinTime, Timestamp checkoutTime,
                              long billingInformationId, long hotelId, long roomNumber, long customerId,
                              Long cateringStaffId, Long roomServiceStaffId) {
        this.id = id;
        this.currentOcupancy = currentOcupancy;
        this.checkinTime = checkinTime;
        this.checkoutTime = checkoutTime;
        this.billingInformationId = billingInformationId;
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.customerId = customerId;
        this.cateringStaffId = cateringStaffId;
        this.roomServiceStaffId = roomServiceStaffId;
    }

    public long getId() {
        return id;
    }

    public Integer getCurrentOcupancy() {
        return currentOcupancy;
    }

    public void setCurrentOcupancy(Integer currentOcupancy) {
        this.currentOcupancy = currentOcupancy;
    }

    public Timestamp getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(Timestamp checkinTime) {
        this.checkinTime = checkinTime;
    }

    public Timestamp getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(Timestamp checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public long getBillingInformationId() {
        return billingInformationId;
    }

    public void setBillingInformationId(long billingInformationId) {
        this.billingInformationId = billingInformationId;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public long getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(long roomNumber) {
        this.roomNumber = roomNumber;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public Long getCateringStaffId() {
        return cateringStaffId;
    }

    public void setCateringStaffId(Long cateringStaffId) {
        this.cateringStaffId = cateringStaffId;
    }

    public Long getRoomServiceStaffId() {
        return roomServiceStaffId;
    }

    public void setRoomServiceStaffId(Long roomServiceStaffId) {
        this.roomServiceStaffId = roomServiceStaffId;
    }
}
