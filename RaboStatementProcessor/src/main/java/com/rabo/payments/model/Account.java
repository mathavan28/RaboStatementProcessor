/**
 * 
 */
package com.rabo.payments.model;


import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class Account {

        @JsonProperty(value="Reference", required=true)
        private int reference;
        
        @JsonProperty(value="AccountNumber", required=true)  
        private String account_number;
        
        @JsonProperty(value="Description", access=Access.WRITE_ONLY)
        private String description;
        
        @JsonProperty(value="Start Balance", access=Access.WRITE_ONLY)
        private BigDecimal start_balance;
        
        @JsonProperty(value="Mutation", access=Access.WRITE_ONLY)
        private BigDecimal mutation;
        
        @JsonProperty(value="End Balance", access=Access.WRITE_ONLY)
        private BigDecimal end_balance;

        public Account() {}
		/**
		 * @param reference
		 * @param account_number
		 */
		public Account(int reference, String account_number) {
			super();
			this.reference = reference;
			this.account_number = account_number;
		}

		/**
		 * @return the reference
		 */
		public int getReference() {
			return reference;
		}

		/**
		 * @param reference the reference to set
		 */
		public void setReference(int reference) {
			this.reference = reference;
		}

		/**
		 * @return the account_number
		 */
		public String getAccount_number() {
			return account_number;
		}

		/**
		 * @param account_number the account_number to set
		 */
		public void setAccount_number(String account_number) {
			this.account_number = account_number;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}

		/**
		 * @return the start_balance
		 */
		public BigDecimal getStart_balance() {
			return start_balance;
		}

		/**
		 * @param start_balance the start_balance to set
		 */
		public void setStart_balance(BigDecimal start_balance) {
			this.start_balance = start_balance;
		}

		/**
		 * @return the mutation
		 */
		public BigDecimal getMutation() {
			return mutation;
		}

		/**
		 * @param mutation the mutation to set
		 */
		public void setMutation(BigDecimal mutation) {
			this.mutation = mutation;
		}

		/**
		 * @return the end_balance
		 */
		public BigDecimal getEnd_balance() {
			return end_balance;
		}

		/**
		 * @param end_balance the end_balance to set
		 */
		public void setEnd_balance(BigDecimal end_balance) {
			this.end_balance = end_balance;
		}


}
