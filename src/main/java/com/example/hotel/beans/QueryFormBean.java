package com.example.hotel.beans;

import java.util.List;

//查询条件
public class QueryFormBean {
    private String hotelName;
    private String location;
    private String roomType;
    private Double priceRangeMin;
    private Double priceRangeMax;
    private String checkInDate; // 格式 yyyy-MM-dd
    private String checkOutDate; //
    private List<String> facilities; //
    private Integer starRating; // 酒店星级评分

    public QueryFormBean() {
    }


    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Double getPriceRangeMin() {
        return priceRangeMin;
    }

    public void setPriceRangeMin(Double priceRangeMin) {
        this.priceRangeMin = priceRangeMin;
    }

    public Double getPriceRangeMax() {
        return priceRangeMax;
    }

    public void setPriceRangeMax(Double priceRangeMax) {
        this.priceRangeMax = priceRangeMax;
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

    public List<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities;
    }

    public Integer getStarRating() {
        return starRating;
    }

    public void setStarRating(Integer starRating) {
        this.starRating = starRating;
    }

    @Override
    public String toString() {
        return "QueryFormBean{" +
                "hotelName='" + hotelName + '\'' +
                ", location='" + location + '\'' +
                ", roomType='" + roomType + '\'' +
                ", priceRangeMin=" + priceRangeMin +
                ", priceRangeMax=" + priceRangeMax +
                ", checkInDate='" + checkInDate + '\'' +
                ", checkOutDate='" + checkOutDate + '\'' +
                ", facilities=" + facilities +
                ", starRating=" + starRating +
                '}';
    }
} 