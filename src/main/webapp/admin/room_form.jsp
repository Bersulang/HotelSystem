<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.hotel.beans.RoomBean" %>
<%@ page import="com.example.hotel.service.AdminService" %>

<!DOCTYPE html>
<html>
<head>
    <title><%= request.getParameter("roomId") != null ? "编辑房间" : "添加房间" %></title>
    <link rel="stylesheet" href="../css/common.css">
    <link rel="stylesheet" href="../css/admin.css">
    <style>
        .required {
            color: #e74c3c;
        }
        
        .form-help {
            margin-top: 20px;
            padding: 15px;
            background-color: #f8f9fa;
            border-left: 4px solid #4dabf7;
            border-radius: 4px;
        }
    </style>
    <script>
        function validateForm() {
            // 检查必填字段
            let roomId = document.getElementById('roomId').value.trim();
            let hotelName = document.getElementById('hotelName').value.trim();
            let roomTypeName = document.getElementById('roomTypeName').value.trim();
            let pricePerNight = document.getElementById('pricePerNight').value.trim();
            let realTimeStock = document.getElementById('realTimeStock').value.trim();
            
            if (!roomId || !hotelName || !roomTypeName || !pricePerNight || !realTimeStock) {
                alert('请填写所有必填字段（带*号的字段）');
                return false;
            }
            
            // 价格验证
            let price = parseFloat(pricePerNight);
            if (isNaN(price) || price <= 0) {
                alert('每晚价格必须是大于零的数字');
                return false;
            }
            
            // 库存验证
            let stock = parseInt(realTimeStock);
            if (isNaN(stock) || stock < 0) {
                alert('库存必须是非负整数');
                return false;
            }
            
            // 促销价验证
            let promotionalPrice = document.getElementById('promotionalPrice').value.trim();
            if (promotionalPrice) {
                let promoPrice = parseFloat(promotionalPrice);
                if (isNaN(promoPrice) || promoPrice <= 0) {
                    alert('促销价必须是大于零的数字');
                    return false;
                }
                
                if (promoPrice > price) {
                    alert('促销价不能高于原价');
                    return false;
                }
            }
            
            return true;
        }    </script>
