package com.example.hotel.servlets;

import com.example.hotel.beans.BookingDetailsBean;
import com.example.hotel.beans.UserBean;
import com.example.hotel.service.AdminService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/admin/manageBookings")
public class BookingManagementServlet extends HttpServlet {
    
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
        
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        
        String action = req.getParameter("action");
        
        if (action == null) {
            // 默认显示所有预订
            resp.sendRedirect("manage_bookings.jsp");
            return;
        }
        
        switch (action) {
            case "view":
                // 查看预订详情
                String bookingIdStr = req.getParameter("bookingId");
                try {
                    int bookingId = Integer.parseInt(bookingIdStr);
                    BookingDetailsBean booking = adminService.getBookingById(bookingId);
                    
                    if (booking != null) {
                        req.setAttribute("booking", booking);
                        req.getRequestDispatcher("booking_details.jsp").forward(req, resp);
                    } else {
                        req.setAttribute("message", "Error: 未找到指定的预订");
                        req.getRequestDispatcher("manage_bookings.jsp").forward(req, resp);
                    }
                } catch (NumberFormatException e) {
                    req.setAttribute("message", "Error: 无效的预订ID");
                    req.getRequestDispatcher("manage_bookings.jsp").forward(req, resp);
                }
                break;
            case "updateStatus":
                // 更新预订状态
                String updateBookingIdStr = req.getParameter("bookingId");
                String status = req.getParameter("status");
                
                try {
                    int bookingId = Integer.parseInt(updateBookingIdStr);
                    boolean updated = adminService.updateBookingStatus(bookingId, status);
                    
                    if (updated) {
                        req.setAttribute("message", "预订状态已成功更新为 " + status);
                    } else {
                        req.setAttribute("message", "Error: 更新预订状态失败");
                    }
                } catch (NumberFormatException e) {
                    req.setAttribute("message", "Error: 无效的预订ID");
                }
                req.getRequestDispatcher("manage_bookings.jsp").forward(req, resp);
                break;
            case "delete":
                // 删除预订
                String deleteBookingIdStr = req.getParameter("bookingId");
                try {
                    int bookingId = Integer.parseInt(deleteBookingIdStr);
                    boolean deleted = adminService.deleteBooking(bookingId);
                    
                    if (deleted) {
                        req.setAttribute("message", "预订已成功删除");
                    } else {
                        req.setAttribute("message", "Error: 删除预订失败");
                    }
                } catch (NumberFormatException e) {
                    req.setAttribute("message", "Error: 无效的预订ID");
                }
                req.getRequestDispatcher("manage_bookings.jsp").forward(req, resp);
                break;
            default:
                resp.sendRedirect("manage_bookings.jsp");
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 预订管理主要通过GET请求处理，所以这里简单地重定向到GET处理
        doGet(req, resp);
    }
}
