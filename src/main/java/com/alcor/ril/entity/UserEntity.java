package com.alcor.ril.entity;

import javax.persistence.*;

/**
 * @author roamer - 徐泽宇
 * @create 2017-08-2017/8/24  下午3:06
 */
@Entity
@Table(name = "user", schema = "ril", catalog = "")
public class UserEntity {
    private String name;
    private String passwd;

    @Id
    @Column(name = "name", nullable = false, length = 36)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "passwd", nullable = false, length = 36)
    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (passwd != null ? !passwd.equals(that.passwd) : that.passwd != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (passwd != null ? passwd.hashCode() : 0);
        return result;
    }
}
