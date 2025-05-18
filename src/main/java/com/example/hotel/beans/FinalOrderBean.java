package com.example.hotel.beans;

// 用于封装最终订单信息，存储和显示
public class FinalOrderBean {
    private String orderId;
    private String roomTypeName;
    private String hotelName;
    private String checkInDate;
    private String checkOutDate;
    private String guestName;
    private String contactNumber;
    private double totalFee;
    private double depositAmountPaid; // 实际支付的定金
    private String paymentMethod;
    private String transactionId;
    private String orderStatus; //
    private String bookingTimestamp; // 预订时间戳 (格式: yyyy-MM-dd xx:xx:xx)

    // 后置条件中提到的信息
    private String virtualRoomNumber; // 虚拟房号
    private String nfcKey; // NFC密钥

    private String bookedRoomId; // 外键，关联到 rooms 表的 room_id
    private String specialRequests; // 特殊要求
    private String hotelLocation;
    private String hotelContact;

    public FinalOrderBean() {
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public double getDepositAmountPaid() {
        return depositAmountPaid;
    }

    public void setDepositAmountPaid(double depositAmountPaid) {
        this.depositAmountPaid = depositAmountPaid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getBookingTimestamp() {
        return bookingTimestamp;
    }

    public void setBookingTimestamp(String bookingTimestamp) {
        this.bookingTimestamp = bookingTimestamp;
    }

    public String getVirtualRoomNumber() {
        return virtualRoomNumber;
    }

    public void setVirtualRoomNumber(String virtualRoomNumber) {
        this.virtualRoomNumber = virtualRoomNumber;
    }

    public String getNfcKey() {
        return nfcKey;
    }

    public void setNfcKey(String nfcKey) {
        this.nfcKey = nfcKey;
    }

    public String getBookedRoomId() {
        return bookedRoomId;
    }

    public void setBookedRoomId(String bookedRoomId) {
        this.bookedRoomId = bookedRoomId;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
    public String getHotelLocation() {
        return hotelLocation;
    }

    public void setHotelLocation(String hotelLocation) {
        this.hotelLocation = hotelLocation;
    }

    public String getHotelContact() {
        return hotelContact;
    }

    public void setHotelContact(String hotelContact) {
        this.hotelContact = hotelContact;
    }

    @Override
    public String toString() {
        return "FinalOrderBean{" +
                "orderId='" + orderId + '\'' +
                ", roomTypeName='" + roomTypeName + '\'' +
                ", hotelName='" + hotelName + '\'' +
                ", guestName='" + guestName + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", bookedRoomId='" + bookedRoomId + '\'' +
                ", specialRequests='" + specialRequests + '\'' +
                ", hotelLocation='" + hotelLocation + '\'' +
                ", hotelContact='" + hotelContact + '\'' +
                '}';
    }
} 