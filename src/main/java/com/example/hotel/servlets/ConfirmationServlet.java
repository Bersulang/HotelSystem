package com.example.hotel.servlets;

import com.example.hotel.beans.BookingDetailsBean;
import com.example.hotel.beans.FinalOrderBean;
import com.example.hotel.beans.PaymentInfoBean;
import com.example.hotel.beans.UserInformationBean;
import com.example.hotel.beans.UserBean;
import com.example.hotel.service.HotelService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ConfirmationServlet extends HttpServlet {
    private HotelService hotelService;

    @Override
    public void init() throws ServletException {
        super.init();
        hotelService = new HotelService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserBean currentUser = null;

        if (session != null) {
            currentUser = (UserBean) session.getAttribute("currentUser");
        }        if (currentUser == null) {
            if (session != null) { // 避免空指针异常
                session.setAttribute("errorMessage", "请先登录再确认预订。未登录用户不能进行支付操作。");
                // 保存当前请求URL，以及所有预订相关数据，方便登录后重定向回来
                session.setAttribute("redirectAfterLogin", req.getRequestURI());
                
                // 确保预订信息保留在会话中
                BookingDetailsBean bookingDetails = (BookingDetailsBean) session.getAttribute("bookingDetails");
                if (bookingDetails != null) {
                    System.out.println("ConfirmationServlet: Saving booking details for after login: " + bookingDetails.getOrderId());
                }
            }
            resp.sendRedirect(req.getContextPath() + "/login.jsp?reason=payment");
            return;
        }        System.out.println("ConfirmationServlet: User '" + currentUser.getUsername() + "' is proceeding with confirmation.");

        req.setCharacterEncoding("UTF-8");
        BookingDetailsBean bookingDetails = (BookingDetailsBean) session.getAttribute("bookingDetails");

        if (bookingDetails == null) {
            System.err.println("ConfirmationServlet: BookingDetails not found in session.");
            resp.sendRedirect(req.getContextPath() + "/query?error=sessionExpiredOrInvalid");
            return;
        }
        
        // 确保使用当前登录用户的用户名
        bookingDetails.setUserName(currentUser.getUsername());

        UserInformationBean userInfo = new UserInformationBean();
        userInfo.setGuestName(req.getParameter("guestName"));
        userInfo.setContactNumber(req.getParameter("contactNumber"));
        userInfo.setSpecialRequests(req.getParameter("specialRequests"));

        String paymentMethod = req.getParameter("paymentMethod");

        System.out.println("ConfirmationServlet: Processing confirmation for order: " + bookingDetails.getOrderId());
        System.out.println("ConfirmationServlet: UserInfo: " + userInfo);
        System.out.println("ConfirmationServlet: PaymentMethod: " + paymentMethod);

        // 模拟支付过程
        PaymentInfoBean paymentInfo = new PaymentInfoBean();
        paymentInfo.setPaymentMethod(paymentMethod);
        paymentInfo.setAmountPaid(bookingDetails.getDepositAmount()); // 假设支付定金
        paymentInfo.setTransactionId("TRANS-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        paymentInfo.setPaymentTime(now.format(formatter));
        
        // 模拟支付成功/失败 (支付失败后保留库存锁定15分钟 - 复杂逻辑未实现)
        // 异常处理 1. 支付失败：自动重试3次（间隔5分钟），失败后释放库存锁并转人工审核 - 复杂逻辑未实现
        boolean paymentSuccessful = true; // 简单模拟支付总是成功
        if ("fail_test".equals(paymentMethod)) { // 用于测试支付失败路径
            paymentSuccessful = false;
        }

        if (paymentSuccessful) {
            paymentInfo.setPaymentStatus("成功");
            System.out.println("ConfirmationServlet: Payment simulation successful.");

            FinalOrderBean finalOrder = hotelService.finalizeBooking(bookingDetails, userInfo, paymentInfo);

            if (finalOrder != null) {
                req.setAttribute("finalOrder", finalOrder);
                session.removeAttribute("bookingDetails"); // 清理会话中的临时预订信息
                session.removeAttribute("currentQuery");
                req.getRequestDispatcher("/successPage.jsp").forward(req, resp);
            } else {
                // 预订最终化失败 (例如库存不足，保存DB失败)
                System.err.println("ConfirmationServlet: Finalize booking failed.");
                req.setAttribute("errorMessage", "抱歉，预订未能最终确认，可能房间刚刚被订完或系统错误。请重试或联系客服。");
                // 保留bookingDetails以便用户可以尝试修改或重新提交
                req.setAttribute("bookingDetails", bookingDetails);
                req.getRequestDispatcher("/confirmationPage.jsp").forward(req, resp); 
            }
        } else {
            paymentInfo.setPaymentStatus("失败");
            System.err.println("ConfirmationServlet: Payment simulation failed.");
            req.setAttribute("errorMessage", "支付失败，请检查您的支付信息或更换支付方式重试。");
            req.setAttribute("bookingDetails", bookingDetails); // 返回确认页面，保留信息
            req.getRequestDispatcher("/confirmationPage.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserBean currentUser = null;
        BookingDetailsBean bookingDetails = null;

        if (session != null) {
            currentUser = (UserBean) session.getAttribute("currentUser");
            bookingDetails = (BookingDetailsBean) session.getAttribute("bookingDetails");
        }

        if (currentUser == null) {
            session.setAttribute("errorMessage", "请先登录再查看预订详情。");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        if (bookingDetails == null) {
            request.setAttribute("error", "没有找到进行中的预订信息。请重新选择房间。");
            request.getRequestDispatcher("/query").forward(request, response);
            return;
        }

        System.out.println("ConfirmationServlet (doGet): Displaying confirmation page for user: " + currentUser.getUsername());
        request.getRequestDispatcher("/confirmationPage.jsp").forward(request, response);
    }
} 