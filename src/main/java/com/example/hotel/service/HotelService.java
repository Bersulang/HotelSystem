package com.example.hotel.service;

import com.example.hotel.beans.*;
import com.example.hotel.dao.BookingDAO;
import com.example.hotel.dao.RoomDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HotelService {
    private RoomDAO roomDAO;
    private BookingDAO bookingDAO;

    public HotelService() {
        this.roomDAO = new RoomDAO();
        this.bookingDAO = new BookingDAO();
    }

    public List<RoomResultBean> searchAvailableRooms(QueryFormBean query) {
        System.out.println("HotelService: searchAvailableRooms called with query: " + query);
        // 在这里可以加入更复杂的逻辑，例如根据促销规则调整价格或推荐
        // 目前直接调用DAO
        List<RoomResultBean> rooms = roomDAO.findRooms(query);
        // 可以在此处理促销规则，例如早鸟优惠
        // if (query.getCheckInDate() != null) { 巴拉巴拉 }
        return rooms;
    }

    public BookingDetailsBean prepareBookingDetails(String roomId, QueryFormBean queryContext) {
        System.out.println("HotelService: prepareBookingDetails for roomId: " + roomId);
        RoomResultBean selectedRoom = roomDAO.findRoomById(roomId);
        if (selectedRoom == null) {
            return null; // 房间不存在
        }

        BookingDetailsBean details = new BookingDetailsBean();
        details.setSelectedRoom(selectedRoom);
        details.setCheckInDate(queryContext.getCheckInDate());
        details.setCheckOutDate(queryContext.getCheckOutDate());
        
        // 当前登录用户
        details.setUserName("User123");
        details.setContactInfo("12138");

        // 计算总价 (基于每晚价格和入住天数)
        long nights = calculateNights(queryContext.getCheckInDate(), queryContext.getCheckOutDate());
        if (nights <= 0) {
            // 非法日期，或应在Servlet层更早处理
            System.err.println("HotelService: Invalid date range for booking.");
            return null; 
        }
        details.setNumberOfNights((int)nights);

        double priceToUse = selectedRoom.getPromotionalPrice() > 0 ? selectedRoom.getPromotionalPrice() : selectedRoom.getPricePerNight();
        double totalFee = priceToUse * nights;
        
        // 优惠：提前7天减15%
        if (isEarlyBird(queryContext.getCheckInDate(), 7)) {
            totalFee *= 0.85;
            System.out.println("HotelService: Applied early bird discount.");
        }
        details.setTotalFee(totalFee);


        double depositAmount = totalFee * 0.08; // 定金比例为总费用的8%
        details.setDepositAmount(depositAmount);

        details.setOrderStatus("待确认");
        details.setCancellationPolicy("标准取消政策：入住前3天可免费取消，否则将收取首晚房费。");
        details.setOrderId("TEMP-" + UUID.randomUUID().toString().substring(0, 8)); // 临时订单号

        System.out.println("HotelService: BookingDetails prepared: " + details);
        return details;
    }

    private boolean isEarlyBird(String checkInDateStr, int daysInAdvance) {
        if (checkInDateStr == null || checkInDateStr.isEmpty()) return false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date checkIn = sdf.parse(checkInDateStr);
            Date today = new Date(); // 当前日期
            long diffInMillis = checkIn.getTime() - today.getTime();
            long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            return diffInDays >= daysInAdvance;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private long calculateNights(String checkInStr, String checkOutStr) {
        if (checkInStr == null || checkOutStr == null || checkInStr.isEmpty() || checkOutStr.isEmpty()) return 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date checkInDate = sdf.parse(checkInStr);
            Date checkOutDate = sdf.parse(checkOutStr);
            if (checkOutDate.before(checkInDate) || checkOutDate.equals(checkInDate)) {
                return 0; // 退房日期必须在入住日期之后
            }
            long diff = checkOutDate.getTime() - checkInDate.getTime();
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }    public FinalOrderBean finalizeBooking(BookingDetailsBean details, UserInformationBean userInfo, PaymentInfoBean paymentInfo) {
        System.out.println("HotelService: finalizeBooking for orderId: " + details.getOrderId());
        
        // 检查用户名是否有效，如果无效可能是未登录用户
        if (details.getUserName() == null || details.getUserName().trim().isEmpty()) {
            System.err.println("HotelService: Invalid or missing username, user may not be logged in.");
            return null; // 用户未登录或无效
        }

        if (!"成功".equals(paymentInfo.getPaymentStatus())) {
            System.err.println("HotelService: Payment not successful, cannot finalize booking.");
            return null; // 支付未成功
        }

        FinalOrderBean finalOrder = new FinalOrderBean();
        finalOrder.setOrderId("ORD-" + UUID.randomUUID().toString().toUpperCase().substring(0, 10)); // 正式订单号
        finalOrder.setHotelName(details.getSelectedRoom().getHotelName());
        finalOrder.setRoomTypeName(details.getSelectedRoom().getRoomTypeName());
        finalOrder.setCheckInDate(details.getCheckInDate());
        finalOrder.setCheckOutDate(details.getCheckOutDate());
        // 从 selectedRoom 获取酒店位置和联系方式
        if (details.getSelectedRoom() != null) {        finalOrder.setHotelLocation(details.getSelectedRoom().getHotelLocation());
            finalOrder.setHotelContact(details.getSelectedRoom().getHotelContact());
        }
        // 使用BookingDetailsBean中的userName (登录用户名)，而不是表单中的guestName
        finalOrder.setGuestName(details.getUserName()); // 这将使用当前登录用户名
        finalOrder.setContactNumber(userInfo.getContactNumber());
        finalOrder.setTotalFee(details.getTotalFee());
        finalOrder.setDepositAmountPaid(paymentInfo.getAmountPaid()); // 假设支付的是定金
        finalOrder.setPaymentMethod(paymentInfo.getPaymentMethod());
        finalOrder.setTransactionId(paymentInfo.getTransactionId());
        finalOrder.setOrderStatus("预订成功 - 已支付定金");
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        finalOrder.setBookingTimestamp(now.format(formatter));

        // 后置条件：生成加密电子订单（含虚拟房号与NFC密钥）
        finalOrder.setVirtualRoomNumber("VROOM-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        finalOrder.setNfcKey("NFCKEY-" + UUID.randomUUID().toString().toUpperCase()); // 简单模拟, 实际应加密

        if (userInfo.getSpecialRequests() != null) {
            finalOrder.setSpecialRequests(userInfo.getSpecialRequests());
        }

        // 触发房态锁定（对应房型可订数量减一）
        boolean stockUpdated = roomDAO.updateRoomStock(details.getSelectedRoom().getRoomId(), 1); // 预订1间
        if (!stockUpdated) {
            // 库存更新失败，可能意味着房间已满（并发情况）
            // 需要处理房态冲突的逻辑，例如补偿用户
            System.err.println("HotelService: Stock update failed for room " + details.getSelectedRoom().getRoomId() + ". Booking failed.");
             // 此处应有回滚支付或更复杂处理
            return null; 
        }

        boolean saved = bookingDAO.saveBooking(finalOrder);
        if (saved) {
            System.out.println("HotelService: Booking finalized and saved: " + finalOrder.getOrderId());
            // 同步发送短信/邮件确认 - 此处为模拟
            System.out.println("HotelService: Sending SMS/Email confirmation for order " + finalOrder.getOrderId());
            return finalOrder;
        } else {
            System.err.println("HotelService: Failed to save final order.");
            // 如果保存失败，理论上应该回滚库存更新等操作
            return null;
        }
    }
} 