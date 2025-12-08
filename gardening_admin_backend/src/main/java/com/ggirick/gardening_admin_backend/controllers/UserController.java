package com.ggirick.gardening_admin_backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.ggirick.gardening_admin_backend.dto.auth.UserTokenDTO;
import com.ggirick.gardening_admin_backend.dto.user.UsersDomainDTO;

import com.ggirick.gardening_admin_backend.services.UsersDomainService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UsersDomainService usersDomainService;

    @GetMapping
    public List<UsersDomainDTO> getUsers(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String range,
            @RequestParam(required = false) String filter,
            HttpServletResponse response
    ) throws JsonProcessingException {

        UsersDomainService.UserQueryResult result =
                usersDomainService.getUsers(sort, range, filter);

        response.setHeader(
                "Content-Range",
                String.format("users %d-%d/%d",
                        result.offset(),
                        result.offset() + result.list().size() - 1,
                        result.total())
        );
        response.setHeader("Access-Control-Expose-Headers", "Content-Range");

        return result.list();
    }

    @GetMapping("/{id}")
    public UsersDomainDTO getUser(@PathVariable("id") String uuid) {
        return usersDomainService.getUser(uuid);
    }

    @PutMapping("/{id}")
    public UsersDomainDTO updateUser(
            @AuthenticationPrincipal UserTokenDTO userTokenDTO,
            @PathVariable("id") String uuid,
            @RequestBody UsersDomainDTO dto
    ) {
        dto.setId(uuid);
        return usersDomainService.updateUser( dto, userTokenDTO.getUid());
    }

    @DeleteMapping("/{id}")
    public UsersDomainDTO deleteUser(@PathVariable("id") String uuid) {

        return usersDomainService.deleteUser(uuid);
    }

    @GetMapping("/batch")
    public List<UsersDomainDTO> getMany(@RequestParam(required = false) String filter)
            throws JsonProcessingException {
        return usersDomainService.getMany(filter);
    }

    @PutMapping
    public List<UsersDomainDTO> updateMany(
            @AuthenticationPrincipal UserTokenDTO userTokenDTO,
            @RequestParam(required = false) String filter,
            @RequestBody UsersDomainDTO dto
    ) throws JsonProcessingException {
        return usersDomainService.updateMany(filter, dto, userTokenDTO.getUid());
    }

    @DeleteMapping
    public List<UsersDomainDTO> deleteMany(
            @RequestParam(required = false) String filter
    ) throws JsonProcessingException {
        return usersDomainService.deleteMany(filter);
    }

    @PostMapping
    public UsersDomainDTO createUser(@RequestBody UsersDomainDTO dto) {
        return usersDomainService.createUser(dto);
    }
}

