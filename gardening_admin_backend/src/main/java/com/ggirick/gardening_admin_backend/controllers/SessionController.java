package com.ggirick.gardening_admin_backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggirick.gardening_admin_backend.dto.session.ActiveSessionDTO;
import com.ggirick.gardening_admin_backend.services.RedisService;
import com.ggirick.gardening_admin_backend.services.SessionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public List<ActiveSessionDTO> getActiveSessions(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String range,
            @RequestParam(required = false) String filter,
            HttpServletResponse response
    ) throws JsonProcessingException {

        List<ActiveSessionDTO> allSessions = sessionService.getActiveSessions(1000); // 전체 조회
        int totalCount = allSessions.size();


        int start = 0;
        int end = totalCount - 1;

        List<ActiveSessionDTO> sessions = allSessions;

        if (range != null) {
            int[] r = objectMapper.readValue(range, int[].class);
            start = r[0];
            end = Math.min(r[1], totalCount - 1);
            sessions = allSessions.subList(start, end + 1);
        }

        response.setHeader("Content-Range",
                "sessions " + start + "-" + end + "/" + totalCount);
        response.setHeader("Access-Control-Expose-Headers", "Content-Range");

        return sessions;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable("id") String id) {
        // id는 sessionId
        log.info("Delete session with id {}", id);
       sessionService.forcedDeleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
