package com.divary.cimbtestrequirement.audit;

import com.divary.cimbtestrequirement.audit.listener.AuditEntityListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners({AuditEntityListener.class})
public class AuditEntity implements Serializable {
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy", timezone = "GMT+7")
    private Date activityDate;
}
