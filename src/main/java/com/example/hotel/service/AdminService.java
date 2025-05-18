package com.example.hotel.service;

import com.example.hotel.beans.BookingDetailsBean;
import com.example.hotel.beans.RoomBean;
import com.example.hotel.beans.UserBean;
import com.example.hotel.dao.AdminBookingDAO;
import com.example.hotel.dao.AdminRoomDAO;
import com.example.hotel.dao.AdminUserDAO;

import java.util.List;

public class AdminService {
    private AdminRoomDAO roomDAO;
    private AdminBookingDAO bookingDAO;
    private AdminUserDAO userDAO;
    
    public AdminService() {
        roomDAO = new AdminRoomDAO();
        bookingDAO = new AdminBookingDAO();
        userDAO = new AdminUserDAO();
    }
    
    // 房间管理相关方法
    public List<RoomBean> getAllRooms() {
        return roomDAO.getAllRooms();
    }
    
    public RoomBean getRoomById(String roomId) {
        return roomDAO.getRoomById(roomId);
    }
    
    public boolean addRoom(RoomBean room) {
        return roomDAO.addRoom(room);
    }
    
    public boolean updateRoom(RoomBean room, String originalRoomId) {
        return roomDAO.updateRoom(room, originalRoomId);
    }
    
    public boolean deleteRoom(String roomId) {
        return roomDAO.deleteRoom(roomId);
    }
    
    // 预订管理相关方法
    public List<BookingDetailsBean> getAllBookings() {
        return bookingDAO.getAllBookings();
    }
    
    public BookingDetailsBean getBookingById(int bookingId) {
        return bookingDAO.getBookingById(bookingId);
    }
    
    public List<BookingDetailsBean> getBookingsByUsername(String userName) {
        return bookingDAO.getBookingsByUsername(userName);
    }
    
    public boolean updateBookingStatus(int bookingId, String status) {
        return bookingDAO.updateBookingStatus(bookingId, status);
    }
    
    public boolean deleteBooking(int bookingId) {
        return bookingDAO.deleteBooking(bookingId);
    }
    
    // 用户管理相关方法
    public List<UserBean> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    public UserBean getUserByUsername(String userName) {
        return userDAO.getUserByUsername(userName);
    }
    
    public boolean addUser(UserBean user) {
        return userDAO.addUser(user);
    }
    
    public boolean updateUser(UserBean user) {
        return userDAO.updateUser(user);
    }
    
    public boolean updateUserPassword(String userName, String newPassword) {
        return userDAO.updateUserPassword(userName, newPassword);
    }
    
    public boolean deleteUser(String userName) {
        return userDAO.deleteUser(userName);
    }
}