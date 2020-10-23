package com.pf.auth.domain;

import com.pf.enums.UseStateEnum;
import com.pf.system.model.UserDto;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 登录用户信息
 * Created by  on 2020/6/19.
 */
@Data
public class SecurityUser implements UserDetails {

    /**
     * ID
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户状态
     */
    private Boolean enabled;
//    /**
//     * 登录客户端ID
//     */
//    private String clientId;
    /**
     * 权限数据
     */
    private Collection<SimpleGrantedAuthority> authorities;

    public SecurityUser() {

    }

    public SecurityUser(UserDto userDto) {
        this.setId(userDto.getUserId());
        this.setUsername(userDto.getUserName());
        this.setPassword(userDto.getUserPasswd());
        this.setEnabled(UseStateEnum.EFFECTIVE.getCodeToStr().equals(userDto.getUserUseState()));
        if (userDto.getRoles() != null) {
            authorities = new ArrayList<>();
            userDto.getRoles().forEach(item -> authorities.add(new SimpleGrantedAuthority(item.getRoleId())));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
        return this.enabled;
    }

}
