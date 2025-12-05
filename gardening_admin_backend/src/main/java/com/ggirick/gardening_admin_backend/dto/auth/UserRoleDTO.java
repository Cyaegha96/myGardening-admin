package com.ggirick.gardening_admin_backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleDTO {
    private String userUid;
    private int roleId;
    private String assignedBy;
    private Timestamp assignedAt;
    private String roleName;
}
