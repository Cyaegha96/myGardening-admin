package com.ggirick.gardening_admin_backend.services;

import com.ggirick.gardening_admin_backend.dto.auth.UserInfoDTO;
import com.ggirick.gardening_admin_backend.dto.session.ActiveSessionDTO;
import com.ggirick.gardening_admin_backend.mappers.auth.UserMapper;
import com.ggirick.gardening_admin_backend.mappers.auth.UserSessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final RedisService redisService;
    private final UserMapper userMapper;
    private final UserSessionMapper userSessionMapper;

    public List<ActiveSessionDTO> getActiveSessions(int limit) {
        List<Map<String, Object>> sessions = redisService.getRawActiveSessions(limit);
        List<ActiveSessionDTO> result = new ArrayList<>();

        for (Map<String, Object> session : sessions) {
            String sessionId = (String) session.get("sessionId");
            String uid = (String) session.get("uid");
            String provider = (String) session.get("provider");
            String ip = (String) session.get("ip");

            // uid -> nickname 조회
            UserInfoDTO userInfoDTO = userMapper.selectUserInfoByUid(uid);
            if(userInfoDTO != null){
                String nickname = userInfoDTO.getNickname();
                result.add(ActiveSessionDTO.builder()
                        .id(sessionId)
                        .uid(uid)
                        .nickname(nickname)
                        .provider(provider)
                        .ip(ip)
                        .build()
                );
            }




        }

        return result;
    }
    @Transactional
    public void forcedDeleteSession(String sessionId) {
        userSessionMapper.updateRevokedStatus(sessionId);
        redisService.forceLogoutBySessionId(sessionId);
    }


}
