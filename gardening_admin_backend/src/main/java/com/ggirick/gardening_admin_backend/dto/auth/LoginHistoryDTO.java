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
public class LoginHistoryDTO {

    private Long loginId;
    private String userUid;
    private String ipAddress;
    private String sessionId;
    private Date loginAt;
    private Date logoutAt;
}
