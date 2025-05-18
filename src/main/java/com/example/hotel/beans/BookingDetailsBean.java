package com.example.hotel.beans;

// 预订信息
public class BookingDetailsBean {
    private String orderId; // 订单号
    private RoomResultBean selectedRoom; // 聚合了选择的房间信息
    private String userName; // 预订用户名 (来自登录用户)
    private String contactInfo; // 预订用户联系方式 (来自登录用户)
    private String checkInDate;
    private String checkOutDate;
    private double totalFee; //
    private double depositAmount; // 定金金额 (总费用的8%)
    private String orderStatus; // 订单状态
    private String specialRequests; // 特殊需求
    private String cancellationPolicy; // 取消政策说明
    private int numberOfNights; // 入住晚数，用于计算
    private java.util.Date bookingDate; // 预订日期时间

    // 为管理预订页面添加的字段
    private int bookingId;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private int numberOfRooms = 1;
    private int numberOfGuests = 2;
    private String paymentMethod;
    private String roomId;
    private double pricePerNight;

    public BookingDetailsBean() {
        this.bookingDate = new java.util.Date(); // 默认为当前时间
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public RoomResultBean getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(RoomResultBean selectedRoom) {
        this.selectedRoom = selectedRoom;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
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

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(int numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    /**
     * 获取客户名称
     */
    public String getCustomerName() {
        return userName;
    }

    /**
     * 获取酒店名称
     */
    public String getHotelName() {
        if (selectedRoom != null) {
            return selectedRoom.getHotelName();
        }
        return "";
    }

    /**
     * 获取房间类型名称
     */
    public String getRoomTypeName() {
        if (selectedRoom != null) {
            return selectedRoom.getRoomTypeName();
        }
        return "";
    }

    /**
     * 获取订单总金额
     */
    public double getTotalAmount() {
        return totalFee;
    }

    /**
     * 获取订单状态
     */
    public String getStatus() {
        return orderStatus;
    }

    /**
     * 获取预订日期
     */
    public java.util.Date getBookingDate() {
        return bookingDate;
    }

    /**
     * 设置预订日期
     */
    public void setBookingDate(java.util.Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    // 可以从时间戳字符串设置预订日期
    public void setBookingDateFromString(String dateTimeStr) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.bookingDate = sdf.parse(dateTimeStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            this.bookingDate = new java.util.Date(); // 失败时使用当前时间
        }
    }

    // 为管理预订页面添加的方法
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getRoomId() {
        if (selectedRoom != null) {
            return selectedRoom.getRoomId();
        }
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public double getPricePerNight() {
        if (selectedRoom != null) {
            return selectedRoom.getPricePerNight();
        }
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    @Override
    public String toString() {
        return "BookingDetailsBean{" +
                "orderId='" + orderId + '\'' +
                ", selectedRoom=" + (selectedRoom != null ? selectedRoom.getRoomTypeName() : null) +
                ", userName='" + userName + '\'' +
                ", checkInDate='" + checkInDate + '\'' +
                ", checkOutDate='" + checkOutDate + '\'' +
                ", totalFee=" + totalFee +
                ", depositAmount=" + depositAmount +
                ", orderStatus='" + orderStatus + '\'' +
                '}';
    }
}
