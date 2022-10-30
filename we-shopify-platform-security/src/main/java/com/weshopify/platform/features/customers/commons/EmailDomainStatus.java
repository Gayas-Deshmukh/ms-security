package com.weshopify.platform.features.customers.commons;

public enum EmailDomainStatus {

	UNDELIVERABLE("UNDELIVERABLE"),
	DELIVERABLE("DELIVERABLE");
	
	private String emailDeliveryStatus;
	public String getEmailDeliveryStatus() {
		return emailDeliveryStatus;
	}
	private EmailDomainStatus(String emailDeliveryStatus) {
		this.emailDeliveryStatus = emailDeliveryStatus;
	}
	
	
	
}
