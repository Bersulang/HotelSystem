package com.example.hotel.beans;

public class RoomBean {
    private String roomId;
    private String hotelName;
    private int hotelStarRating;
    private String hotelLocation;
    private String hotelDescription;
    private String hotelContact;
    private String hotelTransportGuide;
    private String roomTypeName;
    private int realTimeStock;
    private double pricePerNight;
    private double promotionalPrice;
    private String roomFacilitiesList; // Comma-separated
    private String roomDescription;
    private double area;
    private String bedType;
    private int maxOccupancy;

    public RoomBean() {
    }

    // Getters and Setters
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

    public String getRoomFacilitiesList() {
        return roomFacilitiesList;
    }

    public void setRoomFacilitiesList(String roomFacilitiesList) {
        this.roomFacilitiesList = roomFacilitiesList;
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
        return "RoomBean{" +
                "roomId='" + roomId + "\'" +
                ", hotelName='" + hotelName + "\'" +
                ", roomTypeName='" + roomTypeName + "\'" +
                ", pricePerNight=" + pricePerNight +
                ", realTimeStock=" + realTimeStock +
                '}';
    }
}
