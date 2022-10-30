package com.weshopify.platform;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.weshopify.platform.features.customers.models.Customer;
import com.weshopify.platform.features.customers.repository.CustomerDataRepo;

public class CustomerDataRepoTest extends WeShopifyPlatformApplicationTests {

	@Autowired
	private CustomerDataRepo customerRepo;
	
	@Test
	public void testCustomerCreation() {
		Customer customer = new Customer();
		customer.setEmail("john@yopmail.com");
		customer.setFirstName("John");
		customer.setLastName("Peter");
		customer.setMobileNumber("9876543210");
		customer.setPassword("testUser@123");
		customer.setSelfReg(true);
		customer.setUserName("johnPeter");
		
		customer = customerRepo.save(customer);
		System.out.println("customer Id is:\t"+customer.getCustomerId());
		assertNotNull(customer.getCustomerId());
		
	}
}
