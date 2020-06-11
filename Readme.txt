==================================================================================================
				Rabo Statement Processor
==================================================================================================

Logic used:
==========
	This application written in Spring boot deals with two scenarios, one to handle plain raw JSON text and another to handle statement in Json file.
	Added one more sample method to test the server running status.(using /serverstatus)

1. Plain Json:
	This part took the JSON text and validate on various scenarios and generate response based on it.

2. Json File:
	This part took the multipart JSON file, parse it to JSON object array and proceed with the validations as above and  generate response JSON.

	Validation part:
	===============
		1. Check If reference Id present in referenceID list, if not send status as 'SUCCESSFUL' and add it to referenceID list.
		2. If id avail in the list, return status as 'DUPLICATE_REFERENCE' along with account details.
		3. Validate the transaction as End balance should be the summation of start balance and Mutation performed, 
		   if not return status as 'INCORRECT_END_BALANCE' along with account details.
		4. If a record fails in both 2 & 3 rd scenarios, return status as 'DUPLICATE_REFERENCE_INCORRECT_END_BALANCE' along with thier account details.
		5. If any exception occurs while parsing json or any existence of bad data, return status as 'BAD_REQUEST'
		6. any other exception occurs, return status as 'INTERNAL_SERVER_ERROR'


Wrote Unit test for StatementProcessingController as well.

Technologies used: Maven, Java 8, Spring boot, MockMVC, SLF4j Loggers


Steps to test the application:
==============================
Step 1: Import the 'RaboStatementProcessor' project in Eclipse /STS.

Step 2: Run as -> Maven build (goal: clean install)  -> once build complete, Run as -> Spring Boot app

	Check the existence of entry like as 'Started RaboStatementProcessorApplication ' in eclipse log console.

Step 3: Open Postman app and proceed with the below scenarios

To check for application response:
---------------------------------

	GET: http://localhost:8090/rabo/serverstatus

To test the account statement scenarios:

To Pass input as Raw text:
==========================

	POST: http://localhost:8090/rabo/processStatement

	1. Add header 'content-type' as 'application/json' in Header section.

	2. In Body tab, select Body type as 'raw' and input format as 'JSON(application/json)'  (Avail next to binary checkbox).

	3. paste your Json input text and click SEND.


To upload a Json file :
=======================

	POST: http://localhost:8090/rabo/uploadStatement

	1. Pls uncheck all the headers. No header should be present.

	2. In Body tab, select Body type as Form data -> enter key as 'file' and value type as File (Avail next to Key textbox).

	3. In value part, click 'choose files' button, select you input file and Send the request.