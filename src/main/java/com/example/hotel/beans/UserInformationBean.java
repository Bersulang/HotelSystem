package com.example.hotel.beans;

// 用于封装确认页面提交的入住人信息
public class UserInformationBean {
    private String guestName; // 入住人姓名
    private String contactNumber; // 联系方式
    private String specialRequests; // 特殊需求 (可能与BookingDetailsBean中的重复，但这是用户直接输入的)

    public UserInformationBean() {
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

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    @Override
    public String toString() {
        return "UserInformationBean{" +
                "guestName='" + guestName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", specialRequests='" + specialRequests + '\'' +
                '}';
    }
} 