package com.example.chatfirebase;



import android.util.Log;

import java.util.ArrayList;

public class OrderItem
{
    private String Code;
    private String Date;
    private Long Price;
    private String Phone;
    private String State;
    private ArrayList<MenuItem> Order = new ArrayList<MenuItem>();
   // private StringBuilder Order;

    public OrderItem(String code, String date, Long price, String phone, String State,ArrayList<MenuItem> order) {
        Code = code;
        Date = date;
        Price = price;
        Phone = phone;
        this.State=State;
        for (int i= 0;i<order.size();i++)
        {
            Order.add(order.get(i));
        }

        Log.i("Constructor","Done");
    }
    public String getCode() {
        return Code;
    }

    public String getDate() {
        return Date;
    }

    public Long getPrice() {
        return Price;
    }

    public String getPhone() {
        return Phone;
    }

    public ArrayList<MenuItem> getOrder() {
        return Order;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
