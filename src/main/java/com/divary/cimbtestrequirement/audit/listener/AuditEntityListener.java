package com.divary.cimbtestrequirement.audit.listener;

import com.divary.cimbtestrequirement.audit.AuditEntity;

import javax.persistence.PrePersist;
import java.util.Date;

public class AuditEntityListener {

    public AuditEntityListener() {
    }

    @PrePersist
    public void prePersist(AuditEntity a) {
        a.setActivityDate(new Date());
    }

}
