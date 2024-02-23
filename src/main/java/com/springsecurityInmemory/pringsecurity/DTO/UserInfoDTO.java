package com.springsecurityInmemory.pringsecurity.DTO;

import com.springsecurityInmemory.pringsecurity.Entity.UserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoDTO implements UserDetails {

    private String userName;
    private String userPassword;
    private List<GrantedAuthority> grantedAuthorityList;

    public UserInfoDTO(UserInfo userInfo) {

        if(userInfo != null) {
            this.userName = userInfo.getUserName();
            this.userPassword = userInfo.getUserPassword();
            this.grantedAuthorityList = new ArrayList<>();

            String userRole = userInfo.getUserRole();

            if (userRole != null) {
                for (String role : userRole.split(",")) {
                    grantedAuthorityList.add(new SimpleGrantedAuthority(role));
                }
            }
        }
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorityList;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
