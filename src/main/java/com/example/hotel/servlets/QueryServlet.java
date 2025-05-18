package com.example.hotel.servlets;

import com.example.hotel.beans.QueryFormBean;
import com.example.hotel.beans.RoomResultBean;
import com.example.hotel.service.HotelService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/** 处理用户提交的查询请求。获取查询参数，封装成 QueryFormBean，调用 HotelService.searchRooms(queryFormBean)和RoomDAO.findAvailableRooms(queryFormBean)*/
public class QueryServlet extends HttpServlet {
    private HotelService hotelService;

    @Override
    public void init() throws ServletException {
        super.init();
        hotelService = new HotelService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 通常查询表单初始加载用GET，提交用POST，这里允许GET也跳转到表单
        String action = req.getParameter("action");
        HttpSession session = req.getSession(false); // false表示如果会话不存在则不创建

        if ("showLastResults".equals(action) && session != null) {
            @SuppressWarnings("unchecked") // 类型转换是预期的
            List<RoomResultBean> lastResults = (List<RoomResultBean>) session.getAttribute("lastRoomResults");
            QueryFormBean lastQuery = (QueryFormBean) session.getAttribute("lastQueryForm");

            if (lastResults != null && lastQuery != null) {
                System.out.println("QueryServlet: doGet action=showLastResults, found " + lastResults.size() + " rooms in session.");
                req.setAttribute("roomResults", lastResults);
                // 将lastQueryForm也放入request，这样selectionPage.jsp可以访问到它，例如用于显示查询条件
                req.setAttribute("queryForm", lastQuery);
                req.getRequestDispatcher("/selectionPage.jsp").forward(req, resp);
                return;
            } else {
                System.err.println("QueryServlet: doGet action=showLastResults, but lastRoomResults or lastQueryForm not found in session. Redirecting to query form.");
                resp.sendRedirect(req.getContextPath() + "/queryForm.jsp");
                return;
            }
        }
        System.out.println("QueryServlet: doGet default action, forwarding to queryForm.jsp");
        req.getRequestDispatcher("/queryForm.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        QueryFormBean queryBean = new QueryFormBean();
        queryBean.setHotelName(req.getParameter("hotelName"));
        queryBean.setLocation(req.getParameter("location"));
        queryBean.setRoomType(req.getParameter("roomType"));
        queryBean.setCheckInDate(req.getParameter("checkInDate"));
        queryBean.setCheckOutDate(req.getParameter("checkOutDate"));
        
        String priceRangeMinStr = req.getParameter("priceRangeMin");
        if (priceRangeMinStr != null && !priceRangeMinStr.isEmpty()) {
            try {
                queryBean.setPriceRangeMin(Double.parseDouble(priceRangeMinStr));
            } catch (NumberFormatException e) {
                // 处理错误或设置默认值
                System.err.println("QueryServlet: Invalid priceRangeMin format");
            }
        }

        String priceRangeMaxStr = req.getParameter("priceRangeMax");
        if (priceRangeMaxStr != null && !priceRangeMaxStr.isEmpty()) {
            try {
                queryBean.setPriceRangeMax(Double.parseDouble(priceRangeMaxStr));
            } catch (NumberFormatException e) {
                System.err.println("QueryServlet: Invalid priceRangeMax format");
            }
        }
        
        String[] facilities = req.getParameterValues("facilities");
        if (facilities != null) {
            queryBean.setFacilities(Arrays.asList(facilities));
        }

        System.out.println("QueryServlet: Received query -> " + queryBean);

        // 将查询条件存入 session，后续选择房间和确认时可能需要其中的日期信息
        HttpSession session = req.getSession();
        session.setAttribute("currentQuery", queryBean);

        List<RoomResultBean> results = hotelService.searchAvailableRooms(queryBean);
        System.out.println("QueryServlet: Found " + (results != null ? results.size() : 0) + " rooms.");

        req.setAttribute("roomResults", results);
        req.setAttribute("queryForm", queryBean); // 回显查询条件
        // 将本次查询结果和条件也存入session，供"返回选择列表"使用
        session.setAttribute("lastRoomResults", results);
        session.setAttribute("lastQueryForm", queryBean);
        req.getRequestDispatcher("/selectionPage.jsp").forward(req, resp);

    }
} 