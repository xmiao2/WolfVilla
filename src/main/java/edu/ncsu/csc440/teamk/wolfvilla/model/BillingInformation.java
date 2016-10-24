package edu.ncsu.csc440.teamk.wolfvilla.model;

import java.util.Date;

/**
 * Created by Joshua on 10/24/2016.
 */
public class BillingInformation {
    private long id;
    private String billingAddress;
    private String ssn;
    private String paymentMethod;
    private String cardNumber;
    private Date expirationDate;

    public BillingInformation(long id, String billingAddress, String ssn,
                              String paymentMethod, String cardNumber, Date expirationDate) {
        this.id = id;
        this.billingAddress = billingAddress;
        this.ssn = ssn;
        this.paymentMethod = paymentMethod;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
    }

    public long getId() {
        return id;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
