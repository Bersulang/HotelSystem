package com.example.hotel.dao;

import com.example.hotel.beans.BookingDetailsBean;
import com.example.hotel.beans.RoomResultBean;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminBookingDAO {
    private static final Logger logger = Logger.getLogger(AdminBookingDAO.class.getName());
    
    // 获取所有预订信息
    public List<BookingDetailsBean> getAllBookings() {
        List<BookingDetailsBean> bookings = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionUtil.getConnection();
            String query = "SELECT b.*, r.hotel_name, r.room_type_name, r.price_per_night, u.fullname "
                    + "FROM bookings b "
                    + "JOIN rooms r ON b.booked_room_id = r.room_id "
                    + "JOIN users u ON b.guest_name = u.username "
                    + "ORDER BY b.booking_timestamp DESC";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                BookingDetailsBean booking = mapResultSetToBookingBean(rs);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "获取所有预订信息失败", e);
        } finally {
            DBConnectionUtil.closeConnection(conn, ps, rs);
        }
        
        return bookings;
    }
    
    // 根据ID获取预订信息
    public BookingDetailsBean getBookingById(int bookingId) {
        BookingDetailsBean booking = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionUtil.getConnection();
            String query = "SELECT b.*, r.hotel_name, r.room_type_name, r.price_per_night, u.fullname "
                    + "FROM bookings b "
                    + "JOIN rooms r ON b.booked_room_id = r.room_id "
                    + "JOIN users u ON b.guest_name = u.username "
                    + "WHERE b.booking_id = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, bookingId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                booking = mapResultSetToBookingBean(rs);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "根据ID获取预订信息失败: " + bookingId, e);
        } finally {
            DBConnectionUtil.closeConnection(conn, ps, rs);
        }
        
        return booking;
    }
    
    // 根据用户名获取该用户的所有预订
    public List<BookingDetailsBean> getBookingsByUsername(String userName) {
        List<BookingDetailsBean> bookings = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionUtil.getConnection();
            String query = "SELECT b.*, r.hotel_name, r.room_type_name, r.price_per_night, u.fullname "
                    + "FROM bookings b "
                    + "JOIN rooms r ON b.booked_room_id = r.room_id "
                    + "JOIN users u ON b.guest_name = u.username "
                    + "WHERE b.guest_name = ? "
                    + "ORDER BY b.booking_timestamp DESC";
            ps = conn.prepareStatement(query);
            ps.setString(1, userName);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                BookingDetailsBean booking = mapResultSetToBookingBean(rs);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "根据用户名获取预订信息失败: " + userName, e);
        } finally {
            DBConnectionUtil.closeConnection(conn, ps, rs);
        }
        
        return bookings;
    }
    
    // 更新预订状态
    public boolean updateBookingStatus(int bookingId, String status) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnectionUtil.getConnection();
            String query = "UPDATE bookings SET order_status = ? WHERE booking_id = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "更新预订状态失败: " + bookingId, e);
            return false;
        } finally {
            DBConnectionUtil.closeConnection(conn, ps);
        }
    }
    
    // 删除预订
    public boolean deleteBooking(int bookingId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionUtil.getConnection();
            // 首先获取预订信息，以便稍后更新房间库存
            String getBookingQuery = "SELECT b.*, r.* FROM bookings b JOIN rooms r ON b.booked_room_id = r.room_id WHERE b.booking_id = ?";
            ps = conn.prepareStatement(getBookingQuery);
            ps.setInt(1, bookingId);
            rs = ps.executeQuery();
            
            if (!rs.next()) {
                return false; // 预订不存在
            }
            
            String roomId = rs.getString("room_id");
            int numberOfRooms = rs.getInt("number_of_rooms");
            
            rs.close();
            ps.close();
            // 删除预订
            String deleteQuery = "DELETE FROM bookings WHERE booking_id = ?";
            ps = conn.prepareStatement(deleteQuery);
            ps.setInt(1, bookingId);
            int result = ps.executeUpdate();
            
            if (result > 0) {
                // 更新房间库存
                ps.close();
                String updateStockQuery = "UPDATE rooms SET real_time_stock = real_time_stock + ? WHERE room_id = ?";
                ps = conn.prepareStatement(updateStockQuery);
                ps.setInt(1, numberOfRooms);
                ps.setString(2, roomId);
                ps.executeUpdate();
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "删除预订失败: " + bookingId, e);
            return false;
        } finally {
            DBConnectionUtil.closeConnection(conn, ps, rs);
        }
    }
      // 从ResultSet映射到BookingDetailsBean
    private BookingDetailsBean mapResultSetToBookingBean(ResultSet rs) throws SQLException {
        BookingDetailsBean booking = new BookingDetailsBean();
        
        // 设置基本信息
        booking.setBookingId(rs.getInt("booking_id"));
        booking.setOrderId(rs.getString("order_id"));
        booking.setUserName(rs.getString("guest_name"));
        
        // 设置客户姓名 (从User表的fullname字段)
        String customerName = rs.getString("fullname");
        if (customerName != null && !customerName.isEmpty()) {
            booking.setContactName(customerName);
        }
        
        // 创建并设置房间信息
        RoomResultBean room = new RoomResultBean();
        room.setRoomId(rs.getString("booked_room_id"));
        room.setHotelName(rs.getString("hotel_name"));
        room.setRoomTypeName(rs.getString("room_type_name"));
        room.setPricePerNight(rs.getDouble("price_per_night"));
        booking.setSelectedRoom(room);
        
        booking.setCheckInDate(rs.getDate("check_in_date").toString());
        booking.setCheckOutDate(rs.getDate("check_out_date").toString());
        booking.setTotalFee(rs.getDouble("total_fee"));
        
        // 特殊需求
        booking.setSpecialRequests(rs.getString("special_requests"));
        
        // 订单状态
        booking.setOrderStatus(rs.getString("order_status"));
        
        // 设置预订日期
        Timestamp bookingTimestamp = rs.getTimestamp("booking_timestamp");
        if (bookingTimestamp != null) {
            booking.setBookingDate(new java.util.Date(bookingTimestamp.getTime()));
        }
        
        // 设置联系人信息 - 尝试从各个可能的字段中获取
        try {
            String contactName = rs.getString("contact_name");
            if (contactName != null && !contactName.isEmpty()) {
                booking.setContactName(contactName);
            }
            
            booking.setContactPhone(rs.getString("contact_number"));
            String contactEmail = rs.getString("contact_email");
            if (contactEmail != null) {
                booking.setContactEmail(contactEmail);
            }
            
            // 联系信息可以组合
            StringBuilder contactInfoBuilder = new StringBuilder();
            contactInfoBuilder.append("联系人: ").append(booking.getContactName() != null ? booking.getContactName() : customerName);
            
            String phone = rs.getString("contact_number");
            if (phone != null && !phone.isEmpty()) {
                contactInfoBuilder.append(" | 电话: ").append(phone);
            }
            
            if (contactEmail != null && !contactEmail.isEmpty()) {
                contactInfoBuilder.append(" | 邮箱: ").append(contactEmail);
            }
            
            booking.setContactInfo(contactInfoBuilder.toString());
        } catch (SQLException e) {
            // 如果没有这些字段，使用默认值
            booking.setContactInfo("联系人: " + customerName);
        }
        
        // 设置其他信息
        try {
            booking.setNumberOfRooms(rs.getInt("number_of_rooms"));
        } catch (SQLException e) {
            booking.setNumberOfRooms(1); // 默认值
        }
        
        try {
            booking.setPaymentMethod(rs.getString("payment_method"));
        } catch (SQLException e) {
            // 忽略
        }
        
        return booking;
    }
}
