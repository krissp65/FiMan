package com.blackhole.fiman.filters;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackhole.fiman.documents.AccountOperation;
import com.blackhole.fiman.documents.AccountOperationRepository;

public class DuplicateSearchFilter implements ProcessingFilter {

	private final static Logger log = LoggerFactory.getLogger(DuplicateSearchFilter.class);
	
	private AccountOperationRepository repository;
	
	public DuplicateSearchFilter(AccountOperationRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public List<AccountOperation> filter(List<AccountOperation> operations) {
		List<AccountOperation> filtered = null;
		
		if(operations != null) {
			for(AccountOperation operation : operations) {
				AccountOperation op = repository.findByHash(operation.getHash());
				if(op == null) {
					if(filtered == null) {
						filtered = new ArrayList<>();
					}
					filtered.add(operation);
				}
			}
			log.info(filtered.size() +  " new account operations found.");
		} else {
			log.info("No new account operations found.");
		}
		
		return filtered;
	}

}
