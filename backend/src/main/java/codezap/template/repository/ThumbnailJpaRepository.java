package codezap.template.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;

@SuppressWarnings("unused")
public interface ThumbnailJpaRepository extends JpaRepository<Thumbnail, Long> {

    void deleteByTemplateId(Long id);
}
