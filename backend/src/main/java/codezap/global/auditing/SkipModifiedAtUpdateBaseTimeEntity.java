package codezap.global.auditing;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;

import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class SkipModifiedAtUpdateBaseTimeEntity extends BaseTimeEntity {

    @Transient
    protected LocalDateTime lastKnownModifiedAt;

    @Transient
    protected boolean isModified = true;

    @PreUpdate
    private void preUpdate() {
        if (!isModified && lastKnownModifiedAt != null) {
            modifiedAt = lastKnownModifiedAt;
            return;
        }
        isModified = true;
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    private void postLoad() {
        lastKnownModifiedAt = modifiedAt;
    }

    public void markUnModified() {
        isModified = false;
    }
}
