package com.alcor.ril.security;

import com.alcor.ril.persistence.entity.SysPermission;
import com.alcor.ril.persistence.entity.SysRole;
import com.alcor.ril.persistence.entity.SysUser;
import com.alcor.ril.persistence.repository.SysUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created with admin.
 * User: duduba - 邓良玉
 * Date: 2017/12/5
 * Time: 21:52
 */
@Slf4j
@Transactional
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private SysUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<SysUser> user = userRepository.findByUsername(s);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("用户不存在");
        }
        user.ifPresent((u) -> log.debug("username:{}; password:{}", u.getUsername(), u.getPassword()));


        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;

        return new org.springframework.security.core.userdetails.User(
                user.get().getUsername(),
                user.get().getPassword(),
                user.get().getStatus() == 1,
                accountNonExpired,
                credentialsNonExpired,
                user.get().getStatus() != 2,
                getAuthorities(user.get().getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<SysRole> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    private List<String> getPrivileges(Collection<SysRole> roles) {
        List<String> privileges = new ArrayList<>();
        List<SysPermission> collection = new ArrayList<>();
        for (SysRole role : roles) {
            log.debug("role={},avaiable={}", role.getRole(), role.getAvailable());
            if(role.getAvailable()) {
                privileges.add(role.getRole().toUpperCase());
                collection.addAll(role.getPermissions());
            }
        }
        for (SysPermission permission : collection) {
            log.debug("permission={},avaiable={}", permission.getPermission(), permission.getAvailable());
            if(permission.getAvailable()) {
                privileges.add(permission.getPermission().toUpperCase());
            }
        }
        return privileges;
    }
}
