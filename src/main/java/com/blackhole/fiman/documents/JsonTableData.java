package com.blackhole.fiman.documents;

import java.util.List;

import lombok.Data;

@Data
public class JsonTableData {
	private long recordsTotal;
	private long recordsFiltered;
	private int draw;
	private List<AccountOperation> data;
}
