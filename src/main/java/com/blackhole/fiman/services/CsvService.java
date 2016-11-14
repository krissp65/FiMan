package com.blackhole.fiman.services;

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.blackhole.fiman.documents.AccountOperation;

@Service
public class CsvService {

	private final static Logger log = LoggerFactory.getLogger(CsvService.class);
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public List<AccountOperation> parseCsv(Reader reader) throws IOException, ParseException {
		List<AccountOperation> parsed = null;
		AccountOperation op = null;
		
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').parse(reader);
		if(records != null) {
			parsed = new ArrayList<>();
			
			for(CSVRecord record : records) {
				int size = record.size();
				log.debug("Record consist of " + size + " elements.");
				
				op = new AccountOperation();
				op.setOperationDate(sdf.parse(cleanup(record.get(0))));
				op.setDescription(cleanup(record.get(1)));
				op.setOperationAmount(Float.parseFloat(cleanup(record.get(2))));
				op.setAccount(cleanup(record.get(5)));
				
				parsed.add(op);
				log.debug("Add operation: " + op);
			}
		}
		
		return parsed;
	}

	private String cleanup(String input) {
		String clean = null;
		
		clean = input.trim();
		clean = clean.replace("\"", "");
		clean = clean.replace(",", ".");
		
		return clean;
	}
}
