package com.newzkl.platform.base.biz.order.model.dto;

public class OrderStateEdit
{
    private String orderSn;

    private int origStatus;

    private String origStatusName;

    private int status;

    private String statusName;

    public void setOrderSn(String orderSn){
        this.orderSn = orderSn;
    }
    public String getOrderSn(){
        return this.orderSn;
    }
    public void setOrigStatus(int origStatus){
        this.origStatus = origStatus;
    }
    public int getOrigStatus(){
        return this.origStatus;
    }
    public void setOrigStatusName(String origStatusName){
        this.origStatusName = origStatusName;
    }
    public String getOrigStatusName(){
        return this.origStatusName;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setStatusName(String statusName){
        this.statusName = statusName;
    }
    public String getStatusName(){
        return this.statusName;
    }
}
