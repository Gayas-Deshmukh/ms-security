package com.weshopify.platform.features.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weshopify.platform.features.auth.models.UserPermissions;

public interface UserPermissionsDataRepo extends JpaRepository<UserPermissions, Integer> {

}
