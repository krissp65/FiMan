package com.blackhole.fiman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.blackhole.fiman.documents.AccountOperation;
import com.blackhole.fiman.documents.AccountOperationRepository;
import com.blackhole.fiman.services.CsvService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FiManApplicationTests {

	@Autowired
	private CsvService csvService;
	
	@Autowired
	private AccountOperationRepository operationRepository;
	
	private Reader reader;
	
	private final static String CSV = "data/CHK_690.csv";
	
	@Before
	public void init() throws FileNotFoundException {
		reader = new FileReader(new File(CSV));
	}
	
	@Test
	public void parseCsvTest() throws IOException, ParseException, NoSuchAlgorithmException {
		List<AccountOperation> parsed = csvService.parseCsv(reader);
		
		assertNotNull(parsed);
		assertTrue(parsed.get(0) instanceof AccountOperation);
		
	}

}
