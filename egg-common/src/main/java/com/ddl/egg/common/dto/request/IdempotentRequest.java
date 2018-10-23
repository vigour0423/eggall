package com.ddl.egg.common.dto.request;

import java.io.Serializable;

/**
 * 幂等性通用请求,用transactionId做幂等性校验
 * <p>
 * Created by zhuyuefan on 2016/4/18.
 */
public class IdempotentRequest implements Serializable {

    protected String transactionId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
