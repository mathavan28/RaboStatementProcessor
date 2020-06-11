package com.rabo.payments.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

@Component
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class StatementErrorResponse  extends RuntimeException {
	
	private String status;
	private List<?> error_records;
	
	public StatementErrorResponse() { }
    public StatementErrorResponse(String message) {
        /*super(message);*/
    }

    public StatementErrorResponse(String message, Throwable cause) {}

    /**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
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
}
