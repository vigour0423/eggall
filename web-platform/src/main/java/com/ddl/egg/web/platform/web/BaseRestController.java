package com.ddl.egg.web.platform.web;

import com.ddl.egg.exception.BusinessException;
import com.ddl.egg.log.ActionLoggerImpl;
import com.ddl.egg.log.util.ExceptionUtils;
import com.ddl.egg.setting.RuntimeEnvironment;
import com.ddl.egg.setting.RuntimeSettings;
import com.ddl.egg.web.platform.exception.ResourceExistException;
import com.ddl.egg.web.platform.exception.ResourceNotFoundException;
import com.ddl.egg.web.platform.web.response.BaseResponse;
import com.ddl.egg.web.platform.exception.AuthenticationException;
import com.ddl.egg.web.platform.exception.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


public class BaseRestController {
    private final ActionLoggerImpl actionLogger = ActionLoggerImpl.get();

    protected static final BaseResponse SUCCESS_RESPONSE = new BaseResponse();

    @Autowired
    private RuntimeSettings runtimeSettings;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse error(Throwable e) {
        return createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse notFound(ResourceNotFoundException e) {
        return createErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceExistException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse hasExisted(ResourceExistException e) {
        return createErrorResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse validationError(InvalidRequestException e) {
        return createErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse validationError(MethodArgumentNotValidException e) {
        return createErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse validationError(AuthenticationException e) {
        return createErrorResponse(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse businessError(BusinessException e) {
        return createErrorResponse(e);
    }

    private BaseResponse createErrorResponse(Throwable e, HttpStatus httpStatus) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(String.valueOf(httpStatus.value()));
        baseResponse.setMessage(httpStatus.name());
        setErrorMessage(baseResponse, e);

        return baseResponse;
    }

    private BaseResponse createErrorResponse(BusinessException e) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(String.valueOf(e.getErrorCode()));
        setErrorMessage(baseResponse, e);

        return baseResponse;
    }

    private void setErrorMessage(BaseResponse baseResponse, Throwable e) {
        if (RuntimeEnvironment.online != runtimeSettings.getEnvironment()) {
            baseResponse.setStackTrace(ExceptionUtils.stackTrace(e));
        }

        if (StringUtils.hasText(e.getMessage())) {
            baseResponse.setMessage(e.getMessage());
        }
    }

    protected long calElapsedTime() {
        if (actionLogger.currentActionLog() != null) {
            return System.currentTimeMillis() - actionLogger.currentActionLog().getRequestDate().getTime();
        } else {
            return 0;
        }
    }

}
