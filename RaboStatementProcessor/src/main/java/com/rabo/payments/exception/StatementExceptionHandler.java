package com.rabo.payments.exception;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rabo.payments.model.AccountStatus;
import com.rabo.payments.model.ResultCode;

@ControllerAdvice 
public class StatementExceptionHandler {

	//Add an exception handler to catch any exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<AccountStatus> handleException(Exception	 ex)
	{
		AccountStatus errStatus = new AccountStatus();
		errStatus.setResult(ResultCode.INTERNAL_SERVER_ERROR.name());
		errStatus.setError_records(new ArrayList());
		return new ResponseEntity<>(errStatus, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	//Add an exception handler to catch HttpMessageNotReadableException exception 
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<AccountStatus> handleException (HttpMessageNotReadableException exe)
	{
		AccountStatus errStatus = new AccountStatus();
		errStatus.setResult(ResultCode.BAD_REQUEST.name());
		errStatus.setError_records(new ArrayList());
		return new ResponseEntity<>(errStatus, HttpStatus.BAD_REQUEST);
	}
	
	//Add an exception handler to catch StatementParsingException exception
	@ExceptionHandler(StatementParsingException.class)
	public ResponseEntity<AccountStatus> handleException ( StatementParsingException	 exe)
	{
		AccountStatus errStatus = new AccountStatus();
		errStatus.setResult(ResultCode.BAD_REQUEST.name());
		errStatus.setError_records(new ArrayList());
		return new ResponseEntity<>(errStatus, HttpStatus.BAD_REQUEST);
	}
	
}
