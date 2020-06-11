package com.rabo.payments.service;

import java.util.List;

import com.rabo.payments.exception.StatementParsingException;
import com.rabo.payments.model.Account;
import com.rabo.payments.model.AccountStatus;
public interface StatementProcessingService {
	
		public List<AccountStatus> processMonthlyStatement(List<Account> accountsData)
				throws StatementParsingException;
}
