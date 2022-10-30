package com.weshopify.platform;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import com.weshopify.platform.features.customers.models.RoleToPermissions;
import com.weshopify.platform.features.customers.models.UserPermissions;
import com.weshopify.platform.features.customers.models.UserRole;
import com.weshopify.platform.features.customers.repository.UserPermissionsDataRepo;
import com.weshopify.platform.features.customers.repository.UserRolesDataRepo;

//@Import(value = CustomerPermissionsTest.class)
public class CustomerRolesTest extends CustomerPermissionsTest {

	@Autowired
	private UserPermissionsDataRepo permissionsDataRepo;
	
	@Autowired
	private UserRolesDataRepo roleDataRepo;
	
	@Test
	@Order(value = 2)
	public void testCreateRole() {
		UserRole role = new UserRole();
		role.setRole("customer");
		role.setDescription("customer");
		
		List<UserPermissions> permissionsList = permissionsDataRepo.findAll();
		assertTrue(permissionsList !=null);
		assertTrue(!permissionsList.isEmpty());
		assertTrue(permissionsList.size() > 0);
		List<RoleToPermissions> roleActionsList = new ArrayList<RoleToPermissions>();
		permissionsList.stream().forEach(permission->{
			RoleToPermissions roleToAction = new RoleToPermissions();
			roleToAction.setPermissions(permission);
			roleToAction.setRole(role);
			
			roleActionsList.add(roleToAction);
		});
		role.setActions(roleActionsList);
		roleDataRepo.save(role);
		assertNotNull(role.getId());
		assertTrue(role.getId() > 0);
		
		List<RoleToPermissions> actionsList = role.getActions();
		assertTrue(actionsList.size() >0);
		actionsList.stream().forEach(roleToPermission->{
			assertNotNull(roleToPermission.getId());
			assertTrue(roleToPermission.getId() >0);
		});
	}  
	
	
	
}
