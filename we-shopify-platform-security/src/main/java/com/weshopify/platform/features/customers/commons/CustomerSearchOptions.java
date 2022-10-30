package com.weshopify.platform.features.customers.commons;

public enum CustomerSearchOptions {

	BY_EMAIL("By Email","email"),
	BY_UserName("By UserName","userName"),
	BY_Mobile("By Mobile","mobileNumber");
	
	private String key;
	public String getKey() {
		return key;
	}

	private String value;
	
	private CustomerSearchOptions(String searchKey, String searchValue) {
		this.key=searchKey;
		this.value=searchValue;
	}
	
	public static String getSearchName(String searchKey) {
		//CustomerSearchOptions customerOption = CustomerSearchOptions.valueOf(CustomerSearchOptions.class, searchKey);
		for(CustomerSearchOptions customerOption: CustomerSearchOptions.values()) {
			if(customerOption.key.equals(searchKey)) {
				return customerOption.value;
			}
		}
		return null;
		
	}

	public static void main(String[] args) {
		System.out.println(CustomerSearchOptions.BY_EMAIL.getKey());
		
	}
}
