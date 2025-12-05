package com.ggirick.gardening_admin_backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {
    // 로컬 로그인 ID
    private String id;

    // 암호화되지 않은 원본 비밀번호 (raw password)
    private String password;
}
