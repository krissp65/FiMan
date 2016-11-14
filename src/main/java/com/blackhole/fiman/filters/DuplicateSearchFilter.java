package com.blackhole.fiman.filters;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackhole.fiman.documents.AccountOperation;

public class DuplicateSearchFilter implements ProcessingFilter {

	private final static Logger log = LoggerFactory.getLogger(DuplicateSearchFilter.class);
	
	@Override
	public List<AccountOperation> filter(List<AccountOperation> operations) {
		
		return null;
	}

}
