package com.ggirick.gardening_admin_backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSessionDTO {
    private String sessionId;
    private String userUid;
    private String provider;
    private String refreshToken;
    private String accessToken;
    private Date lastAccessTokenIssuedAt;
    private Date lastAccessTokenExpiresAt;
    private Date expiresAt;
    private String ipAddress;
    private String isRevoked;
    private  Date createdAt;
}
