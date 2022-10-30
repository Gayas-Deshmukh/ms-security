package com.weshopify.platform;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.web.client.RestTemplate;

import com.zaxxer.hikari.HikariDataSource;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WeShopifyPlatformApplication implements CommandLineRunner{

	@Autowired
	private HikariDataSource ds;
	
	@Autowired
	private TransactionDefinition txDef;
	
	public static void main(String[] args) {
		SpringApplication.run(WeShopifyPlatformApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder( ) {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void run(String... args) throws Exception {
		
		System.out.println(ds.getConnection().getClientInfo());
		Properties props = ds.getConnection().getClientInfo();
		System.out.println(ds.getConnection().toString());
		System.out.println(ds.getCatalog());
		System.out.println("maximum pool size is:\t"+ds.getMaximumPoolSize());
		for(Object key: props.keySet()) {
			System.out.println(props.get(key));
		}
		
		System.out.println("current transaction isolation level is:\t"+txDef.getIsolationLevel());
		System.out.println("propagation behaviour is:\t"+txDef.getPropagationBehavior());
		System.out.println("Time Out of the transaction is:\t"+txDef.getTimeout());
	}

}
