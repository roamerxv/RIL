package com.alcor.ril.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity  implements Serializable {

    @Id
    @Getter @Setter protected String id;

    @CreatedDate
    @Column(name = "gmt_create", nullable = false, insertable = true, updatable = false)
    @Temporal(TIMESTAMP)
    @Getter @Setter protected Date creationDate;

    @LastModifiedDate
    @Column(name = "gmt_modified", insertable = false, updatable = true)
    @Temporal(TIMESTAMP)
    @Getter @Setter protected Date lastModifiedDate;

    /**
     * copies the auto generated fields id, created and last modified form the given entity/DTO to this entity/DTO
     *
     * @param copyEntity the entity to copy from.
     */
    public void copy(BaseEntity copyEntity) {
        this.setId(copyEntity.getId());
        this.setCreationDate(copyEntity.getCreationDate());
        this.setLastModifiedDate(copyEntity.getLastModifiedDate());
    }
    
}
