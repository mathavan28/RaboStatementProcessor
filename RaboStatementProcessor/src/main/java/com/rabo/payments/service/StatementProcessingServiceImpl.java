package com.rabo.payments.service;

import java.util.List;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rabo.payments.exception.StatementParsingException;
import com.rabo.payments.model.Account;
import com.rabo.payments.model.AccountStatus;
import com.rabo.payments.model.ResultCode;
import com.rabo.payments.validation.StatementValidator;

@Service
public class StatementProcessingServiceImpl implements StatementProcessingService {

	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private List<Integer> curReferenceList;
	private List<AccountStatus> responseList;
	private List<Account> errorList;

	/**
	 * Validate the JSON inputs and generate response
	 */
	public List<AccountStatus> processMonthlyStatement(List<Account> accountsData)
		throws StatementParsingException {

		curReferenceList = new ArrayList<Integer>();
		responseList =  new ArrayList<AccountStatus>();
		AccountStatus accstatus = new AccountStatus();;
		try {
			for(Account account:accountsData) {
					ResultCode code= StatementValidator.validate(account, curReferenceList);
					
					accstatus = new AccountStatus();
					errorList =  new ArrayList<Account>();
					if(!code.name().equals("SUCCESSFUL")) {
						if(code.name().equals("DUPLICATE_REFERENCE_INCORRECT_END_BALANCE")){
							Account duplicateRefAccount = new Account(account.getReference(),account.getAccount_number());
							errorList.add(duplicateRefAccount);
						}
						errorList.add(new Account(account.getReference(),account.getAccount_number()));
						accstatus.setResult(code.name());
						accstatus.setError_records(errorList);
						responseList.add(accstatus);
					}

					curReferenceList.add(account.getReference());
			}
			if(responseList.isEmpty()) {
				accstatus.setResult("SUCCESSFUL");
				accstatus.setError_records(new ArrayList<Account>());
				responseList.add(accstatus);
			}
		}catch(StatementParsingException spe) {
			logger.error("Error while validating Json data!..");
			throw new StatementParsingException("Error while validating Json data!..",spe);
		}
		return responseList;
	}
}
