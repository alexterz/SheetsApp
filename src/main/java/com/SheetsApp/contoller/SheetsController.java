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


    private static final String SPREADSHEET_ID = "15vh1aoHC6dXAf2mT6aNjPyY7DlWPu97mQyXRqAy-RKk";



    @Autowired
    RestTemplate restTemplate;


    @GetMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CoWorker>> getAvailability() throws IOException, GeneralSecurityException {
    	//setup OAuth2
    	setup();
		List<String> ranges = Arrays.asList("Sheet1");
		BatchGetValuesResponse readResult = sheetsService.spreadsheets().values()
											.batchGet(SPREADSHEET_ID)
											.setRanges(ranges)
											.execute();

		ValueRange all = readResult.getValueRanges().get(0);

		return(new ResponseEntity<> (saveDataToSpringApp(all),HttpStatus.OK));									
    }

    private List<CoWorker> saveDataToSpringApp(ValueRange all){
    	//all.getValues() is of type List<List<Object>>   
    	int countRows = all.getValues().size();
    	List<CoWorker> coWorkers = new ArrayList();

    	List<Object> row ; 
    	for(int i = 3; i<countRows; i ++ ){
	    	row =  all.getValues().get(i);
	    	coWorkers.add(putAvailability(row));  //return putAvailability(row);
	    }
	    return coWorkers;

    }


    private CoWorker putAvailability(List<Object> excelData){
    	// GET request to find coWorker By Id 
    	String id = (String)excelData.get(0);
    	String theUrl ="http://localhost:8080/api/coWorker/"+id ;
    	Integer hours;

    	//get CoWorker object with the corresponding id
    	ResponseEntity<CoWorker> getResponse = restTemplate.exchange(theUrl, HttpMethod.GET, null, CoWorker.class);
    	if (getResponse.getStatusCodeValue()!=200){
    		// POSTcoWorkerToSpringApp
    		//return ("Cant find coWorker with id:"+id);
    		return null;
    	}
        CoWorker coWorker = getResponse.getBody();	

        //extract availability from excel
    	List<Integer> availability = new ArrayList();
    	int length = excelData.size(); //days of a month
    	for(Integer i=3; i<length; i++){
    		hours = Integer.parseInt ((String) excelData.get(i));
    		availability.add(hours); 
    	}
 
    	coWorker.setAvailability(availability);

    	//PUT request 
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
    	ResponseEntity<CoWorker> putResponse = restTemplate.exchange(putUrl, HttpMethod.PUT, entity, CoWorker.class, id);
   		if (putResponse.getStatusCodeValue()!=200){
    		return null;
    	}


        
        return coWorker;


    }

    
}