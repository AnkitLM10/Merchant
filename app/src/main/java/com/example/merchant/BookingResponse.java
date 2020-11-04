package com.example.merchant;


import java.util.Date;
import java.util.List;

class Booking {
    public int bookingId;
    public String clientName;
    public Object merchant;
    public List<Object> services;
    public Date date;
    public String status;
    public String razorpayOrderId;
    public int payableAmount;
}

public class BookingResponse {
    public String responseCode;
    public String message;
    public List<Booking> bookings;
}

