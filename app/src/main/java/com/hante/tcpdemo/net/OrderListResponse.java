package com.hante.tcpdemo.net;

import com.hante.tcpdemo.bean.OrderInfo;

import java.io.Serializable;
import java.util.List;

public class OrderListResponse implements Serializable {
    private int current;
    private int pages;
    private int size;
    private int total;
    private List<OrderInfo> records;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<OrderInfo> getRecords() {
        return records;
    }

    public void setRecords(List<OrderInfo> records) {
        this.records = records;
    }
}
