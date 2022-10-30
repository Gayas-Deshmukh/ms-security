/**
 * 
 */
package com.weshopify.platform.features.customers.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.weshopify.platform.features.customers.CustomerBean;
import com.weshopify.platform.features.customers.commons.CustomerSearchOptions;
import com.weshopify.platform.features.customers.models.Customer;
import com.weshopify.platform.features.customers.repository.CustomerDataRepo;
import com.weshopify.platform.features.customers.repository.UserRolesDataRepo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author IM-LP-1763
 *
 */
@Service
@Slf4j
//@Transactional
public class CustomerServiceImpl implements CustomerService {

	// private static Set<CustomerBean> IN_MEMORY_DB = new HashSet<CustomerBean>();
	private static Map<Integer, CustomerBean> IN_MEMORY_DB = new HashMap<Integer, CustomerBean>();
	
	private CustomerDataRepo customerRepo;
	

	@Autowired
	private UserRolesDataRepo rolesDataRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public CustomerServiceImpl(CustomerDataRepo customerRepo) {
		this.customerRepo = customerRepo;
	}

	/**
	 * if the id is there in the customer bean then update the database with the
	 * data that is coming inside the customer bean else save it as new customer
	 */
	@Override
	public CustomerBean saveCustomer(CustomerBean customerBean) {

		/*
		 * if(customerBean.getCustomerId() != 0) {
		 * IN_MEMORY_DB.put(customerBean.getCustomerId(),customerBean); }else { int
		 * customerId = new Random().nextInt(); customerBean.setCustomerId(customerId);
		 * IN_MEMORY_DB.put(customerId,customerBean); }
		 */
		/**
		 * Step-1: Convert the bean data to domain to save in database
		 * 
		 */
		Customer customerDomain = new Customer();
		BeanUtils.copyProperties(customerBean, customerDomain);
		customerDomain.setPassword(passwordEncoder.encode(customerDomain.getPassword()));
		customerDomain.setRole(rolesDataRepo.getById(1));
		customerRepo.save(customerDomain);

		/**
		 * Step-2: Convert the domain data to bean again beacuse domain might have the
		 * auto generated ID to set it back to the bean to send it again for the futher
		 * CRD operations
		 */
		customerBean.setCustomerId(customerDomain.getCustomerId());
		customerBean.setRole(customerDomain.getRole().getRole());
		BeanUtils.copyProperties(customerDomain, customerBean);
		return customerBean;
	}

	@Override
	public CustomerBean updateCustomer(CustomerBean customerBean) {
		// IN_MEMORY_DB.put(customerBean.getCustomerId(),customerBean);
		if (customerBean.getCustomerId() > 0) {
			return saveCustomer(customerBean);
		} else {
			throw new RuntimeException("Updating Customer applied on existing customers only");
		}

	}

	
	@Override
	public List<CustomerBean> findAllCustomers() {
		// TODO Auto-generated method stub
		List<CustomerBean> customerList = new ArrayList<>();
		Iterable<Customer> customerIterator = customerRepo.findAll();

		customerIterator.forEach(customer -> {
			CustomerBean customerBean = new CustomerBean();
			BeanUtils.copyProperties(customer, customerBean);
			customerList.add(customerBean);

		});
		return customerList;
	}

	@Override
	public CustomerBean findCustomerById(int customerId) {
		Optional<Customer> opt = customerRepo.findById(customerId);
		CustomerBean customerBean = new CustomerBean();
		opt.ifPresentOrElse((customer) -> {

			BeanUtils.copyProperties(customer, customerBean);
		},

				() -> {
					throw new RuntimeException("No Customer Found with the CustomerId:\t" + customerId);
				});

		return customerBean;

	}

	/*
	 * @Transactional(isolation = Isolation.READ_COMMITTED, propagation =
	 * Propagation.REQUIRED, rollbackFor = Throwable.class)
	 */
	@Override
	public List<CustomerBean> deleteCustomer(int customerId) {
		customerRepo.deleteById(customerId);
		return findAllCustomers();
	}

	@Override
	public List<CustomerBean> searchCustomer() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED, 
			propagation = Propagation.NOT_SUPPORTED, 
			rollbackFor = Throwable.class)
	 
	@Override
	public List<CustomerBean> findAllCustomers(int currentPage, int noOfRecPerPage) {

		Pageable page = PageRequest.of(currentPage, noOfRecPerPage);
		Page<Customer> customerPages = customerRepo.findAll(page);

		List<Customer> customerDomainList = customerPages.getContent();

		// List<CustomerBean> customersList = null;
		if (customerDomainList != null && customerDomainList.size() > 0) {
			List<CustomerBean> customersList = new ArrayList<>();
			customerDomainList.stream().forEach(customerDomain -> {
				CustomerBean customerBean = new CustomerBean();
				BeanUtils.copyProperties(customerDomain, customerBean);
				customersList.add(customerBean);
			});
			return customersList;
		}

		return null;
	}

	@Override
	public List<CustomerBean> seachAllCustomers(String searchKey, String searchText) {

		String searchProperty = CustomerSearchOptions.getSearchName(searchKey);
		if (searchProperty != null && !searchProperty.isEmpty()) {
			try {
				log.info("search Key is:\t" + searchProperty);
				log.info("search text with the like operator is:\t" + searchText);
				List<Customer> customerDomainList = null;
				
				
				
				if (CustomerSearchOptions.BY_EMAIL.getKey().equals(searchKey)) {
					customerDomainList = customerRepo.searchByEmail(searchText);
				} else if (CustomerSearchOptions.BY_Mobile.getKey().equals(searchKey)) {
					customerDomainList = customerRepo.searchByMobile(searchText);					
				} else {
					customerDomainList = customerRepo.searchByUserName(searchText);
				}
				if (customerDomainList != null && customerDomainList.size() > 0) {
					List<CustomerBean> customersList = new ArrayList<>();
					customerDomainList.stream().forEach(customerDomain -> {
						CustomerBean customerBean = new CustomerBean();
						BeanUtils.copyProperties(customerDomain, customerBean);
						customersList.add(customerBean);
					});
					return customersList;
				} else {
					throw new RuntimeException("No Customer Found with the given search criterial:\t" + searchKey);
				}

			} catch (Exception e) {
				// e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}

		} else {
			throw new RuntimeException(
					"given search criteria:\t" + searchKey + "\tis invalid. please search with the valid option");
		}

	}

}
