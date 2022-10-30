package com.weshopify.platform.features.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weshopify.platform.features.auth.models.UserRole;

public interface UserRolesDataRepo extends JpaRepository<UserRole, Integer> {

}
