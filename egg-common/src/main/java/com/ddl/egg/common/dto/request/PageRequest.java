package com.ddl.egg.common.dto.request;

import java.io.Serializable;

public class PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer page = 1;

    private Integer rows = 25;

    private String sort;

    private String order;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public PageRequest(Integer page, Integer rows) {
        this.page = page;
        this.rows = rows;
    }

    public PageRequest() {
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "domain=" + page +
                ", rows=" + rows +
                ", sort=" + sort +
                ", order='" + order + '\'' +
                '}';
    }
}
