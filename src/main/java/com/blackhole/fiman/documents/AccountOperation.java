package com.blackhole.fiman.documents;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.blackhole.fiman.utils.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Data
@Document
public class AccountOperation {
	
	@Id
	private String id;
	
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	@JsonSerialize(using = CustomDateSerializer.class) 
	private Date operationDate;
	
	private String description;
	
	private float operationAmount;
	
	private String account;
	
	private String tag;
}
