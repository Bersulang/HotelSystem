package com.example.hotel.servlets;

import com.example.hotel.beans.RoomBean;
import com.example.hotel.beans.UserBean;
import com.example.hotel.service.AdminService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/admin/manageRooms")
public class RoomManagementServlet extends HttpServlet {
    
    private AdminService adminService;
    
    @Override
    public void init() {
        adminService = new AdminService();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserBean currentUser = (UserBean) session.getAttribute("currentUser");
        
        // 检查是否是管理员
        if (currentUser == null || !"admin".equals(currentUser.getUserRole())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=unauthorized");
            return;
        }
        
        String action = req.getParameter("action");
        
        if (action == null) {
            // 默认显示所有房间
            resp.sendRedirect("manage_rooms.jsp");
            return;
        }
        
        switch (action) {
            case "add":
                // 跳转到添加房间页面
                req.getRequestDispatcher("room_form.jsp").forward(req, resp);
                break;
            case "edit":
                // 编辑现有房间
                String roomId = req.getParameter("roomId");
                RoomBean room = adminService.getRoomById(roomId);
                
                if (room != null) {
                    req.setAttribute("room", room);
                    req.getRequestDispatcher("room_form.jsp").forward(req, resp);
                } else {
                    req.setAttribute("message", "Error: 未找到指定的房间");
                    req.getRequestDispatcher("manage_rooms.jsp").forward(req, resp);
                }
                break;
            case "delete":
                // 删除房间
                String deleteRoomId = req.getParameter("roomId");
                boolean deleted = adminService.deleteRoom(deleteRoomId);
                
                if (deleted) {
                    req.setAttribute("message", "房间已成功删除");
                } else {
                    req.setAttribute("message", "Error: 无法删除房间，可能有关联的预订");
                }
                req.getRequestDispatcher("manage_rooms.jsp").forward(req, resp);
                break;
            default:
                resp.sendRedirect("manage_rooms.jsp");
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserBean currentUser = (UserBean) session.getAttribute("currentUser");
        
        // 检查是否是管理员
        if (currentUser == null || !"admin".equals(currentUser.getUserRole())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=unauthorized");
            return;
        }
        
        req.setCharacterEncoding("UTF-8"); // 确保正确处理中文输入
        
        String action = req.getParameter("action");
        
        if ("createRoom".equals(action) || "updateRoom".equals(action)) {
            // 基本数据验证
            String roomId = req.getParameter("roomId");
            String hotelName = req.getParameter("hotelName");
            String roomTypeName = req.getParameter("roomTypeName");
            String pricePerNight = req.getParameter("pricePerNight");
            String realTimeStock = req.getParameter("realTimeStock");
            
            // 验证必填字段
            if (roomId == null || roomId.trim().isEmpty() || 
                hotelName == null || hotelName.trim().isEmpty() ||
                roomTypeName == null || roomTypeName.trim().isEmpty() ||
                pricePerNight == null || pricePerNight.trim().isEmpty() ||
                realTimeStock == null || realTimeStock.trim().isEmpty()) {
                
                req.setAttribute("message", "Error: 所有带*的字段都是必填项");
                req.getRequestDispatcher("room_form.jsp").forward(req, resp);
                return;
            }
            
            // 价格和库存的数值验证
            try {
                double price = Double.parseDouble(pricePerNight);
                int stock = Integer.parseInt(realTimeStock);
                
                if (price <= 0) {
                    req.setAttribute("message", "Error: 每晚价格必须大于零");
                    req.getRequestDispatcher("room_form.jsp").forward(req, resp);
                    return;
                }
                
                if (stock < 0) {
                    req.setAttribute("message", "Error: 库存不能为负数");
                    req.getRequestDispatcher("room_form.jsp").forward(req, resp);
                    return;
                }
            } catch (NumberFormatException e) {
                req.setAttribute("message", "Error: 价格和库存必须是有效数字");
                req.getRequestDispatcher("room_form.jsp").forward(req, resp);
                return;
            }
            
            // 从表单收集房间信息
            RoomBean room = new RoomBean();
            room.setRoomId(roomId);
            room.setHotelName(hotelName);
            
            try {
                room.setHotelStarRating(Integer.parseInt(req.getParameter("hotelStarRating")));
            } catch (NumberFormatException e) {
                room.setHotelStarRating(0);
            }
            
            room.setHotelLocation(req.getParameter("hotelLocation"));
            room.setHotelDescription(req.getParameter("hotelDescription"));
            room.setHotelContact(req.getParameter("hotelContact"));
            room.setHotelTransportGuide(req.getParameter("hotelTransportGuide"));
            room.setRoomTypeName(roomTypeName);
            room.setRealTimeStock(Integer.parseInt(realTimeStock));
            room.setPricePerNight(Double.parseDouble(pricePerNight));
            
            String promotionalPrice = req.getParameter("promotionalPrice");
            if (promotionalPrice != null && !promotionalPrice.isEmpty()) {
                try {
                    double promoPrice = Double.parseDouble(promotionalPrice);
                    // 验证促销价不高于原价
                    if (promoPrice > room.getPricePerNight()) {
                        req.setAttribute("message", "Error: 促销价不能高于原价");
                        req.getRequestDispatcher("room_form.jsp").forward(req, resp);
                        return;
                    }
                    room.setPromotionalPrice(promoPrice);
                } catch (NumberFormatException e) {
                    req.setAttribute("message", "Error: 促销价必须是有效数字");
                    req.getRequestDispatcher("room_form.jsp").forward(req, resp);
                    return;
                }
            }
            
            room.setRoomFacilitiesList(req.getParameter("roomFacilitiesList"));
            room.setRoomDescription(req.getParameter("roomDescription"));
            
            String area = req.getParameter("area");
            if (area != null && !area.isEmpty()) {
                try {
                    room.setArea(Double.parseDouble(area));
                } catch (NumberFormatException e) {
                    req.setAttribute("message", "Error: 面积必须是有效数字");
                    req.getRequestDispatcher("room_form.jsp").forward(req, resp);
                    return;
                }
            }
            
            room.setBedType(req.getParameter("bedType"));
            
            String maxOccupancy = req.getParameter("maxOccupancy");
            if (maxOccupancy != null && !maxOccupancy.isEmpty()) {
                try {
                    room.setMaxOccupancy(Integer.parseInt(maxOccupancy));
                } catch (NumberFormatException e) {
                    req.setAttribute("message", "Error: 最大入住人数必须是整数");
                    req.getRequestDispatcher("room_form.jsp").forward(req, resp);
                    return;
                }
            }
            
            boolean success = false;
            
            if ("createRoom".equals(action)) {
                // 检查房间ID是否已存在
                RoomBean existingRoom = adminService.getRoomById(room.getRoomId());
                if (existingRoom != null) {
                    req.setAttribute("message", "Error: 房间ID已存在，请使用不同的ID");
                    req.getRequestDispatcher("room_form.jsp").forward(req, resp);
                    return;
                }
                
                // 添加新房间
                success = adminService.addRoom(room);
                if (success) {
                    req.setAttribute("message", "房间已成功添加");
                } else {
                    req.setAttribute("message", "Error: 添加房间失败");
                }
            } else {
                // 更新现有房间
                String originalRoomId = req.getParameter("originalRoomId");
                success = adminService.updateRoom(room, originalRoomId);
                if (success) {
                    req.setAttribute("message", "房间已成功更新");
                } else {
                    req.setAttribute("message", "Error: 更新房间失败");
                }
            }
            
            req.getRequestDispatcher("manage_rooms.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("manage_rooms.jsp");
        }
    }
}
