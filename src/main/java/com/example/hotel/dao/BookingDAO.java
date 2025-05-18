    package com.example.hotel.dao;

import com.example.hotel.beans.FinalOrderBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** 对应交互数据库中的订单信息表*/
public class BookingDAO {

    public boolean saveBooking(FinalOrderBean order) {        System.out.println("BookingDAO: saveBooking called for order: " + order.getOrderId());
        Connection conn = null;
        PreparedStatement pstmt = null;        String sql = "INSERT INTO bookings (order_id, room_type_name, hotel_name, check_in_date, check_out_date, " +
                "guest_name, contact_number, total_fee, deposit_amount_paid, payment_method, transaction_id, " +
                "order_status, booking_timestamp, virtual_room_number, nfc_key, booked_room_id, special_requests) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = DBConnectionUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, order.getOrderId());
            pstmt.setString(2, order.getRoomTypeName());
            pstmt.setString(3, order.getHotelName());

            //转换日期类型放入数据库
            if (order.getCheckInDate() != null) {
                pstmt.setDate(4, java.sql.Date.valueOf(LocalDate.parse(order.getCheckInDate())));
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }
            if (order.getCheckOutDate() != null) {
                pstmt.setDate(5, java.sql.Date.valueOf(LocalDate.parse(order.getCheckOutDate())));
            } else {
                pstmt.setNull(5, java.sql.Types.DATE);
            }

            pstmt.setString(6, order.getGuestName());
            pstmt.setString(7, order.getContactNumber());
            pstmt.setDouble(8, order.getTotalFee());
            pstmt.setDouble(9, order.getDepositAmountPaid());
            pstmt.setString(10, order.getPaymentMethod());
            pstmt.setString(11, order.getTransactionId());
            pstmt.setString(12, order.getOrderStatus());            // 设置订单时间
            Timestamp timestamp;
            if (order.getBookingTimestamp() != null) {
                timestamp = Timestamp.valueOf(order.getBookingTimestamp());
            } else {
                timestamp = Timestamp.valueOf(LocalDateTime.now());
            }
            pstmt.setTimestamp(13, timestamp);

            pstmt.setString(14, order.getVirtualRoomNumber());
            pstmt.setString(15, order.getNfcKey());
            pstmt.setString(16, order.getBookedRoomId());
            pstmt.setString(17, order.getSpecialRequests());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("BookingDAO: Booking saved successfully to DB for order ID: " + order.getOrderId());
                return true;
            } else {
                System.out.println("BookingDAO: Booking save failed for order ID: " + order.getOrderId());
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("BookingDAO: SQL Error saving booking: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("BookingDAO: Error saving booking: " + e.getMessage());
            return false;
        } finally {
            DBConnectionUtil.closeConnection(conn, pstmt);
        }
    }
} 