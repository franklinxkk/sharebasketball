package com.bblanqiu.common.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * @author franklin.li
 */
public class DefaultExceptionHandler implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);
    private ObjectMapper mapper;

    public ObjectMapper getMapper() {
        return mapper;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public DefaultExceptionHandler() {
        super();
        this.mapper = new ObjectMapper();
    }

    public DefaultExceptionHandler(ObjectMapper mapper) {
        super();
        this.mapper = mapper;
    }

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, 
    		Object handler, Exception ex) {
    	ex.printStackTrace();
        logger.error(ex.getMessage());
        Error error = null;
        if (ex instanceof ErrorCodeException) {
            ErrorCodeException exception = (ErrorCodeException) ex;
            if (exception.getError() != null) {
                error = exception.getError();
            }
        }else if(ex instanceof IllegalArgumentException){
        	error = new Error(ErrorCode.PARAMETER_VALUE_INVALID);
        } else if(ex instanceof NumberFormatException){
        	error = new Error(ErrorCode.PARAMETER_ERROR);
        } else if(ex instanceof RequestLimitException){
        	error = new Error(ErrorCode.OPERATION_OUT_OF_LIMIT);
        }else if(ex instanceof InvalidMediaTypeException){
        	error = new Error(ErrorCode.INVALID_REQUEST);
        }else if (ex instanceof HttpMediaTypeNotSupportedException) {
        	error = new Error(ErrorCode.UNSUPPORTED_RESPONSE_TYPE);
        }else if(ex instanceof HttpMessageNotReadableException){
        	error = new Error(ErrorCode.HTTP_BODY_FORMAT_ERROR);
        }else if(ex instanceof JsonMappingException){
        	error = new Error(ErrorCode.HTTP_BODY_FORMAT_ERROR);
        }else if(ex instanceof MissingServletRequestParameterException){
        	error = new Error(ErrorCode.PARAMETER_ERROR);
        }else if(ex instanceof JsonParseException){
        	error = new Error(ErrorCode.HTTP_BODY_FORMAT_ERROR);
        }else{
            error = new Error(request.getRequestURI(), ErrorCode.SYSTEM_ERROR);
        }
        if (StringUtils.isEmpty(error.getRequest())) {
            error.setRequest(request.getRequestURI());
        }
        try {
            response.getWriter().write(mapper.writeValueAsString(error));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ModelAndView();
    }

}
