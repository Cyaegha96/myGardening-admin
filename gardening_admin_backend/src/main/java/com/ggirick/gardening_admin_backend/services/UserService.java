package com.ggirick.gardening_admin_backend.services;



import com.ggirick.gardening_admin_backend.dto.auth.AuthDTO;
import com.ggirick.gardening_admin_backend.dto.auth.UserInfoDTO;
import com.ggirick.gardening_admin_backend.dto.auth.UsersDTO;
import com.ggirick.gardening_admin_backend.mappers.auth.AuthMapper;
import com.ggirick.gardening_admin_backend.mappers.auth.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final AuthMapper authMapper;

    public void completeProfile(UserInfoDTO dto) {
        // UID가 이미 존재하는지 확인
        UsersDTO exists = userMapper.selectUserByUid(dto.getUuid());

        if (exists != null) {
            // 이미 존재하면 update
            userMapper.updateUserInfo(dto);
            userMapper.updateUserEmailAndPhoneToAuth(dto.getUuid(),dto.getEmail(), dto.getPhone());
        } else {
            // 존재하지 않으면 insert
            userMapper.insertUserInfo(dto);
        }
    }
    public UserInfoDTO getUserInfo(String uid) {


        UserInfoDTO userInfoDTO =  userMapper.selectUserInfoByUid(uid);

        List<String> roles = userMapper.selectRoleNameByUserUid(uid);

        userInfoDTO.setRoles(roles);
        return userInfoDTO;
    }

    public AuthDTO getUserAuthByPhone(String phone) {
        return userMapper.selectUserByPhone(phone);
    }
}
