package com.rabo.payments.model;

import java.util.List;

public class AccountStatus {

	private String result;
	private List<Account> error_records;
	
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return the error_records
	 */
	public List<Account> getError_records() {
		return error_records;
	}
	/**
	 * @param error_records the error_records to set
	 */
	public void setError_records(List<Account> error_records) {
		this.error_records = error_records;
	}

}
