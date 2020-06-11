/**
 * 
 */
package com.rabo.payments.validation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.rabo.payments.exception.StatementParsingException;
import com.rabo.payments.model.Account;
import com.rabo.payments.model.ResultCode;
/**
 * @author Madhavan
 *
 */
public class StatementValidator {
	
	public static ResultCode validate(Account account,List<Integer> curReferenceList) throws StatementParsingException {

        final int referenceId = account.getReference();
        
        if(referenceId ==0 || account.getAccount_number()==null) 
        	throw new StatementParsingException(); 
        
        
        if (!curReferenceList.contains(referenceId)) {
        	return isValidTransaction(account) ? ResultCode.SUCCESSFUL:ResultCode.INCORRECT_END_BALANCE;   
        } else {
    		try {
    			
    			return isValidTransaction(account) ? ResultCode.DUPLICATE_REFERENCE:ResultCode.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE;
        		        			
    		}catch(Exception ex) {
    			throw new StatementParsingException("Error while parsing record",ex);
    		}
        }
    }
    
    private static boolean isValidTransaction(Account account) throws StatementParsingException {
    	
    	if(Optional.of(account.getStart_balance()).isPresent() && account.getStart_balance() instanceof BigDecimal &&
    			Optional.of(account.getEnd_balance()).isPresent() && account.getEnd_balance() instanceof BigDecimal &&
    			Optional.of(account.getMutation()).isPresent() && account.getMutation() instanceof BigDecimal) {
    			
    		return (account.getEnd_balance().compareTo(
    				account.getStart_balance().add(account.getMutation()))==0?true:false);
    	}else 
    		throw new StatementParsingException();
    	
    }
}
