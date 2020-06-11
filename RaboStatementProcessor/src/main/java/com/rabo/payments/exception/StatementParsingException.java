package com.rabo.payments.exception;

import java.util.List;

import org.springframework.stereotype.Component;
import com.rabo.payments.model.ResultCode;

@Component
public class StatementParsingException  extends Exception {

	private String status;
	private List<?> error_records;
	

	public StatementParsingException() { }
	public StatementParsingException(ResultCode code) {
		this.status = code.name();
	}
	 
    public StatementParsingException(String message, Throwable cause) {}
	
	/**
	 * @return the status
	 */
	public String getResult() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void getResult(String status) {
		this.status = status;
	}
	/**
	 * @return the error_records
	 */
	public List<?> getError_records() {
		return error_records;
	}
	
	/**
	 * @param error_records the error_records to set
	 */
	public void setError_records(List<?> error_records) {
		this.error_records = error_records;
	}	


	@Override
	public StackTraceElement[] getStackTrace() {
		// TODO Auto-generated method stub
		return null;
	}
}
