package com.weshopify.platform.features.customers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.weshopify.platform.features.customers.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CustomerController {

	//Logger log = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerService customerService;
	
	private TransactionDefinition txDef;
	
	@Autowired
	private PlatformTransactionManager txManager;
	
	@RequestMapping(value = { "/selfReg" }, method = RequestMethod.GET)
	public String customerSelfSignupPage() {
		log.info("in render signup page method");
		return "sign-up.html";
	}
	
	@RequestMapping(value = { "/customer-admin-reg" }, method = RequestMethod.GET)
	public String customerAdminSignupPage(Model model) {
		log.info("in customer-admin-reg");
		model.addAttribute("customer", new CustomerBean());
		return "customer-admin-reg.html";
	}
	
	@RequestMapping(value = { "/access-denied" }, method = RequestMethod.GET)
	public String customerAccessDenied(Model model) {
		log.info("in customer Access Denied");
		//model.addAttribute("customer", new CustomerBean());
		int currentPage = 0;
		int NoOfRecPerPage = 5;
		List<CustomerBean> customersList = customerService.findAllCustomers(currentPage,NoOfRecPerPage);
		model.addAttribute("customersList", customersList);
		model.addAttribute("currentPage", currentPage+1);
		model.addAttribute("NoOfRecPerPage", NoOfRecPerPage);
		
		txDef = new DefaultTransactionDefinition(Propagation.REQUIRED.value());
		System.out.println("current transaction isolation level is:\t"+txDef.getIsolationLevel());
		System.out.println("propagation behaviour is:\t"+txDef.getPropagationBehavior());
		System.out.println("Time Out of the transaction is:\t"+txDef.getTimeout());
		TransactionStatus txStatus = txManager.getTransaction(txDef);
		txManager.commit(txStatus);
		System.out.println("Transaction status is:\t"+txStatus.isCompleted());
		System.out.println("does the transaction has the save point:\t"+txStatus.hasSavepoint());
		
		return "access-denied.html";
	}
	
	@RequestMapping(value = { "/view-customers" }, method = RequestMethod.GET)
	@Transactional(isolation = Isolation.READ_COMMITTED, 
					propagation =Propagation.REQUIRED, 
					rollbackFor = Throwable.class,
					timeout = 3,
					readOnly = true)
	 
	public String viewCustomersPage(Model model) {
		log.info("in render signup page method");
		
		Authentication  authenticate =  SecurityContextHolder.getContext().getAuthentication();
		
		String loggedinUser = authenticate.getName();
		log.info("LoggedIn user Name : " + loggedinUser);
		log.info("Is Authenticated : " + authenticate.isAuthenticated());
		
		authenticate.getAuthorities().forEach(authority -> {
			log.info("Authority : " + authority.getAuthority());
		});

		
		int currentPage = 0;
		int NoOfRecPerPage = 5;
		List<CustomerBean> customersList = customerService.findAllCustomers(currentPage,NoOfRecPerPage);
		model.addAttribute("customersList", customersList);
		model.addAttribute("currentPage", currentPage+1);
		model.addAttribute("NoOfRecPerPage", NoOfRecPerPage);
		
		txDef = new DefaultTransactionDefinition(Propagation.REQUIRED.value());
		System.out.println("current transaction isolation level is:\t"+txDef.getIsolationLevel());
		System.out.println("propagation behaviour is:\t"+txDef.getPropagationBehavior());
		System.out.println("Time Out of the transaction is:\t"+txDef.getTimeout());
		TransactionStatus txStatus = txManager.getTransaction(txDef);
		txManager.commit(txStatus);
		System.out.println("Transaction status is:\t"+txStatus.isCompleted());
		System.out.println("does the transaction has the save point:\t"+txStatus.hasSavepoint());
		
		return "customer.html";
	}
	
	@RequestMapping(value = { "/view-customers/{currentPage}/{NoOfRecPerPage}" }, method = RequestMethod.GET)
	public String viewCustomersPageByPagination(Model model,@PathVariable("currentPage") int currentPage,
			@PathVariable("NoOfRecPerPage") int NoOfRecPerPage) {
		log.info("viewCustomersPageByPagination");
		
		List<CustomerBean> customersList = customerService.findAllCustomers(currentPage,NoOfRecPerPage);
		model.addAttribute("currentPage", currentPage+1);
		model.addAttribute("NoOfRecPerPage", NoOfRecPerPage);
		model.addAttribute("customersList", customersList);
		return "customer.html";
	}
	
	@PreAuthorize("hasAuthority('admin')")
	@RequestMapping(value = { "/delete-customers/{customerId}" }, method = RequestMethod.GET)
	  @Transactional(isolation = Isolation.READ_COMMITTED, propagation =
	  Propagation.REQUIRED, rollbackFor = Throwable.class)
	public String deleteCustomers(@PathVariable("customerId") int customerId, Model model) {
		
		log.info("deleteing the customet by the customer Id {}",customerId);
		CustomerBean customer = customerService.findCustomerById(customerId);
		customer.setSelfReg(true);
		customerService.updateCustomer(customer);
		List<CustomerBean> customersList = customerService.deleteCustomer(customerId);
		model.addAttribute("customersList", customersList);
		return "customer.html";
	}


	@RequestMapping(value = { "/create-customer" }, method = RequestMethod.POST)
	public String createCustomer(@ModelAttribute("customer") @Valid CustomerBean customer, BindingResult validationResult,Model model) {
		List<String> errorList = new ArrayList<String>();
		log.info("is Customer Self Registered:\t"+customer.isSelfReg());
		log.info(customer.toString());
		
		if(validationResult.hasErrors()) {
			log.info("Data entered by Users contains the errors ");
			if(customer.isSelfReg()) {
				return "sign-up.html";
			}else {
				return "customer-admin-reg.html";
			}
			
		}
		customer = customerService.saveCustomer(customer);
		if (customer.getCustomerId() > 0) {

			String message = "User Registration Successfull. Proceed with the Login Login";
			model.addAttribute("message", message);
		}
		if(customer.isSelfReg()) {
			return "sign-up.html";
		}else {
			return "redirect:/view-customers";
		}
		
	}
	
	@RequestMapping(value = { "/selfReg-customer" }, method = RequestMethod.POST)
	public String customerSelfreg(@ModelAttribute("customer") @Valid CustomerBean customer, BindingResult validationResult,Model model) {
		List<String> errorList = new ArrayList<String>();
		log.info("is Customer Self Registered:\t"+customer.isSelfReg());
		log.info(customer.toString());
		
		if(validationResult.hasErrors()) {
			log.info("Data entered by Users contains the errors ");
			if(customer.isSelfReg()) {
				return "sign-up.html";
			}else {
				return "customer-admin-reg.html";
			}
			
		}
		customer = customerService.saveCustomer(customer);
		if (customer.getCustomerId() > 0) {

			String message = "User Registration Successfull. Proceed with the Login Login";
			model.addAttribute("message", message);
		}
		if(customer.isSelfReg()) {
			return "sign-up.html";
		}else {
			return "redirect:/view-customers";
		}
		
	}
	
	@RequestMapping(value = { "/edit-customers/{customerId}" }, method = RequestMethod.GET)
	public String editCustomer(@PathVariable("customerId") int customerId, Model model) {
		log.info("customer ID in Edit Customer Page:\t"+customerId);
		CustomerBean customer = customerService.findCustomerById(customerId);
		model.addAttribute("customer",customer);
		return "customer-admin-reg.html";
		
	}
	
	@RequestMapping(value = { "/search-customers" }, method = RequestMethod.POST)
	public String searchCustomer(@RequestParam("searchKey") String searchKey, 
			@RequestParam("searchText") String searchText,Model model) {
		log.info("searchCustomer searchKey is:\t"+searchKey);
		log.info("searchCustomer searchText is:\t"+searchText);
		
		List<CustomerBean> customersList = customerService.seachAllCustomers(searchKey, searchText);
		/*
		 * model.addAttribute("currentPage", currentPage+1);
		 * model.addAttribute("NoOfRecPerPage", NoOfRecPerPage);
		 */
		model.addAttribute("customersList", customersList);
		return "customer.html";
		
	}

}
