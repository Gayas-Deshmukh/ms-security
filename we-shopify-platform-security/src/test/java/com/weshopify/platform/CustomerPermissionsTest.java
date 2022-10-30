package com.weshopify.platform;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.weshopify.platform.features.customers.models.UserPermissions;
import com.weshopify.platform.features.customers.repository.UserPermissionsDataRepo;

public class CustomerPermissionsTest extends WeShopifyPlatformApplicationTests{

	@Autowired
	private UserPermissionsDataRepo permissionsDataRepo;
	
	String action= null;
	
	@BeforeEach
	public void init() {
		this.action="view_customers";
	}
	
	@Test
	@Order(value = 1)
	public void testCreatePermission() {
		UserPermissions permission = new UserPermissions();
		permission.setAction(action);
		permissionsDataRepo.save(permission);
		assertNotNull(permission.getId());
	}
}
