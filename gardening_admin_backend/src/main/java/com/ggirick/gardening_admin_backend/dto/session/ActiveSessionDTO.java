package com.ggirick.gardening_admin_backend.dto.session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActiveSessionDTO {
    private String id; //sessionId
    private String uid;
    private String nickname;
    private String provider;
    private String ip;
}