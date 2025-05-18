package com.example.hotel.dao;

import com.example.hotel.beans.QueryFormBean;
import com.example.hotel.beans.RoomResultBean;
import com.example.hotel.beans.RoomBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/** 对应交互数据库中的酒店房间信息表*/
public class RoomDAO {
    /**
     * 获取所有房间
     */
    public List<RoomBean> getAllRooms() {
        List<RoomBean> rooms = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionUtil.getConnection();
            String sql = "SELECT * FROM rooms";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                RoomBean room = mapResultSetToRoomBean(rs);
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnectionUtil.closeConnection(conn, pstmt, rs);
        }
        
        return rooms;
    }
    
    /**
     * 通过ID获取房间
     */
    public RoomBean getRoomById(String roomId) {
        RoomBean room = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnectionUtil.getConnection();
            String sql = "SELECT * FROM rooms WHERE room_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                room = mapResultSetToRoomBean(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnectionUtil.closeConnection(conn, pstmt, rs);
        }
        
        return room;
    }
    
    /**
     * 创建新房间
     */
    public boolean createRoom(RoomBean room) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnectionUtil.getConnection();            String sql = "INSERT INTO rooms (room_id, hotel_name, hotel_star_rating, hotel_location, hotel_description, " +
                    "hotel_contact, hotel_transport_guide, room_type_name, real_time_stock, price_per_night, " +
                    "promotional_price, room_facilities_list, room_description, area, bed_type, maxOccupancy) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            
            // 如果房间ID为空，生成一个新ID
            if (room.getRoomId() == null || room.getRoomId().isEmpty()) {
                room.setRoomId("R_" + UUID.randomUUID().toString().substring(0, 8));
            }
            
            pstmt.setString(1, room.getRoomId());
            pstmt.setString(2, room.getHotelName());
            pstmt.setInt(3, room.getHotelStarRating());
            pstmt.setString(4, room.getHotelLocation());
            pstmt.setString(5, room.getHotelDescription());
            pstmt.setString(6, room.getHotelContact());
            pstmt.setString(7, room.getHotelTransportGuide());
            pstmt.setString(8, room.getRoomTypeName());
            pstmt.setInt(9, room.getRealTimeStock());
            pstmt.setDouble(10, room.getPricePerNight());
            pstmt.setDouble(11, room.getPromotionalPrice());
            pstmt.setString(12, room.getRoomFacilitiesList());
            pstmt.setString(13, room.getRoomDescription());
            pstmt.setDouble(14, room.getArea());
            pstmt.setString(15, room.getBedType());
            pstmt.setInt(16, room.getMaxOccupancy());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnectionUtil.closeConnection(conn, pstmt);
        }
    }
    
    /**
     * 更新房间信息
     */
    public boolean updateRoom(RoomBean room) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnectionUtil.getConnection();            String sql = "UPDATE rooms SET hotel_name = ?, hotel_star_rating = ?, hotel_location = ?, hotel_description = ?, " +
                    "hotel_contact = ?, hotel_transport_guide = ?, room_type_name = ?, real_time_stock = ?, price_per_night = ?, " +
                    "promotional_price = ?, room_facilities_list = ?, room_description = ?, area = ?, bed_type = ?, maxOccupancy = ? " +
                    "WHERE room_id = ?";
            
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, room.getHotelName());
            pstmt.setInt(2, room.getHotelStarRating());
            pstmt.setString(3, room.getHotelLocation());
            pstmt.setString(4, room.getHotelDescription());
            pstmt.setString(5, room.getHotelContact());
            pstmt.setString(6, room.getHotelTransportGuide());
            pstmt.setString(7, room.getRoomTypeName());
            pstmt.setInt(8, room.getRealTimeStock());
            pstmt.setDouble(9, room.getPricePerNight());
            pstmt.setDouble(10, room.getPromotionalPrice());
            pstmt.setString(11, room.getRoomFacilitiesList());
            pstmt.setString(12, room.getRoomDescription());
            pstmt.setDouble(13, room.getArea());
            pstmt.setString(14, room.getBedType());
            pstmt.setInt(15, room.getMaxOccupancy());
            pstmt.setString(16, room.getRoomId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnectionUtil.closeConnection(conn, pstmt);
        }
    }
    
    /**
     * 删除房间
     */
    public boolean deleteRoom(String roomId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnectionUtil.getConnection();
            String sql = "DELETE FROM rooms WHERE room_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnectionUtil.closeConnection(conn, pstmt);
        }
    }
    
    /**
     * 将 ResultSet 映射到 RoomBean 对象
     */
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
        room.setRoomDescription(rs.getString("room_description"));
        room.setArea(rs.getDouble("area"));
        room.setBedType(rs.getString("bed_type"));
        room.setMaxOccupancy(rs.getInt("maxOccupancy"));
        return room;
    }
    
    /** 根据前端返回的条件查找房间 */
    public List<RoomResultBean> findRooms(QueryFormBean query) {
        System.out.println("RoomDAO: findRooms called with query: " + query);
        List<RoomResultBean> rooms = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;  //防止sql注入
        ResultSet rs = null;

        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM rooms WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (query.getLocation() != null && !query.getLocation().isEmpty()) {
            sqlBuilder.append(" AND hotel_location LIKE ?");
            params.add("%" + query.getLocation() + "%");
        }
        if (query.getRoomType() != null && !query.getRoomType().isEmpty()) {
            sqlBuilder.append(" AND room_type_name LIKE ?"); // Assuming room_type_name is the target
            params.add("%" + query.getRoomType() + "%");
        }
        if (query.getPriceRangeMin() != null) {
            sqlBuilder.append(" AND price_per_night >= ?");
            params.add(query.getPriceRangeMin());
        }
        if (query.getPriceRangeMax() != null && query.getPriceRangeMax() > 0) {
            sqlBuilder.append(" AND price_per_night <= ?");
            params.add(query.getPriceRangeMax());
        }
        if (query.getStarRating() != null && query.getStarRating() > 0) {
            sqlBuilder.append(" AND hotel_star_rating >= ?");
            params.add(query.getStarRating());
        }
        // 为简单起见，本例未直接按日期范围进行过滤，还可基于beans中的封装方法写更多自己想要的条件
        sqlBuilder.append(" AND real_time_stock > 0");


        try {
            conn = DBConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sqlBuilder.toString());

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            System.out.println("Executing SQL: " + pstmt.toString());

            rs = pstmt.executeQuery();
            while (rs.next()) {
                RoomResultBean room = mapRowToRoomResultBean(rs);
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace(); //适当添加适当添加
        } finally {
            DBConnectionUtil.closeConnection(conn, pstmt, rs);
        }
        System.out.println("RoomDAO: Returning " + rooms.size() + " rooms from DB.");
        return rooms;
    }

         /**根据房间号查找房间 */
    public RoomResultBean findRoomById(String roomId) {
        System.out.println("RoomDAO: findRoomById called with roomId: " + roomId);
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM rooms WHERE room_id = ?";
        RoomResultBean room = null;

        try {
            conn = DBConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                room = mapRowToRoomResultBean(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnectionUtil.closeConnection(conn, pstmt, rs);
        }
        return room;
    }

            /** 更新房间表*/
    public boolean updateRoomStock(String roomId, int quantityToReduce) {
        System.out.println("RoomDAO: updateRoomStock called for roomId: " + roomId + ", reduce by: " + quantityToReduce);
        Connection conn = null;
        PreparedStatement pstmt = null;
        // 确保库存不会因更新而降至零以下
        String sql = "UPDATE rooms SET real_time_stock = real_time_stock - ? WHERE room_id = ? AND real_time_stock >= ?";
        try {
            conn = DBConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quantityToReduce);
            pstmt.setString(2, roomId);
            pstmt.setInt(3, quantityToReduce);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("RoomDAO: Stock update successful for " + roomId);
                return true;
            } else {
                System.out.println("RoomDAO: Stock update failed for " + roomId + " (insufficient stock or room not found)");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnectionUtil.closeConnection(conn, pstmt, null);
        }
    }
/** 负责从 ResultSet当前行读取数据，并填充一个新的 RoomResultBean 对象。*/
    private RoomResultBean mapRowToRoomResultBean(ResultSet rs) throws SQLException {
        RoomResultBean room = new RoomResultBean();
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

        String facilities = rs.getString("room_facilities_list");
        if (facilities != null && !facilities.isEmpty()) {
            room.setRoomFacilitiesList(Arrays.asList(facilities.split("\s*,\s*")));
        } else {
            room.setRoomFacilitiesList(new ArrayList<>());
        }

         /*  available_date_ranges 在表中被注释掉了，所以这里没有映射。
         如果添加了，需要解析：room.setAvailableDateRanges(parseDateRanges(rs.getString("available_date_ranges")));*/        room.setRoomDescription(rs.getString("room_description"));
        room.setArea(rs.getDouble("area"));
        room.setBedType(rs.getString("bed_type"));
        room.setMaxOccupancy(rs.getInt("maxOccupancy"));
        return room;
    }
} 