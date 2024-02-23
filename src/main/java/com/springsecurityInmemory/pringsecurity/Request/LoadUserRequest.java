package com.springsecurityInmemory.pringsecurity.Request;


import com.springsecurityInmemory.pringsecurity.Entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoadUserRequest {

    private String userName;
    private String userPassword;
    private String userRole;

    public UserInfo to(LoadUserRequest userRequest){

        UserInfo userInfo = UserInfo.builder()
                .userName(userRequest.userName)
                .userPassword(userRequest.userPassword)
                .userRole(userRequest.userRole)
                .build();
        return userInfo;
    }


}
