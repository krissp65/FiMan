package com.blackhole.fiman.controller;

import java.util.List;

import javax.naming.ldap.SortControl;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blackhole.fiman.documents.AccountOperation;
import com.blackhole.fiman.documents.AccountOperationRepository;
import com.blackhole.fiman.documents.BankAccount;
import com.blackhole.fiman.documents.BankAccountRepository;
import com.blackhole.fiman.documents.JsonTableData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DashboardController {
	
	private final static Logger log = LoggerFactory.getLogger(DashboardController.class);
	
	@Autowired
	private BankAccountRepository accountRepository;
	
	@Autowired
	private AccountOperationRepository operationRepository;

	@GetMapping(value="/login")
	public String login() {
		return "dashboard";
	}
	
	@GetMapping(value={"", "/", "/dashboard"})
	public String dashboard(Model model) {
		
		List<BankAccount> accounts = accountRepository.findAll();
		model.addAttribute("accounts", accounts);
		
		return "dashboard";
	}
	
	@GetMapping(value="/operations.data")
	@ResponseBody
	public String operationsData(HttpServletRequest request) throws JsonProcessingException {
		int iDraw = Integer.parseInt(request.getParameter("draw"));
		int iLength = Integer.parseInt(request.getParameter("length"));
		int iStart = Integer.parseInt(request.getParameter("start"));
		String search = request.getParameter("search[value]");
		
		int iOrderColumn = Integer.parseInt(request.getParameter("order[0][column]"));
		String orderDir = request.getParameter("order[0][dir]");
		long operationsTotalCount = operationRepository.count();

		int pageNumber = (iStart/iLength);

		Sort.Direction sort_dir = orderDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : orderDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		String sort_column = getSortColumn(iOrderColumn);

		Page<AccountOperation> operations = null;

		if(search == null || search.isEmpty()) {
			operations = operationRepository.findAll(new PageRequest(pageNumber, iLength, sort_dir, sort_column));
		}
		
		JsonTableData data = new JsonTableData();
		data.setDraw(iDraw);
		data.setRecordsTotal(operationsTotalCount);
		data.setRecordsFiltered(operations.getTotalElements());
		data.setData(operations.getContent());
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonData = mapper.writer().writeValueAsString(data);
		
		return jsonData;
	}
	
	@GetMapping(value="/operations/{iban}")
	public String operations(@PathVariable(name="iban") String iban, Model model) {
		log.debug("operations for account " + iban);
		List<AccountOperation> operations = operationRepository.findAll();
		model.addAttribute("operations", operations);
		model.addAttribute("iban", iban);
		return "operationslist";
	}
	
	private String getSortColumn(int iOrderColumn) {
		String colName = "id";

		switch(iOrderColumn) {
		case 0 :
			colName = "id";
			break;
		case 1:
			colName = "operationDate";
			break;
		case 2:
			colName = "operationAmount";
			break;
		case 3:
			colName = "tag";
			break;
		case 4:
			colName = "description";
			break;
		}

		return colName;
	}

}
