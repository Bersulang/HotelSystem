package com.example.hotel.servlets;

import com.example.hotel.beans.BookingDetailsBean;
import com.example.hotel.beans.QueryFormBean;
import com.example.hotel.service.HotelService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class SelectionServlet extends HttpServlet {
    private HotelService hotelService;

    @Override
    public void init() throws ServletException {
        super.init();
        hotelService = new HotelService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String roomId = req.getParameter("roomId");
        HttpSession session = req.getSession();
        QueryFormBean queryContext = (QueryFormBean) session.getAttribute("currentQuery");

        System.out.println("SelectionServlet: Received roomId -> " + roomId);

        if (roomId == null || roomId.isEmpty()) {
            // 处理错误，roomId是必需的
            resp.sendRedirect(req.getContextPath() + "/queryForm.jsp?error=missingRoomId");
            return;
        }
        if (queryContext == null) {
            // queryContext 不应该为空，如果为空则重定向到查询页面
            System.err.println("SelectionServlet: QueryContext not found in session.");
            resp.sendRedirect(req.getContextPath() + "/queryForm.jsp?error=sessionExpired");
            return;
        }

        BookingDetailsBean bookingDetails = hotelService.prepareBookingDetails(roomId, queryContext);

        if (bookingDetails == null) {
            // 房间无效
            System.err.println("SelectionServlet: Failed to prepare booking details for roomId: " + roomId);
            // 可以重定向到错误页面或带错误参数的选择页面
            resp.sendRedirect(req.getContextPath() + "/selectionPage.jsp?error=bookingPrepFailed&roomId=" + roomId);
            return;
        }

        session.setAttribute("bookingDetails", bookingDetails);
        req.setAttribute("bookingDetails", bookingDetails);
        req.getRequestDispatcher("/confirmationPage.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String roomId = req.getParameter("roomId");
        if (roomId != null && !roomId.isEmpty()) {
            // 模拟POST行为，但不推荐用于实际数据修改操作
            HttpSession session = req.getSession();
            QueryFormBean queryContext = (QueryFormBean) session.getAttribute("currentQuery");
             if (queryContext == null) {
                resp.sendRedirect(req.getContextPath() + "/queryForm.jsp?error=sessionExpired");
                return;
            }
            BookingDetailsBean bookingDetails = hotelService.prepareBookingDetails(roomId, queryContext);
            if (bookingDetails != null) {
                session.setAttribute("bookingDetails", bookingDetails);
                req.setAttribute("bookingDetails", bookingDetails);
                req.getRequestDispatcher("/confirmationPage.jsp").forward(req, resp);
                return;
            }
        }
        resp.sendRedirect(req.getContextPath() + "/query"); // 重定向到查询页面
    }
} 