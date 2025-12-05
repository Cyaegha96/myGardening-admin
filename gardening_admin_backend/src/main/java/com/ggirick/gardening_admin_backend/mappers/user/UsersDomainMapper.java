package com.ggirick.gardening_admin_backend.mappers.user;

import com.ggirick.gardening_admin_backend.dto.user.UsersDomainDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UsersDomainMapper {

    List<UsersDomainDTO> findUsers(
            @Param("offset") int offset,
            @Param("perPage") int perPage,
            @Param("sortField") String sortField,
            @Param("sortOrder") String sortOrder,
            @Param("filters") Map<String, Object> filters
    );

    int countUsers(@Param("filters") Map<String, Object> filters);
    UsersDomainDTO selectOne(@Param("uuid") String uid);

    void updateUsers(UsersDomainDTO user);
    void updateAuth(UsersDomainDTO user);
    void updateUserInfo(UsersDomainDTO user);
    void deleteUser(@Param("uuid") String uid);
    void deleteUserInfo(@Param("uuid") String uid);
    void deleteUserAuth(@Param("uuid") String uid);
    //여기서 ids 는 uuid를 담고 있습니다.
    List<UsersDomainDTO> findUsersByIds(@Param("ids") List<String> ids);
    void insertUser(UsersDomainDTO dto);
}
