package com.ggirick.gardening_admin_backend.services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggirick.gardening_admin_backend.dto.auth.UserRoleDTO;
import com.ggirick.gardening_admin_backend.dto.auth.UserTokenDTO;
import com.ggirick.gardening_admin_backend.dto.user.UsersDomainDTO;
import com.ggirick.gardening_admin_backend.mappers.user.RoleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import com.ggirick.gardening_admin_backend.mappers.user.UsersDomainMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersDomainService {

    private final UsersDomainMapper usersDomainMapper;
    private final AuthService authService;
    private final RoleMapper roleMapper;
    private final ObjectMapper mapper = new ObjectMapper();

    // React-Admin 목록 조회 결과 패키징용 record
    public record UserQueryResult(
            List<UsersDomainDTO> list,
            int offset,
            int limit,
            int total
    ) {}

    /** 목록 조회 */
    public UserQueryResult getUsers(String sort, String range, String filter)
            throws JsonProcessingException {

        int offset = 0;
        int limit = 10;

        if (range != null) {
            int[] r = mapper.readValue(range, int[].class);
            offset = r[0];
            limit = r[1] - r[0] + 1;
        }

        // sort 파싱
        String sortField = "createdAt";
        String sortOrder = "ASC";

        if (sort != null) {
            try {
                Map<String, String> s = mapper.readValue(
                        sort,
                        new TypeReference<Map<String, String>>() {}
                );
                sortField = s.getOrDefault("field", sortField);
                sortOrder = s.getOrDefault("order", sortOrder);
            } catch (Exception e) {
                String[] s = mapper.readValue(sort, String[].class);
                sortField = s[0];
                sortOrder = s[1];
            }
        }

        // filter 파싱
        Map<String, Object> filters = new HashMap<>();
        if (filter != null) {
            filters = mapper.readValue(filter, new TypeReference<>() {});
        }

        List<UsersDomainDTO> list =
                usersDomainMapper.findUsers(offset, limit, sortField, sortOrder, filters);

        int total = usersDomainMapper.countUsers(filters);

        List<String> ids = list.stream().map(UsersDomainDTO::getId).toList();
        List<UserRoleDTO> roles = roleMapper.getRolesByUserUids(ids);

        // Map으로 묶어서 매핑
        Map<String, List<UserRoleDTO>> roleMap = roles.stream()
                .collect(Collectors.groupingBy(UserRoleDTO::getUserUid));

        list.forEach(u -> u.setRoles(roleMap.getOrDefault(u.getId(), List.of())));

        return new UserQueryResult(list, offset, limit, total);

    }

    /** 단일 조회 */
    public UsersDomainDTO getUser(String uuid) {
        UsersDomainDTO dto =  usersDomainMapper.selectOne(uuid);
        dto.setRoles(usersDomainMapper.getRoles(uuid));
       return dto;
    }

    /** 단일 업데이트 */
    @Transactional
    public UsersDomainDTO updateUser(UsersDomainDTO dto, String adminId) {


        usersDomainMapper.updateUsers(dto);
        usersDomainMapper.updateAuth(dto);
        usersDomainMapper.updateUserInfo(dto);
        //역할 수정
        updateUserRoles(dto.getId(),  dto.getRoles(), adminId);
        String status = dto.getStatus();

        if ("INACTIVE".equalsIgnoreCase(status) || "BLOCKED".equalsIgnoreCase(status)) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            System.out.println("블락된 아이디:"+dto.getId());
                            authService.forceLogout(dto.getId());
                        }
                    }
            );
        }

        return dto;
    }
    @Transactional
    public void updateUserRoles(String userUid, List<UserRoleDTO> newRoles, String adminId) {
        // 1. 기존 roles 조회
        List<UserRoleDTO> oldRoles = roleMapper.getRolesByUserUid(userUid);

        // 2. 삭제할 roles (old에는 있지만 new에는 없는 것)
        for (UserRoleDTO oldRole : oldRoles) {
            boolean exists = newRoles.stream()
                    .anyMatch(r -> r.getRoleId() == oldRole.getRoleId());
            if (!exists) {
                roleMapper.deleteUserRole(oldRole);
            }
        }

        // 3. 추가/수정할 roles
        for (UserRoleDTO newRole : newRoles) {
            boolean exists = oldRoles.stream()
                    .anyMatch(r -> r.getRoleId() == newRole.getRoleId());
            if (!exists) {
                // 새로 추가
                newRole.setUserUid(userUid);
                newRole.setAssignedBy(adminId);

                roleMapper.insertUserRole(newRole);
            } else {
                // 기존 값과 비교해서 변경된 경우만 update
                UserRoleDTO oldRole = oldRoles.stream()
                        .filter(r -> r.getRoleId() == newRole.getRoleId())
                        .findFirst()
                        .orElse(null);

                if (oldRole != null &&
                        (!Objects.equals(oldRole.getAssignedBy(), newRole.getAssignedBy()) ||
                                !Objects.equals(oldRole.getAssignedAt(), newRole.getAssignedAt()))) {
                    roleMapper.updateUserRole(newRole);
                }
            }
        }
    }


    /** 단일 삭제 */
    @Transactional
    public UsersDomainDTO deleteUser(String uuid) {
        UsersDomainDTO old = usersDomainMapper.selectOne(uuid);
        usersDomainMapper.deleteUserInfo(uuid);
        usersDomainMapper.deleteUserAuth(uuid);
        usersDomainMapper.deleteUser(uuid);

        return old;
    }

    /** 여러 개 조회 */
    public List<UsersDomainDTO> getMany(String filter)
            throws JsonProcessingException {

        Map<String, Object> filters = parseFilter(filter);
        List<String> ids = (List<String>) filters.get("id");

        return usersDomainMapper.findUsersByIds(ids);
    }

    /** 여러 개 업데이트 */
    @Transactional
    public List<UsersDomainDTO> updateMany(String filter, UsersDomainDTO dto, String adminId)
            throws JsonProcessingException {

        Map<String, Object> filters = parseFilter(filter);
        List<String> ids = (List<String>) filters.get("id");

        for (String id : ids) {
            dto.setId(id);
            updateUser(dto, adminId);
        }

        return usersDomainMapper.findUsersByIds(ids);
    }

    /** 여러 개 삭제 */
    @Transactional
    public List<UsersDomainDTO> deleteMany(String filter)
            throws JsonProcessingException {

        Map<String, Object> filters = parseFilter(filter);
        List<String> ids = (List<String>) filters.get("id");

        List<UsersDomainDTO> old = usersDomainMapper.findUsersByIds(ids);

        for (String uuid : ids) {
            deleteUser(uuid);
        }

        return old;
    }

    /** 생성 */
    public UsersDomainDTO createUser(UsersDomainDTO dto) {
        usersDomainMapper.insertUser(dto);
        return dto;
    }

    /* 공통 필터 파싱 메서드 */
    private Map<String, Object> parseFilter(String filter)
            throws JsonProcessingException {

        if (filter == null) return new HashMap<>();

        return mapper.readValue(filter, new TypeReference<>() {});
    }
}
