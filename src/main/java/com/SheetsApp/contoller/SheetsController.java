package com.SheetsApp.controller;


import com.SheetsApp.model.CoWorker;
import com.SheetsApp.sheets.SheetsServiceUtil; 
import java.io.IOException;
import java.security.GeneralSecurityException;
//import com.SheetsApp.model.CoWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import io.vavr.Tuple2;
import io.vavr.Tuple;

import javax.validation.Valid;
import org.springframework.validation.BindingResult;


//import com.SheetsApp.sheets;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.CopyPasteRequest;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.UpdateSpreadsheetPropertiesRequest;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

//import static org.assertj.core.api.Assertions.*;


 



@RestController
@RequestMapping(value = "/api/cells")
public class SheetsController {

    private static Sheets sheetsService; // = SheetsServiceUtil.getSheetsService();


    public static void setup() throws GeneralSecurityException, IOException {
        sheetsService = SheetsServiceUtil.getSheetsService();
    }

    // this id can be replaced with your spreadsheet id
    // otherwise be advised that multiple people may run this test and update the public spreadsheet
    private static final String SPREADSHEET_ID = "15vh1aoHC6dXAf2mT6aNjPyY7DlWPu97mQyXRqAy-RKk";



    @Autowired
    RestTemplate restTemplate;


    @GetMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CoWorker> getAvailability() throws IOException, GeneralSecurityException {
    	//setup OAuth2
    	setup();
		List<String> ranges = Arrays.asList("Sheet1!4:5");
		BatchGetValuesResponse readResult = sheetsService.spreadsheets().values()
											.batchGet(SPREADSHEET_ID)
											.setRanges(ranges)
											.execute();

		ValueRange all = readResult.getValueRanges().get(0);
		List<Object> row =  all.getValues().get(0);
		//String response = saveDataToSpringApp (row);
		//return(new ResponseEntity<> (response,HttpStatus.OK));
		return(new ResponseEntity<> (saveDataToSpringApp(row),HttpStatus.OK));									
    }


    private CoWorker saveDataToSpringApp(List<Object> excelData){
    	// GET request to find coWorker By Id 
    	String id = (String)excelData.get(0);
    	String theUrl ="http://localhost:8080/api/coWorker/"+id ;
    	Integer hours;

    	//get CoWorker object with the corresponding id
    	ResponseEntity<CoWorker> getResponse = restTemplate.exchange(theUrl, HttpMethod.GET, null, CoWorker.class);
    	if (getResponse.getStatusCodeValue()!=200){
    		//return ("Cant find coWorker with id:"+id);
    		return null;
    	}
        CoWorker coWorker = getResponse.getBody();	

        //extract availability from excel
    	List<Integer> availability = new ArrayList();
    	for(Integer i=3; i<32; i++){
    		hours = Integer.parseInt ((String) excelData.get(i));
    		availability.add(hours); 
    	}
    	//Stream <Tuple2<Integer,Integer>> streamAvailability = availability.stream();
 
    	coWorker.setAvailability(availability);

    	//PUT request to update 
    	String putUrl = "http://localhost:8080/api/coWorker/"+id ;

	    // create headers
	    HttpHeaders headers = new HttpHeaders();
	    // set `content-type` header
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    // set `accept` header
	   // headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

	    // build the request
	    HttpEntity<CoWorker> entity = new HttpEntity<>(coWorker, headers);

	    // send PUT request to update CoWorker with `id` 
	  //  ResponseEntity<Post> response = this.restTemplate.exchange(url, HttpMethod.PUT, entity, Post.class, 10);

    	ResponseEntity<CoWorker> putResponse = restTemplate.exchange(putUrl, HttpMethod.PUT, entity, CoWorker.class, id);
   		if (putResponse.getStatusCodeValue()!=200){
    		return null;
    	}


        
        return coWorker;


    }

    
}