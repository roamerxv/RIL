package com.alcor.ril.persistence.entity;

import javax.persistence.*;

/**
 * @author roamer - 徐泽宇
 * @create 2017-12-2017/12/12  下午12:13
 */
@Entity
@Table(name = "ril_role_permission", schema = "ril", catalog = "")
@IdClass(RilRolePermissionEntityPK.class)
public class RilRolePermissionEntity {
    private String permissionId;
    private String roleId;

    @Id
    @Column(name = "permission_id", nullable = false, length = 36)
    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    @Id
    @Column(name = "role_id", nullable = false, length = 36)
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RilRolePermissionEntity that = (RilRolePermissionEntity) o;

        if (permissionId != null ? !permissionId.equals(that.permissionId) : that.permissionId != null) return false;
        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = permissionId != null ? permissionId.hashCode() : 0;
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        return result;
    }
}
