package com.example.hotel.dao;

import com.example.hotel.beans.RoomBean;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminRoomDAO {
    private static final Logger logger = Logger.getLogger(AdminRoomDAO.class.getName());
    
    // 获取所有房间信息
    public List<RoomBean> getAllRooms() {
        List<RoomBean> rooms = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionUtil.getConnection();            String query = "SELECT * FROM rooms ORDER BY hotel_name, room_type_name";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                RoomBean room = mapResultSetToRoomBean(rs);
                rooms.add(room);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "获取所有房间信息失败", e);
        } finally {
            DBConnectionUtil.closeConnection(conn, ps, rs);
        }
        
        return rooms;
    }
    
    // 通过ID获取房间信息
    public RoomBean getRoomById(String roomId) {
        RoomBean room = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionUtil.getConnection();            String query = "SELECT * FROM rooms WHERE room_id = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, roomId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                room = mapResultSetToRoomBean(rs);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "根据ID获取房间信息失败: " + roomId, e);
        } finally {
            DBConnectionUtil.closeConnection(conn, ps, rs);
        }
        
        return room;
    }
    
    // 添加新房间
    public boolean addRoom(RoomBean room) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnectionUtil.getConnection();            String query = "INSERT INTO rooms " +
                    "(room_id, hotel_name, hotel_star_rating, hotel_location, hotel_description, " +
                    "hotel_contact, hotel_transport_guide, room_type_name, real_time_stock, price_per_night, " +
                    "promotional_price, room_facilities_list, room_description, area, bed_type, maxOccupancy) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
            
            ps.setString(1, room.getRoomId());
            ps.setString(2, room.getHotelName());
            ps.setInt(3, room.getHotelStarRating());
            ps.setString(4, room.getHotelLocation());
            ps.setString(5, room.getHotelDescription());
            ps.setString(6, room.getHotelContact());
            ps.setString(7, room.getHotelTransportGuide());
            ps.setString(8, room.getRoomTypeName());
            ps.setInt(9, room.getRealTimeStock());
            ps.setDouble(10, room.getPricePerNight());
            ps.setDouble(11, room.getPromotionalPrice());
            ps.setString(12, room.getRoomFacilitiesList());
            ps.setString(13, room.getRoomDescription());
            ps.setDouble(14, room.getArea());
            ps.setString(15, room.getBedType());
            ps.setInt(16, room.getMaxOccupancy());
            
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "添加房间失败", e);
            return false;
        } finally {
            DBConnectionUtil.closeConnection(conn, ps);
        }
    }
    
    // 更新房间信息
    public boolean updateRoom(RoomBean room, String originalRoomId) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnectionUtil.getConnection();            String query = "UPDATE rooms SET " +
                    "room_id = ?, hotel_name = ?, hotel_star_rating = ?, hotel_location = ?, " +
                    "hotel_description = ?, hotel_contact = ?, hotel_transport_guide = ?, " +
                    "room_type_name = ?, real_time_stock = ?, price_per_night = ?, " +
                    "promotional_price = ?, room_facilities_list = ?, room_description = ?, " +
                    "area = ?, bed_type = ?, maxOccupancy = ? " +
                    "WHERE room_id = ?";
            ps = conn.prepareStatement(query);
            
            ps.setString(1, room.getRoomId());
            ps.setString(2, room.getHotelName());
            ps.setInt(3, room.getHotelStarRating());
            ps.setString(4, room.getHotelLocation());
            ps.setString(5, room.getHotelDescription());
            ps.setString(6, room.getHotelContact());
            ps.setString(7, room.getHotelTransportGuide());
            ps.setString(8, room.getRoomTypeName());
            ps.setInt(9, room.getRealTimeStock());
            ps.setDouble(10, room.getPricePerNight());
            ps.setDouble(11, room.getPromotionalPrice());
            ps.setString(12, room.getRoomFacilitiesList());
            ps.setString(13, room.getRoomDescription());
            ps.setDouble(14, room.getArea());
            ps.setString(15, room.getBedType());
            ps.setInt(16, room.getMaxOccupancy());
            ps.setString(17, originalRoomId);
            
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "更新房间失败: " + originalRoomId, e);
            return false;
        } finally {
            DBConnectionUtil.closeConnection(conn, ps);
        }
    }
    
    // 删除房间
    public boolean deleteRoom(String roomId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionUtil.getConnection();
              // 检查是否有关联的预订
            String checkQuery = "SELECT COUNT(*) FROM bookings WHERE booked_room_id = ?";
            ps = conn.prepareStatement(checkQuery);
            ps.setString(1, roomId);
            rs = ps.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                // 有关联的预订，不能删除
                return false;
            }
            
            // 没有关联的预订，可以删除
            ps.close();
            rs.close();
              String deleteQuery = "DELETE FROM rooms WHERE room_id = ?";
            ps = conn.prepareStatement(deleteQuery);
            ps.setString(1, roomId);
            
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "删除房间失败: " + roomId, e);
            return false;
        } finally {
            DBConnectionUtil.closeConnection(conn, ps, rs);
        }
    }
    
    // 从ResultSet映射到RoomBean
    private RoomBean mapResultSetToRoomBean(ResultSet rs) throws SQLException {
        RoomBean room = new RoomBean();
          room.setRoomId(rs.getString("room_id"));
        room.setHotelName(rs.getString("hotel_name"));
        room.setHotelStarRating(rs.getInt("hotel_star_rating"));
        room.setHotelLocation(rs.getString("hotel_location"));
        room.setHotelDescription(rs.getString("hotel_description"));
        room.setHotelContact(rs.getString("hotel_contact"));
        room.setHotelTransportGuide(rs.getString("hotel_transport_guide"));
        room.setRoomTypeName(rs.getString("room_type_name"));
        room.setRealTimeStock(rs.getInt("real_time_stock"));
        room.setPricePerNight(rs.getDouble("price_per_night"));
        room.setPromotionalPrice(rs.getDouble("promotional_price"));
        room.setRoomFacilitiesList(rs.getString("room_facilities_list"));
        room.setRoomDescription(rs.getString("room_description"));        room.setArea(rs.getDouble("area"));
        room.setBedType(rs.getString("bed_type"));
        room.setMaxOccupancy(rs.getInt("maxOccupancy"));
        
        return room;
    }
}