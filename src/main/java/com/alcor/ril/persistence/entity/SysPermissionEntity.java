package com.alcor.ril.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ril_permission")
public class SysPermissionEntity extends BaseEntity {
    //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    @Column(unique = true, nullable = false)
    @Getter @Setter private String permission;

    //资源路径
    @Getter @Setter private String url;

    // http method
    @Column(columnDefinition="enum('ALL','GET','POST','PUT','DELETE')")
    @Getter @Setter private String method = "GET";

    //资源类型，[menu|button]
    @Column(columnDefinition="enum('menu','button')")
    @Getter @Setter private String resourceType;
    
    //权限名称.
    @Getter @Setter private String name;

    //父编号
    @Getter @Setter private String parentId;

    //父编号列表
    @Getter @Setter private String parentIds;

    // 是否可用
    @Getter @Setter private Boolean available = Boolean.FALSE;

    @Column(name = "clazz", nullable = false, length = 256)
    @Getter @Setter private String clazz ;


    @Column(name = "order_num", nullable = false)
    @Getter @Setter Integer orderNum = 0 ;


    @JsonIgnore
    @ManyToMany
    @JoinTable(name="ril_role_permission",joinColumns={@JoinColumn(name="permissionId")},inverseJoinColumns={@JoinColumn(name="roleId")})
    @Getter @Setter private List<SysRoleEntity> roles;

}
