package com.ggirick.gardening_admin_backend.dto.user;

import com.ggirick.gardening_admin_backend.dto.auth.UserRoleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersDomainDTO {
    private String id;       // uuid 역할
    private String status;     // UsersDTO
    private Date createdAt;
    private Date updatedAt;

    // Auth
    private String provider;
    private String userId;  //id
    private String providerUserId;
    private String email;
    private String phone;


    // UserInfo
    private String name;
    private String nickname;
    private String address;
    private String addressDetail;
    private String zipcode;
    private String bio;
    private String profileUrl;
    private LocalDate birthDate;

    // Role 리스트
    private List<UserRoleDTO> roles;
}
