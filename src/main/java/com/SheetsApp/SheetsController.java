package com.SheetsApp.controller;


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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public ResponseEntity<String> getAvaliability() throws IOException, GeneralSecurityException {
    	setup();
    	//= restTemplate.getForEntity(uri, String.class);
		List<String> ranges = Arrays.asList("B2","C2");
		BatchGetValuesResponse readResult = sheetsService.spreadsheets().values()
											.batchGet(SPREADSHEET_ID)
											.setRanges(ranges)
											.execute();

		ValueRange b2 = readResult.getValueRanges().get(0);
		String result = (String) b2.getValues().get(0).get(0);
		return(new ResponseEntity<> (result,HttpStatus.OK));									
    }



    
}