</head>
<body class="admin">
    <%@ include file="../WEB-INF/jspf/admin_header.jsp" %>
    <div class="container">
        <h1><%= request.getParameter("roomId") != null ? "编辑房间" : "添加新房间" %></h1>

        <% 
            String roomId = request.getParameter("roomId");
            RoomBean room = null;
            AdminService adminService = new AdminService();
            boolean isEditMode = roomId != null && !roomId.isEmpty();
            if (isEditMode) {
                room = adminService.getRoomById(roomId);
            }
            if (room == null) {
                room = new RoomBean(); // For add mode or if room not found for edit
            }

            String action = isEditMode ? "updateRoom" : "createRoom";
        %>

        <% String message = (String) request.getAttribute("message"); %>
        <% if (message != null && !message.isEmpty()) { %>
            <p class="message <%= message.startsWith("Error") ? "error-message" : "success-message" %>"><%= message %></p>
        <% } %>        <form action="manageRooms" method="post" onsubmit="return validateForm()">
            <input type="hidden" name="action" value="<%= action %>">
            <% if (isEditMode) { %>
                <input type="hidden" name="originalRoomId" value="<%= room.getRoomId() %>">
            <% } %>
            <div class="form-help">
                <p><strong>提示：</strong> 带有 <span class="required">*</span> 的字段为必填项。</p>
            </div><div class="form-group">
                <label for="roomId">房间ID: <span class="required">*</span></label>
                <input type="text" id="roomId" name="roomId" value="<%= room.getRoomId() != null ? room.getRoomId() : "" %>" <%= isEditMode ? "readonly" : "required" %>>
                 <small>添加新房间时，房间ID必须唯一。编辑时不可更改。</small>
            </div>

            <div class="form-group">
                <label for="hotelName">酒店名称: <span class="required">*</span></label>
                <input type="text" id="hotelName" name="hotelName" value="<%= room.getHotelName() != null ? room.getHotelName() : "" %>" required>
            </div>

            <div class="form-group">
                <label for="hotelStarRating">酒店星级 (1-5):</label>
                <input type="number" id="hotelStarRating" name="hotelStarRating" value="<%= room.getHotelStarRating() > 0 ? room.getHotelStarRating() : "" %>" min="1" max="5" required>
            </div>

            <div class="form-group">
                <label for="hotelLocation">酒店位置:</label>
                <input type="text" id="hotelLocation" name="hotelLocation" value="<%= room.getHotelLocation() != null ? room.getHotelLocation() : "" %>" required>
            </div>

            <div class="form-group">
                <label for="hotelDescription">酒店描述:</label>
                <textarea id="hotelDescription" name="hotelDescription" rows="3" required><%= room.getHotelDescription() != null ? room.getHotelDescription() : "" %></textarea>
            </div>

            <div class="form-group">
                <label for="hotelContact">酒店联系方式:</label>
                <input type="text" id="hotelContact" name="hotelContact" value="<%= room.getHotelContact() != null ? room.getHotelContact() : "" %>">
            </div>

            <div class="form-group">
                <label for="hotelTransportGuide">交通指南:</label>
                <textarea id="hotelTransportGuide" name="hotelTransportGuide" rows="3"><%= room.getHotelTransportGuide() != null ? room.getHotelTransportGuide() : "" %></textarea>
            </div>            <div class="form-group">
                <label for="roomTypeName">房型名称: <span class="required">*</span></label>
                <input type="text" id="roomTypeName" name="roomTypeName" value="<%= room.getRoomTypeName() != null ? room.getRoomTypeName() : "" %>" required>
            </div>

            <div class="form-group">
                <label for="realTimeStock">实时库存: <span class="required">*</span></label>
                <input type="number" id="realTimeStock" name="realTimeStock" value="<%= room.getRealTimeStock() %>" min="0" required>
            </div>

            <div class="form-group">
                <label for="pricePerNight">每晚价格: <span class="required">*</span></label>
                <input type="number" id="pricePerNight" name="pricePerNight" value="<%= String.format("%.2f", room.getPricePerNight()) %>" step="0.01" min="0" required>
            </div>

            <div class="form-group">
                <label for="promotionalPrice">促销价格 (可选):</label>
                <input type="number" id="promotionalPrice" name="promotionalPrice" value="<%= String.format("%.2f", room.getPromotionalPrice()) %>" step="0.01" min="0">
            </div>

            <div class="form-group">
                <label for="roomFacilitiesList">房间设施 (逗号分隔):</label>
                <input type="text" id="roomFacilitiesList" name="roomFacilitiesList" value="<%= room.getRoomFacilitiesList() != null ? room.getRoomFacilitiesList() : "" %>">
                 <small>例如: 空调,电视,免费WiFi</small>
            </div>

            <div class="form-group">
                <label for="roomDescription">房间描述:</label>
                <textarea id="roomDescription" name="roomDescription" rows="3"><%= room.getRoomDescription() != null ? room.getRoomDescription() : "" %></textarea>
            </div>

            <div class="form-group">
                <label for="area">面积 (平方米):</label>
                <input type="number" id="area" name="area" value="<%= room.getArea() > 0 ? String.format("%.2f", room.getArea()) : "" %>" step="0.01" min="0">
            </div>

            <div class="form-group">
                <label for="bedType">床型:</label>
                <input type="text" id="bedType" name="bedType" value="<%= room.getBedType() != null ? room.getBedType() : "" %>">
            </div>

            <div class="form-group">
                <label for="maxOccupancy">最大入住人数:</label>
                <input type="number" id="maxOccupancy" name="maxOccupancy" value="<%= room.getMaxOccupancy() > 0 ? room.getMaxOccupancy() : "" %>" min="1">
            </div>

            <button type="submit" class="submit-button"><%= isEditMode ? "更新房间" : "添加房间" %></button>
            <a href="manage_rooms.jsp" class="cancel-button">取消</a>
        </form>
    </div>
    <%@ include file="../WEB-INF/jspf/admin_footer.jsp" %>
</body>
</html>
