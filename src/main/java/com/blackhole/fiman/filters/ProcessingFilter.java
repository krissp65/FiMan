package com.blackhole.fiman.filters;

import java.util.List;

import com.blackhole.fiman.documents.AccountOperation;

public interface ProcessingFilter {
	
	public List<AccountOperation> filter(List<AccountOperation> operations);
	
	
}
