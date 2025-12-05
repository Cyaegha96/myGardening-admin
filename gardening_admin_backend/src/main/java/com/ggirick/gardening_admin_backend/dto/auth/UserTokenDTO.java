package com.ggirick.gardening_admin_backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTokenDTO {
    private String uid; // users.uid (UUID)
    private String provider; // auth.provider (e.g., "local", "kakao", "google")


}
