package com.ggirick.gardening_admin_backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO {
    private int id;
    private String roleName;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
