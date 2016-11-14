package com.blackhole.fiman.documents;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class BankAccount {
	
	@Id
	private String id;
	
	@Max(200)
	@NotNull
	private String bankName;
	
	@Max(60)
	@NotNull
	private String iban;
	
	@Max(200)
	@NotNull
	private String accountOwner;
	
	@NotNull
	private Date creationDate;
	
	@NotNull
	private double creationBalance;
	
	private double accountBalance;
}
