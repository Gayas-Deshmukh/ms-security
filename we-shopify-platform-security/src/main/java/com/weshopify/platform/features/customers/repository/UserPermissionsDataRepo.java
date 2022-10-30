package com.weshopify.platform.features.customers.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weshopify.platform.features.customers.models.UserPermissions;

public interface UserPermissionsDataRepo extends JpaRepository<UserPermissions, Integer> {

}
