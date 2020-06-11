/**
 * 
 */
package com.rabo.payments.controller;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabo.payments.controller.StatementProcessingController;
import com.rabo.payments.model.Account;
import com.rabo.payments.model.AccountStatus;
import com.rabo.payments.model.ResultCode;
import com.rabo.payments.service.StatementProcessingService;
/**
 * 
 *   Unit test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StatementProcessingControllerTest {
	

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;
	  
	@MockBean
	StatementProcessingService statementProcessingService;
	
	@LocalServerPort
	private int port;
	

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    
	String exampleJson = "[{\"Reference\":123,\"AccountNumber\":\"IN123AWDJD23\","
			+ "\"Description\":\"Clothes for Tester\","
			+ "\"Start Balance\": 1.00,"
			+ "\"Mutation\": 1.00,"
			+ "\"End Balance\": 2.00}]";

	
	 @Test
 	 public void testCheckServerStatus() throws Exception {
	    HttpEntity<String> entity = new HttpEntity<String>(null, headers);
	    ResponseEntity<String> response = restTemplate.exchange(
	      createURLWithPort("/rabo/serverstatus"), HttpMethod.GET, entity, String.class);
	    String expected = "Welcome to RaboBank Payment Services!..";
	    assertEquals(expected, response.getBody());
	}
	 
	 // Test using MockMvc
	@Test
	public void testProcessStatement() throws Exception {

		// data and mock's behaviour
		List mockdata = new ArrayList<AccountStatus>();
		AccountStatus status = new AccountStatus();
		status.setError_records(new ArrayList());
		status.setResult(ResultCode.SUCCESSFUL.name());
		mockdata.add(status);

		when(statementProcessingService.processMonthlyStatement(any(List.class))).thenReturn(mockdata);

		ObjectMapper objectMapper = new ObjectMapper();
		Account mockaccount = new Account(123,"IN123AWDJD23");
        String json = objectMapper.writeValueAsString(mockaccount);
        
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/rabo/processStatement")
			    .contentType(MediaType.APPLICATION_JSON)
			    .content(exampleJson)
			    .characterEncoding("utf-8"))
			    .andExpect(MockMvcResultMatchers.status().isOk())
			    .andReturn();

		MockHttpServletResponse response = result.getResponse();

		String expected = "[{\"result\":\"SUCCESSFUL\",\"error_records\":[]}]";
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertNotNull(result);
		assertEquals("SUCCESSFUL",((AccountStatus) mockdata.get(0)).getResult());
		assertEquals(expected,response.getContentAsString());
		}

	@Test
	public void testProcessStatementForBadRequest() throws Exception {

		// data and mock's behaviour
		List mockdata = new ArrayList<AccountStatus>();
		AccountStatus status = new AccountStatus();
		Account mockaccount = new Account(123,"IN123AWDJD23");
		List<Account> responseList = new ArrayList<Account>();
		responseList.add(mockaccount);
		status.setError_records(responseList);
		status.setResult(ResultCode.BAD_REQUEST.name());
		mockdata.add(status);

		when(statementProcessingService.processMonthlyStatement(any(List.class))).thenReturn(mockdata);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/rabo/processStatement")
			    .contentType(MediaType.APPLICATION_JSON)
			    .content(exampleJson)
			    .characterEncoding("utf-8"))
			    .andExpect(MockMvcResultMatchers.status().isOk())
			    .andReturn();	

		MockHttpServletResponse response = result.getResponse();

		String expected = "[{\"result\":\"BAD_REQUEST\",\"error_records\":["
				+ "{\"Reference\":123,\"AccountNumber\":\"IN123AWDJD23\"}"
				+ "]}]";
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertNotNull(result);
		assertEquals("BAD_REQUEST",((AccountStatus) mockdata.get(0)).getResult());
		assertEquals(expected,response.getContentAsString());

		}

	@Test
	public void testUploadFile() throws Exception {

		// data and mock's behaviour
		List mockdata = new ArrayList<AccountStatus>();
		AccountStatus status = new AccountStatus();
		status.setError_records(new ArrayList());
		status.setResult(ResultCode.SUCCESSFUL.name());
		mockdata.add(status);

		when(statementProcessingService.processMonthlyStatement(any(List.class))).thenReturn(mockdata);
		MockMultipartFile mockFile = new MockMultipartFile("file", "temp.json", "application/json", exampleJson.getBytes());

		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart("/rabo/uploadStatement")
                        .file(mockFile))
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(content().string("[{\"result\":\"SUCCESSFUL\",\"error_records\":[]}]"));
        
		assertEquals("SUCCESSFUL",((AccountStatus) mockdata.get(0)).getResult());
	}

	 
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
