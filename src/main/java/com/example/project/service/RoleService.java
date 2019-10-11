package com.example.project.service;

import com.example.project.entity.Roles;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    Roles findByRoleName(String roleName);
}
