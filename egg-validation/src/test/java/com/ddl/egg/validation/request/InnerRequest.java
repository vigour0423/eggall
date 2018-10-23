package com.ddl.egg.validation.request;

import com.ddl.egg.validation.custom.v2.annotation.IntNotEmpty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by mark on 2017/3/29.
 */
public class InnerRequest {

    @NotEmpty
    private String innerStr;
    @IntNotEmpty
    private Integer innerInt;

    public String getInnerStr() {
        return innerStr;
    }

    public void setInnerStr(String innerStr) {
        this.innerStr = innerStr;
    }

    public Integer getInnerInt() {
        return innerInt;
    }

    public void setInnerInt(Integer innerInt) {
        this.innerInt = innerInt;
    }
}
