package com.blackhole.fiman.documents;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountOperationRepository extends MongoRepository<AccountOperation, String> {
	AccountOperation findByHash(String hash);
}
