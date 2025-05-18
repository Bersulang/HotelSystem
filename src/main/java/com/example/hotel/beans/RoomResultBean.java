package com.example.hotel.beans;

import java.util.List;

// 房态信息,  房型详情, 酒店信息
public class RoomResultBean {
    // 唯一标识符，用于选择
    private String roomId;

    //酒店信息
    private String hotelName;
    private int hotelStarRating;

    private String hotelLocation;
    private String hotelDescription;
    private String hotelContact;
    private String hotelTransportGuide;

    // 房型与房态信息
    private String roomTypeName;
    private int realTimeStock; // 剩余房间
    private double pricePerNight; // 每晚价格
    private double promotionalPrice; // 促销价
    private List<String> roomFacilitiesList; // 房间设施列表 (如智能门锁)
    private List<String> availableDateRanges; // 可订日期段 (简化为字符串列表)

    //房型详情
    private String roomDescription;
    private double area; // 面积
    private String bedType; // 床型
    private int maxOccupancy; // 最大入住人数

    public RoomResultBean() {
    }

    public RoomResultBean(String roomId, String hotelName, String roomTypeName, int realTimeStock, double pricePerNight, String roomDescription, List<String> roomFacilitiesList) {
        this.roomId = roomId;
        this.hotelName = hotelName;
        this.roomTypeName = roomTypeName;
        this.realTimeStock = realTimeStock;
        this.pricePerNight = pricePerNight;
        this.roomDescription = roomDescription;
        this.roomFacilitiesList = roomFacilitiesList;
    }



    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public int getHotelStarRating() {
        return hotelStarRating;
    }

    public void setHotelStarRating(int hotelStarRating) {
        this.hotelStarRating = hotelStarRating;
    }

    public String getHotelLocation() {
        return hotelLocation;
    }

    public void setHotelLocation(String hotelLocation) {
        this.hotelLocation = hotelLocation;
    }

    public String getHotelDescription() {
        return hotelDescription;
    }

    public void setHotelDescription(String hotelDescription) {
        this.hotelDescription = hotelDescription;
    }

    public String getHotelContact() {
        return hotelContact;
    }

    public void setHotelContact(String hotelContact) {
        this.hotelContact = hotelContact;
    }

    public String getHotelTransportGuide() {
        return hotelTransportGuide;
    }

    public void setHotelTransportGuide(String hotelTransportGuide) {
        this.hotelTransportGuide = hotelTransportGuide;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public int getRealTimeStock() {
        return realTimeStock;
    }

    public void setRealTimeStock(int realTimeStock) {
        this.realTimeStock = realTimeStock;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public double getPromotionalPrice() {
        return promotionalPrice;
    }

    public void setPromotionalPrice(double promotionalPrice) {
        this.promotionalPrice = promotionalPrice;
    }

    public List<String> getRoomFacilitiesList() {
        return roomFacilitiesList;
    }

    public void setRoomFacilitiesList(List<String> roomFacilitiesList) {
        this.roomFacilitiesList = roomFacilitiesList;
    }

    public List<String> getAvailableDateRanges() {
        return availableDateRanges;
    }

    public void setAvailableDateRanges(List<String> availableDateRanges) {
        this.availableDateRanges = availableDateRanges;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    @Override
    public String toString() {
        return "RoomResultBean{" +
                "roomId='" + roomId + '\'' +
                ", hotelName='" + hotelName + '\'' +
                ", roomTypeName='" + roomTypeName + '\'' +
                ", realTimeStock=" + realTimeStock +
                ", pricePerNight=" + pricePerNight +
                '}';
    }
} 