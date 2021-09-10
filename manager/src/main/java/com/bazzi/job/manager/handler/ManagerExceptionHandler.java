package com.bazzi.job.manager.handler;

import com.bazzi.job.common.ex.BusinessException;
import com.bazzi.job.common.ex.ParameterException;
import com.bazzi.job.common.generic.Result;
import com.bazzi.job.common.generic.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class ManagerExceptionHandler {

    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(ParameterException.class)
    public Result<?> handleException(ParameterException ex, HttpServletRequest request,
                                     HttpServletResponse response) {
        Result<?> result = new Result<>();
        result.setError(ex.getCode(), ex.getMessage());
        return result;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleException(BusinessException ex, HttpServletRequest request,
                                     HttpServletResponse response) {
        Result<?> result = new Result<>();
        result.setError(ex.getCode(), ex.getMessage());
        return result;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> handleException(HttpRequestMethodNotSupportedException ex,
                                     HttpServletRequest request, HttpServletResponse response) {
        log.error(ex.getMessage(), ex);
        Result<?> result = new Result<>();
        result.setError(StatusCode.CODE_008.getCode(),
                String.format(StatusCode.CODE_008.getMessage(), ex.getMethod()));
        return result;
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        log.error(ex.getMessage(), ex);
        Result<?> result = new Result<>();
        result.setError(StatusCode.CODE_003.getCode(), ex.getMessage());
        return result;
    }

}
