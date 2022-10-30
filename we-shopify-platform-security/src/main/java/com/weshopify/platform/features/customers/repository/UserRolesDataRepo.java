package com.weshopify.platform.features.customers.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weshopify.platform.features.customers.models.UserRole;

public interface UserRolesDataRepo extends JpaRepository<UserRole, Integer> {

}
