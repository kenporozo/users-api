package com.nobelti.users.service;

import com.nobelti.users.model.Role;

public interface RoleService {
    Role insertRole(Role role);
    Role findByRole(String role);
}
