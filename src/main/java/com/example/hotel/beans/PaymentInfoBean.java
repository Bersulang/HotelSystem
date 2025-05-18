package com.example.hotel.beans;

//支付信息
public class PaymentInfoBean {
    private String paymentMethod; // 支付方式
    private String paymentTime; // 支付时间 (格式: yyyy-MM-dd yy:yy:yy)
    private String transactionId; // 交易流水号
    private String paymentStatus; // 例如: "成功", "失败", "处理中"
    private double amountPaid; // 实际支付金额

    public PaymentInfoBean() {
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    @Override
    public String toString() {
        return "PaymentInfoBean{" +
                "paymentMethod='" + paymentMethod + '\'' +
                ", paymentTime='" + paymentTime + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", amountPaid=" + amountPaid +
                '}';
    }
} 