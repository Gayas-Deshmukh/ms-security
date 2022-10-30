package com.weshopify.platform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import com.weshopify.platform.features.customers.CustomerBean;
import com.weshopify.platform.features.customers.service.CustomerService;

@TestMethodOrder(OrderAnnotation.class)
public class CustomerServiceTest extends /*WeShopifyPlatformApplicationTests*/ CustomerRolesTest {

	@Autowired
	private CustomerService customerService;
	static CustomerBean customer = null;
	

	@BeforeEach
	//@BeforeAll
	public void init() {
		System.out.println("before each test case");
		if(customer != null && customer.getCustomerId() > 0) {
			System.out.println("customer is already created");
		}else {
			customer = new CustomerBean();
			customer.setEmail("john@yopmail.com");
			customer.setFirstName("John");
			customer.setLastName("Peter");
			customer.setMobileNumber("9876543210");
			customer.setPassword("testUser@123");
			customer.setSelfReg(true);
			customer.setUserName("johnPeter");
		}
	
	}

	/*
	 * @AfterEach public void destroy() { System.out.println("after each testcase");
	 * customer = null; }
	 */

	@Test
	@Order(value = 3)
	public void testSaveCustomer() {
		System.out.println("testSaveCustomer");
		customer = customerService.saveCustomer(customer);
		System.out.println("customer Id is:\t"+customer.getCustomerId());
		assertNotNull(customer);
		//assertNotNull(customer.getCustomerId());
		assertNotEquals(0, customer.getCustomerId());
		assertNotNull(customer.getRole());
		assertNotNull("customer", customer.getRole());
	}

	@Test
	@Order(value = 4)
	public void testUpdateCustomer() {
		System.out.println("testUpdateCustomer");
		String beforeUpdateEmail = customer.getEmail();
		customer.setEmail("johnPeter@yopmail.com");
		customer = customerService.updateCustomer(customer);
		assertNotNull(customer);
		assertNotNull(customer.getCustomerId());
		assertNotSame(beforeUpdateEmail, customer.getEmail());
		/*
		 * assertThatThrownBy(()->{ throw new RuntimeException();
		 * }).isInstanceOf(Exception.class) .hasMessage(message)
		 * 
		 * });
		 */
	}
	
	@Test
	@Order(value = 5)
	public void testFindAllCustomers() {
		System.out.println("testFindAllCustomers");
		List<CustomerBean> customerList = customerService.findAllCustomers();
		assertNotNull(customerList);
		assertTrue(customerList.size() >0);
		/*
		 * assertThatThrownBy(()->{ throw new RuntimeException();
		 * }).isInstanceOf(Exception.class) .hasMessage(message)
		 * 
		 * });
		 */
	}
	
	@Test
	@Order(value = 6)
	public void testFindCustomerById() {
		System.out.println("testFindCustomerById");
		CustomerBean savedCustomer = customerService.findCustomerById(customer.getCustomerId());
		assertNotNull(savedCustomer);
		assertTrue(savedCustomer.getCustomerId() >0);
		assertEquals(customer.getCustomerId(), savedCustomer.getCustomerId());
		/*
		 * assertThatThrownBy(()->{ throw new RuntimeException();
		 * }).isInstanceOf(Exception.class) .hasMessage(message)
		 * 
		 * });
		 */
	}
	
	@Test
	@Order(value = 7)
	public void testDeleteCustomer() {
		System.out.println("testDeleteCustomer");
		
		customerService.deleteCustomer(customer.getCustomerId());
		
		Throwable thrown = catchThrowable(() -> {
			customerService.findCustomerById(customer.getCustomerId());			
		});
		
		assertThat(thrown).as(thrown.getMessage())
		                   .isInstanceOf(Exception.class);
		
	}
}
