package com.ggirick.gardening_admin_backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogoutRequestDTO {

    //로그인한 사용자의 refreshToken
    private String refreshToken;
    //로그인한 사용자의 accessToken
    private String accessToken;
}
