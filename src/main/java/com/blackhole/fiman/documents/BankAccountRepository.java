package com.blackhole.fiman.documents;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BankAccountRepository extends MongoRepository<BankAccount, String> {
	BankAccount findByBankNameIgnoreCase(String bankName);
}
