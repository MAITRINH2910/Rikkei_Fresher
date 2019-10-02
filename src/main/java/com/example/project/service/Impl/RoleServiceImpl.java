package com.example.project.service.Impl;

import com.example.project.entity.Roles;
import com.example.project.repository.RoleRepository;
import com.example.project.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Roles findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
