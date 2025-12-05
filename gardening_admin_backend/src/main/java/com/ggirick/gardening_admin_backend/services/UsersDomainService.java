package com.ggirick.gardening_admin_backend.services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggirick.gardening_admin_backend.dto.user.UsersDomainDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.ggirick.gardening_admin_backend.mappers.user.UsersDomainMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersDomainService {

    private final UsersDomainMapper usersDomainMapper;
    private final AuthService authService;
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

        return new UserQueryResult(list, offset, limit, total);
    }

    /** 단일 조회 */
    public UsersDomainDTO getUser(String uuid) {
        UsersDomainDTO dto =  usersDomainMapper.selectOne(uuid);
       return dto;
    }

    /** 단일 업데이트 */
    @Transactional
    public UsersDomainDTO updateUser(UsersDomainDTO dto) {


        usersDomainMapper.updateUsers(dto);
        usersDomainMapper.updateAuth(dto);
        usersDomainMapper.updateUserInfo(dto);
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
    public List<UsersDomainDTO> updateMany(String filter, UsersDomainDTO dto)
            throws JsonProcessingException {

        Map<String, Object> filters = parseFilter(filter);
        List<String> ids = (List<String>) filters.get("id");

        for (String id : ids) {
            dto.setId(id);
            updateUser(dto);
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
