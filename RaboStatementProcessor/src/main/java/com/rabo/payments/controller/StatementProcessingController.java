package com.rabo.payments.controller;
import java.io.File;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabo.payments.model.Account;
import com.rabo.payments.model.AccountStatus;
import com.rabo.payments.exception.StatementParsingException;
import com.rabo.payments.service.StatementProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/rabo")
	
public class StatementProcessingController {
	
		private final Logger logger = LoggerFactory.getLogger(this.getClass());

		@Autowired
		private StatementProcessingService statementProcessingService;
		
		@GetMapping("/serverstatus")
		public String checkServerStatus()
		{
			return "Welcome to RaboBank Payment Services!..";
		}
	
		@PostMapping(value="/processStatement", consumes = {MediaType.APPLICATION_JSON_VALUE},
												produces = {MediaType.APPLICATION_JSON_VALUE})
		public List<AccountStatus> processStatement(@RequestBody List<Account> accountsData) throws StatementParsingException
		{
			logger.info("Processing account statements..");
			
	        List<AccountStatus>  responseList = statementProcessingService.processMonthlyStatement(accountsData);
	        
	        logger.info("Statement validation done!..");
			return responseList;
		}
		
		@PostMapping(value="/uploadStatement",
										  produces = {MediaType.APPLICATION_JSON_VALUE})
	    public List<AccountStatus>  uploadFile(@RequestParam("file") MultipartFile multiPartFile) 
	    throws StatementParsingException {
	        logger.info("Processing uploaded statements..");
	        
			List<Account> accountsData = null;
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);	
				
			    File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+multiPartFile.getOriginalFilename());
			    
			    multiPartFile.transferTo(convFile);
			    
			    JsonParser jp = new JsonFactory().createParser(convFile);
			    TypeReference<List<Account>> tRef = new TypeReference<List<Account>>() {};
			    accountsData = mapper.readValue(jp, tRef);
				    
			}catch(IOException ioe) {
				logger.error("Error on parsing input file...");
				throw new StatementParsingException("Error parsing json file!..",ioe);
			}
	        
			List<AccountStatus>  responseList = statementProcessingService.processMonthlyStatement(accountsData);
	        
	        logger.info("Uploaded statement validation done!..");
	        return responseList;
		}

}
