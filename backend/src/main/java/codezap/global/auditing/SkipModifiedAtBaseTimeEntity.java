package codezap.global.auditing;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;

@MappedSuperclass
public abstract class SkipModifiedAtBaseTimeEntity extends BaseTimeEntity {

    @Transient
    private LocalDateTime lastModifiedAt;

    @Transient
    private boolean isModified = true;

    @PreUpdate
    private void preUpdate() {
        if (!isModified && lastModifiedAt != null) {
            modifiedAt = lastModifiedAt;
            return;
        }
        isModified = true;
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    private void postLoad() {
        lastModifiedAt = modifiedAt;
    }

    public void skipModifiedAtUpdate() {
        isModified = false;
    }
}
