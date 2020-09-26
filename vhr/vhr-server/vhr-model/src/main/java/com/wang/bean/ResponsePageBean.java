package com.wang.bean;

import java.util.List;

public class ResponsePageBean {
    private Long total;//总共查询出来多少数据
    private List<?> data;//数据的集合

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
