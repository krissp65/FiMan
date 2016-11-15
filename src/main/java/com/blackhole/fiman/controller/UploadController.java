package com.blackhole.fiman.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.BufferUnderflowException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blackhole.fiman.documents.AccountOperation;
import com.blackhole.fiman.documents.AccountOperationRepository;
import com.blackhole.fiman.filters.DuplicateSearchFilter;
import com.blackhole.fiman.services.CsvService;

@Controller
public class UploadController {
	
	private final static Logger log = LoggerFactory.getLogger(UploadController.class);
	
	@Autowired
	private CsvService csvService;
	
	@Autowired
	private AccountOperationRepository operationRepository;
	
	@PostMapping("/operations/upload")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("iban") String iban, RedirectAttributes redirectAttributes) {
		log.debug("upload file info  [" + file.getOriginalFilename() + ", " + file.getSize() + "]" ) ;
		
		if(!file.isEmpty()) {
			
			Reader reader = null;
			
			
			List<AccountOperation> operations = null;
			try {
				reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "ISO-8859-1"));

				operations = csvService.parseCsv(reader);
				
				DuplicateSearchFilter dsf = new DuplicateSearchFilter(operationRepository);
				
				operations = dsf.filter(operations);
				
				operationRepository.save(operations);
				
				redirectAttributes.addFlashAttribute("flashMessage", "Import processed successfully for file: " + file.getOriginalFilename());

			} catch (IOException | ParseException | NoSuchAlgorithmException e) {
				redirectAttributes.addFlashAttribute("flashMessage", "Error on importing file '" + file.getOriginalFilename() + "' [" + e.getLocalizedMessage() + "].");
			}
			
		} else {
			redirectAttributes.addFlashAttribute("flashMessage", "Please select file for import first.");
		}
		
		
		return "redirect:/operations/" + iban;
	}
}
