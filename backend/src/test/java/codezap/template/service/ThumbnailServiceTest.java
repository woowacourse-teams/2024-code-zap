package codezap.template.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;
import codezap.template.fixture.SourceCodeFixture;
import codezap.template.fixture.TemplateFixture;
import codezap.template.fixture.ThumbnailFixture;
import codezap.template.repository.FakeThumbnailRepository;
import codezap.template.repository.ThumbnailRepository;

class ThumbnailServiceTest {
    private final ThumbnailRepository thumbnailRepository = new FakeThumbnailRepository(
            List.of(ThumbnailFixture.getSecond())
    );
    private final ThumbnailService thumbnailService = new ThumbnailService(thumbnailRepository);

    @Test
    @DisplayName("썸네일 생성 성공")
    void createThumbnailSuccess() {
        Template template = TemplateFixture.getFirst();
        SourceCode sourceCode = SourceCodeFixture.getFirst();

        thumbnailService.createThumbnail(template, sourceCode);
        Thumbnail thumbnail = thumbnailRepository.fetchByTemplate(template);

        assertThat(thumbnail.getSourceCode()).isEqualTo(sourceCode);
    }

    @Test
    @DisplayName("템플릿에 썸네일 조회")
    void getByTemplate() {
        Thumbnail expected = ThumbnailFixture.getSecond();

        Thumbnail thumbnail = thumbnailService.getByTemplate(expected.getTemplate());

        assertAll(
                () -> assertThat(thumbnail.getTemplate()).isEqualTo(expected.getTemplate()),
                () -> assertThat(thumbnail.getSourceCode()).isEqualTo(expected.getSourceCode())
        );
    }

    @Test
    @DisplayName("썸네일 삭제")
    void deleteByIds() {
        Thumbnail expected = ThumbnailFixture.getSecond();
        List<Long> ids = List.of(expected.getTemplate().getId());

        thumbnailService.deleteByTemplateIds(ids);
        List<Thumbnail> thumbnails = thumbnailRepository.findAll();

        assertThat(thumbnails).isEmpty();
    }
}
