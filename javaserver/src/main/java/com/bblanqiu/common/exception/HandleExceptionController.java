package com.bblanqiu.common.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
/**
 * @author franklin.li
 */
public abstract class HandleExceptionController {
    private static Logger exceptionLogger = LoggerFactory.getLogger(HandleExceptionController.class);

    @ExceptionHandler
    public
    @ResponseBody
    Object handleException(HttpServletRequest request, Exception ex) {
        Error error = handleDeviceNetworkException(ex);
        String requestUri = request.getRequestURI();
        if (error == null) {
            error = new Error(requestUri, ErrorCode.SYSTEM_ERROR);
        }
        error.setRequest(requestUri);
        exceptionLogger.error(error.toString());
        return error;

    }

    private Error handleDeviceNetworkException(Exception ex) {
        if (ex instanceof TypeMismatchException) {
            return handleTypeMismatchException((TypeMismatchException) ex);
        } else if (ex instanceof HttpMessageNotReadableException) {
            return handleHttpMessageNotReadableException((HttpMessageNotReadableException) ex);
        } else if (ex instanceof MissingServletRequestParameterException) {
            return handleMissingServletRequestParameterException((MissingServletRequestParameterException) ex);
        } else if (ex instanceof BindException) {
            return handleBindException((BindException) ex);
        } else if (ex instanceof MethodArgumentNotValidException) {
            return handleMethodArgumentNotValidException((MethodArgumentNotValidException) ex);
        } else if (ex instanceof ErrorCodeException) {
            ErrorCodeException ex2 = (ErrorCodeException) ex;
            return ex2.getError();
        } else if(ex instanceof InvalidMediaTypeException){
        	return new Error(ErrorCode.INVALID_REQUEST);
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            return new Error(ErrorCode.INVALID_REQUEST);
        } else if(ex instanceof IllegalArgumentException){
        	return new Error(ErrorCode.PARAMETER_VALUE_INVALID);
        }else if(ex instanceof RequestLimitException){
        	return new Error(ErrorCode.OPERATION_OUT_OF_LIMIT);
        }else if(ex instanceof HttpMessageNotReadableException){
        	return new Error(ErrorCode.HTTP_BODY_FORMAT_ERROR);
        }else {
            return null;
        }
    }

    private Error handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return handleBindingResult(ex.getBindingResult());
    }

    private Error handleBindingResult(BindingResult result) {
        if (result.hasFieldErrors()) {
            FieldError fieldError = result.getFieldError();
            if (fieldError.getCode().equals("NotNull")) {
                return new Error(ErrorCode.MISS_REQUIRED_PARAMETER, fieldError.getField());
            } else {
                System.out.println(fieldError.getCode());
                return new Error(ErrorCode.PARAMETER_VALUE_INVALID, fieldError.getField());
            }
        } else if (result.hasGlobalErrors()) {
            System.out.println(result.getGlobalError().getDefaultMessage());
            return null;
        } else {
            return null;
        }
    }

    private Error handleBindException(BindException ex) {
        return handleBindingResult(ex.getBindingResult());
    }

    private Error handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        return new Error(ErrorCode.MISS_REQUIRED_PARAMETER, parameterName);
    }

    private Error handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof UnrecognizedPropertyException) {
            exceptionLogger.error("unrecogized property "
                    + ((UnrecognizedPropertyException) ex.getCause()).getUnrecognizedPropertyName());
            return null;
        } else {
            if (cause instanceof JsonMappingException) {
                JsonMappingException exception = (JsonMappingException) cause;
                if (exception.getPath().size() == 0) {
                    return new Error(ErrorCode.INVALID_REQUEST);
                } else {
                    String fieldName = exception.getPath().get(0).getFieldName();
                    return new Error(ErrorCode.PARAMETER_VALUE_INVALID, fieldName);
                }
            } else if (cause instanceof JsonParseException) {
                return new Error(ErrorCode.INVALID_REQUEST);
            } else {
                ex.printStackTrace();
                return null;
            }
        }
    }

    private Error handleTypeMismatchException(TypeMismatchException ex) {
        exceptionLogger.debug(" " + (ex.getLocalizedMessage()));
        return new Error(ErrorCode.PARAMETER_VALUE_INVALID, "");
    }
}
