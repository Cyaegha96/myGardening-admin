package com.ggirick.gardening_admin_backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    @GetMapping
    public List<ActiveSessionDTO> getActiveSessions(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String range,
            @RequestParam(required = false) String filter,
            HttpServletResponse response
    ) throws JsonProcessingException {
        List<ActiveSessionDTO> sessions = sessionService.getActiveSessions(100);

        response.setHeader("Content-Range", "sessions 0-" + (sessions.size()-1) + "/" + sessions.size());
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
