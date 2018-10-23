package com.ddl.egg.exception;

import com.ddl.egg.log.exception.Ignore;

/**
 * Created by mark.huang on 2016-10-18.
 */
@Ignore
public class IgnoredBusinessException extends BusinessException {

    public IgnoredBusinessException(int code, String message) {
        super(code, message);
    }

    public IgnoredBusinessException(int code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
}
