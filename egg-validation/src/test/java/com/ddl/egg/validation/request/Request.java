package com.ddl.egg.validation.request;

import com.ddl.egg.validation.custom.v2.annotation.CollectionVerify;
import com.ddl.egg.validation.custom.v2.annotation.IntNotEmpty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by mark on 2017/3/29.
 */
public class Request {

    @NotEmpty
    private String str;
    @IntNotEmpty
    private Integer i;
    @CollectionVerify(nullable = false, min = 2, max = 10)
    @Valid
    private List<InnerRequest> innerRequestList;
    @CollectionVerify(nullable = true, min = 0, max = 10)
    @Valid
    private List<InnerRequest> innerRequestList2;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    public List<InnerRequest> getInnerRequestList() {
        return innerRequestList;
    }

    public void setInnerRequestList(List<InnerRequest> innerRequestList) {
        this.innerRequestList = innerRequestList;
    }

    public List<InnerRequest> getInnerRequestList2() {
        return innerRequestList2;
    }

    public void setInnerRequestList2(List<InnerRequest> innerRequestList2) {
        this.innerRequestList2 = innerRequestList2;
    }
}
