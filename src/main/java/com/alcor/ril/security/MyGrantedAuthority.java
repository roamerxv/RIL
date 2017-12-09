package com.alcor.ril.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

/**
 * Created with admin.
 * User: duduba - 邓良玉
 * Date: 2017/12/9
 * Time: 02:01
 */
public class MyGrantedAuthority implements GrantedAuthority {

    @Getter private String permission;
    @Getter private String url;
    @Getter private String method;

    public MyGrantedAuthority(String permission, String url, String method) {
        this.permission = permission;
        this.url = url;
        this.method = method;
    }
    
    @Override
    public String getAuthority() {
        return this.permission;
    }

    @Override
    public String toString() {
        return this.permission;
    }
}
