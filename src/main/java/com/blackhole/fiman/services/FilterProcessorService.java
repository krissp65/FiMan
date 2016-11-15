package com.blackhole.fiman.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.blackhole.fiman.filters.ProcessingFilter;

@Service
public class FilterProcessorService {

	private final static Logger log = LoggerFactory.getLogger(FilterProcessorService.class);
	
	private List<ProcessingFilter> filters = new ArrayList<>();
	
	public void registerFilter(ProcessingFilter filter) {
		if(filter != null) {
			filters.add(filter);
		}
	}
	
	public boolean unregisterFilter(ProcessingFilter filter) {
		if(filter != null)  {
			return filters.remove(filter);
		}
		
		return false;
	}
	
}
