package com.hashedin.user_service.service;

import com.hashedin.user_service.model.Role;
import com.hashedin.user_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findByName(String name){
        return roleRepository.findRolesByName(name);
    }
}
