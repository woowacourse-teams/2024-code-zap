package codezap.template.repository;

import java.util.NoSuchElementException;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.template.domain.Template;

public interface TemplateRepository extends JpaRepository<Template, Long> {

    default Template getById(Long id) {
        return findById(id).orElseThrow(
                () -> new NoSuchElementException("식별자 " + id + "에 해당하는 템플릿이 존재하지 않습니다."));
    }
}
