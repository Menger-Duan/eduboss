package com.eduboss.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.utils.DateTools;


//@EnableWebMvc
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	protected HttpServletResponse response;
	
	@Bean
    public HandlerExceptionResolver sentryExceptionResolver() {
        return new io.sentry.spring.SentryExceptionResolver();
    }

//	@ExceptionHandler(AppException.class)
//	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
//	public @ResponseBody Response handleAppException(HttpServletRequest request, AppException ex){
//		logger.error("ApplicationException:" + ex.getErrorCode().getValue() + "##" + ex.getErrorMsg());
//		ex.printStackTrace();
//		Response response = new Response();
//		response.setResultCode(ex.getErrorCode().getErrorCode());
//		response.setResultMessage(ex.getErrorCode().getErrorString());
//		return response;
//	}



    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody Response handleApplicationException(HttpServletRequest request, RuntimeException ex){
        logger.error(DateTools.getCurrentDateTime() + " 发生系统错误，请联系管理员", ex);
        ex.printStackTrace();
        Response resp = new Response();
        resp.setResultCode(ErrorCode.SYSTEM_ERROR.getErrorCode());
        resp.setResultMessage(DateTools.getCurrentDateTime() + " 发生系统错误，请联系管理员");

        return resp;
    }
	
	@ExceptionHandler(ApplicationException.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody Response handleApplicationException(HttpServletRequest request, ApplicationException ex){
	    if (ex.getErrorCode() != null) {
	        logger.error("ApplicationException:" + ex.getErrorCode().getValue() + "##" + ex.getErrorMsg(), ex);
	    } else {
	        logger.error("ApplicationException:" + ex.getErrorMsg(), ex);
	    }
		Response resp = new Response();
		resp.setResultCode(ex.getErrorCode().getErrorCode());
		resp.setResultMessage(ex.getErrorMsg());
		
		return resp;
	}
	
	@ExceptionHandler(SQLException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	public @ResponseBody Response handleSQLException(HttpServletRequest request, Exception ex){
		logger.error("SQLException:"+ex.getMessage());
		ex.printStackTrace();
		Response response = new Response();
		response.setResultCode(ErrorCode.SQL_ERROR.getErrorCode());
		response.setResultMessage(ex.getMessage());
		return response;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	public @ResponseBody Response handleUnknowException(HttpServletRequest request, Exception ex){
		logger.error("Exception:"+ex.getMessage(),ex);
		ex.printStackTrace();
		Response response = new Response();
		response.setResultCode(ErrorCode.SYSTEM_ERROR.getErrorCode());
		response.setResultMessage(ex.getMessage());
		return response;
	}

	
	
	//hibernate 乐观锁异常
	@ExceptionHandler(StaleObjectStateException.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody Response handleStaleObjectStateException(HttpServletRequest request, ApplicationException ex){
		logger.error("Exception:"+ex.getMessage(),ex);
        ex.printStackTrace();
		Response resp = new Response();
		resp.setResultCode(ErrorCode.HIBERNATE_VERSION_ERROR.getErrorCode());
		resp.setResultMessage(ex.getErrorMsg());
		return resp;
	}
	
	
	
	
	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){
		this.response = response;
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Response handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
	    BindingResult bindingResult = ex.getBindingResult();
	    String errorMesssage = "请求参数错误:";

	    for (FieldError fieldError : bindingResult.getFieldErrors()) {
	        errorMesssage +=  fieldError.getDefaultMessage() + ",";
	    }
	    errorMesssage = errorMesssage.substring(0, errorMesssage.length() - 1);
	    Response resp = new Response();
        resp.setResultCode(ErrorCode.PARAMETER_ERROR.getErrorCode());
        resp.setResultMessage(errorMesssage);
        return resp;
	}

	@ExceptionHandler(BindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response handleBindArgumentNotValidException(BindException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String errorMesssage = "请求参数错误:";

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage +=  fieldError.getDefaultMessage() + ",";
        }
        errorMesssage = errorMesssage.substring(0, errorMesssage.length() - 1);
        Response resp = new Response();
        resp.setResultCode(ErrorCode.PARAMETER_ERROR.getErrorCode());
        resp.setResultMessage(errorMesssage);
        return resp;
    }
}
