package codezap.global.auditing;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @Transient
    private LocalDateTime lastKnownModifiedAt;

    @Transient
    private boolean isModified = true;

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

    public void markModified() {
        isModified = true;
    }
}
