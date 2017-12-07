package com.alcor.ril.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ril_role")
public class SysRole extends BaseEntity {
    // 角色名,如"admin":
    @Column(unique = true, nullable = false)
    @Getter @Setter private String role;

    // 角色描述,UI界面显示使用
    @Getter @Setter private String description;

    // 是否可用,如果不可用将不会添加给用户
    @Getter @Setter private Boolean available = Boolean.FALSE;

    //角色 -- 权限关系：多对多关系;
    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name="ril_role_permission",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="permissionId")})
    @Getter @Setter private List<SysPermission> permissions;

    // 用户 - 角色关系定义;  一个角色对应多个用户
    @ManyToMany
    @JoinTable(name="ril_user_role",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="uid")})
    @Getter @Setter private List<SysUser> users;

}
