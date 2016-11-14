package com.blackhole.fiman;

import java.util.Date;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.blackhole.fiman.documents.BankAccount;
import com.blackhole.fiman.documents.BankAccountRepository;
import com.blackhole.fiman.documents.User;
import com.blackhole.fiman.documents.UserRepository;
import com.blackhole.fiman.security.SecurityHelper;

public class ApplicationEventListener implements ApplicationListener<ApplicationEvent> {

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ApplicationReadyEvent) {
			ApplicationContext ctx = ((ApplicationReadyEvent) event).getApplicationContext();
			
			UserRepository userRepository = ctx.getBean(UserRepository.class);
			User adminUser = userRepository.findByUsername("chris");
			if(adminUser == null) {
				adminUser = new User();
				adminUser.setFirstName("Chris");
				adminUser.setLastName("Pieszkur");
				adminUser.setUsername("chris");
				adminUser.setPasswordHash(SecurityHelper.hash("123456".toCharArray()));
				userRepository.save(adminUser);
			}
			
			BankAccountRepository accountRepository = ctx.getBean(BankAccountRepository.class);
			
			BankAccount account = accountRepository.findByBankNameIgnoreCase("Targo Bank AG");
			if(account == null) {
				account = new BankAccount();
				account.setBankName("Targo Bank AG");
				account.setIban("DE90300209000207651690");
				account.setAccountOwner("Chris Pieszkur");
				account.setCreationDate(new Date());
				account.setCreationBalance(0.0);
				account.setAccountBalance(0.0);
				accountRepository.save(account);
			}
		}
	}

}
