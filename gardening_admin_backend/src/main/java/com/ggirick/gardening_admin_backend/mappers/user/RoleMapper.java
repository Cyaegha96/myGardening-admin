package com.ggirick.gardening_admin_backend.mappers.user;

import com.ggirick.gardening_admin_backend.dto.auth.UserRoleDTO;
import com.ggirick.gardening_admin_backend.dto.user.RoleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {

    List<RoleDTO> getAllRoles();

    List<UserRoleDTO> getRolesByUserUid(String userUid);

    void deleteUserRole(UserRoleDTO oldRole);

    void insertUserRole(UserRoleDTO newRole);

    void updateUserRole(UserRoleDTO newRole);

    List<UserRoleDTO> getRolesByUserUids(List<String> ids);
}
