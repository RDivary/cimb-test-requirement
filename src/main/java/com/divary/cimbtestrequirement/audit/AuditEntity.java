package com.divary.cimbtestrequirement.audit;

import com.divary.cimbtestrequirement.audit.listener.AuditEntityListener;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners({AuditEntityListener.class})
public class AuditEntity implements Serializable {
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
}
