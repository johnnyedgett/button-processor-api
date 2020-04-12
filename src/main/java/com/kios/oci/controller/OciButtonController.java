package com.kios.oci.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.bmc.nosql.NosqlClient;
import com.oracle.bmc.nosql.model.TableSummary;
import com.oracle.bmc.nosql.requests.ListTablesRequest;

/* Right now using a lot of static variables for playing with nosql api. need to make this a bit more dynamic */
@RestController
@RequestMapping("/api")
public class OciButtonController {
    
    @Autowired
    NosqlClient dbClient;
    
    @Value("${COMPARTMENT_ID}")
    String compartmentId;
    
	@GetMapping("/tables")
	public ResponseEntity<List<Map<String, String>>> getButtonPressCount(@RequestParam(required = false) String user){
        ListTablesRequest request = ListTablesRequest.builder()
                .compartmentId(compartmentId)
                .build();
        List<TableSummary> tables = dbClient.listTables(request).getTableCollection().getItems();
        // Figure out how to stream & map this / use gson
        List<Map<String, String>> tablesData = new ArrayList<>();
        Iterator<TableSummary> it = tables.iterator();
        while(it.hasNext()) {
            Map<String, String> tableData = new HashMap<>();
            TableSummary cur = it.next();
            tableData.put("id", cur.getId());
            tableData.put("name", cur.getName());
            tablesData.add(tableData);
        }
		//	Do something with the user in the call to the database
		return new ResponseEntity<>(tablesData, HttpStatus.OK);
	}

}
