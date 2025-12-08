package com.ggirick.gardening_admin_backend.controllers;

import com.ggirick.gardening_admin_backend.dto.user.RoleDTO;
import com.ggirick.gardening_admin_backend.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleDTO> getRoles() {
        return roleService.getAllRoles();
    }
}
