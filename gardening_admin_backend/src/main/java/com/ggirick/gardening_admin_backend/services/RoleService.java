package com.ggirick.gardening_admin_backend.services;

import com.ggirick.gardening_admin_backend.dto.user.RoleDTO;
import com.ggirick.gardening_admin_backend.mappers.user.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleMapper roleMapper;


    public List<RoleDTO> getAllRoles() {
            return roleMapper.getAllRoles();
    }
}
