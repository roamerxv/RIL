package com.alcor.ril.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ril_user")
public class SysUser extends BaseEntity {
    //帐号
    @Column(unique = true, nullable = false)
    @Getter @Setter private String username;

    @Column(unique = true)
    @Getter @Setter private String email;

    //密码;
    @Getter @Setter private String password;

    //名称（昵称或者真实姓名）
    @Getter @Setter private String name;

    //重置密码Token
    @Getter @Setter private String resetToken;

    //用户状态,0:创建未激活,1:正常状态,2：用户被锁定.
    @Column(nullable = false)
    @Getter @Setter private int status = 0;

    // 一个用户具有多个角色
    @ManyToMany(fetch = FetchType.EAGER)//立即从数据库中进行加载数据;
    @JoinTable(name = "ril_user_role", joinColumns = {@JoinColumn(name = "uid")}, inverseJoinColumns = {@JoinColumn(name = "roleId")})
    @Getter @Setter private Set<SysRole> roles;
    
}
