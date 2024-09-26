package codezap.template.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.DatabaseIsolation;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSourceCodeRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.repository.SourceCodeRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.TemplateSpecification;
import codezap.template.repository.ThumbnailRepository;

@SpringBootTest
@DatabaseIsolation
@Transactional
class TemplateApplicationServiceTest {

    @Autowired
    TemplateApplicationService sut;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TemplateRepository templateRepository;
    @Autowired
    SourceCodeRepository sourceCodeRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ThumbnailRepository thumbnailRepository;

    @Nested
    @DisplayName("템플릿 생성")
    class CreateTemplate {

        @Test
        @DisplayName("성공: 작성자의 권한 확인 후 템플릿 생성")
        void createTemplate() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var request = createTemplateRequest(category);

            // when
            var actual = sut.createTemplate(member, request);

            // then
            assertThat(categoryRepository.fetchById(actual)).isNotNull();
        }

        @Test
        @DisplayName("실패: 카테고리에 대한 권한이 없는 경우")
        void createTemplate_Fail_NoAuthorization() {
            // given
            var ownerMember = memberRepository.save(MemberFixture.getFirstMember());
            var otherMember = memberRepository.save(MemberFixture.getSecondMember());
            var category = categoryRepository.save(new Category("Members", ownerMember));
            var request = createTemplateRequest(category);

            // when & then
            assertThatThrownBy(() -> sut.createTemplate(otherMember, request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
        }

        private static CreateTemplateRequest createTemplateRequest(Category category) {
            var title = "title1";
            var description = "description1";
            var sourceCodeRequest = new CreateSourceCodeRequest("filename1", "content1", 1);
            var sourceCodes = List.of(sourceCodeRequest);
            int thumbnailOrdinal = 1;
            List<String> tags = List.of();
            return new CreateTemplateRequest(
                    title,
                    description,
                    sourceCodes,
                    thumbnailOrdinal,
                    category.getId(),
                    tags);
        }
    }

    @Nested
    @DisplayName("ID로 템플릿 조회")
    class FindByTemplateId {

        @Test
        @DisplayName("ID로 템플릿 조회 성공")
        void findByTemplateId() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template = templateRepository.save(TemplateFixture.get(member, category));

            // when
            var actual = sut.findTemplateById(template.getId());

            // then
            assertThat(actual.id()).isEqualTo(1L);
        }
    }


    @Nested
    @DisplayName("템플릿 전체 조회")
    class FindAllBy {

        @ParameterizedTest
        @MethodSource
        @DisplayName("사용자ID, 검색어, 카테고리ID, 태그ID로 템플릿 조회 성공")
        void findAllBy(
                Long memberId,
                String keyword,
                Long categoryId,
                List<Long> tagIds,
                Pageable pageable
        ) {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template1 = templateRepository.save(new Template(member, "title1", "description", category));
            var sourceCode1 = sourceCodeRepository.save(new SourceCode(template1, "filename1", "content", 1));
            thumbnailRepository.save(new Thumbnail(template1, sourceCode1));

            // when & then
            assertThatCode(() -> sut.findAllTemplatesBy(memberId, keyword, categoryId, tagIds, pageable))
                    .doesNotThrowAnyException();
        }

        static Stream<Arguments> findAllBy() {
            return Stream.of(
                    Arguments.of(null, null, null, null, Pageable.ofSize(1)),
                    Arguments.of(1L, null, null, null, Pageable.ofSize(1)),
                    Arguments.of(null, "keyword", null, null, Pageable.ofSize(1)),
                    Arguments.of(null, null, 1L, null, Pageable.ofSize(1)),
                    Arguments.of(null, null, null, List.of(1L, 2L), Pageable.ofSize(1)),
                    Arguments.of(null, null, null, null, Pageable.ofSize(2))
            );
        }
    }

    @Nested
    @DisplayName("템플릿 수정")
    class Update {

        @Test
        @DisplayName("템플릿 업데이트 성공")
        void update() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template = templateRepository.save(new Template(member, "title1", "description", category));
            var sourceCode1 = sourceCodeRepository.save(new SourceCode(template, "filename1", "content1", 1));
            var sourceCode2 = sourceCodeRepository.save(new SourceCode(template, "filename2", "content2", 2));
            thumbnailRepository.save(new Thumbnail(template, sourceCode1));

            var createRequest = List.of(new CreateSourceCodeRequest("filename3", "content3", 3));
            var updateRequest1 = updateSourceCodeRequest(sourceCode1);
            var updateRequest2 = updateSourceCodeRequest(sourceCode2);
            var updateRequest = List.of(updateRequest1, updateRequest2);
            List<Long> deleteIds = List.of();
            var request = new UpdateTemplateRequest(
                    "Updated Template",
                    "Updated Description",
                    createRequest,
                    updateRequest,
                    deleteIds,
                    category.getId(),
                    List.of());

            // when
            sut.update(member, template.getId(), request);

            // when & then
            var updatedTemplate = templateRepository.fetchById(template.getId());
            assertAll(
                    () -> assertEquals("Updated Template", updatedTemplate.getTitle()),
                    () -> assertEquals("Updated Description", updatedTemplate.getDescription())
            );
        }

        @Test
        @DisplayName("실패: 카테고리에 대한 권한이 없는 경우")
        void updateTemplate_WhenNoAuthorization() {
            // given
            Member otherMember = memberRepository.save(MemberFixture.getFirstMember());
            Category othersCategory = categoryRepository.save(new Category("Members", otherMember));

            Member member = memberRepository.save(MemberFixture.getSecondMember());
            Category category = categoryRepository.save(new Category("Members", member));
            Template template = templateRepository.save(TemplateFixture.get(member, category));

            SourceCode sourceCode = sourceCodeRepository.save(new SourceCode(template, "filename", "content", 1));
            thumbnailRepository.save(new Thumbnail(template, sourceCode));
            UpdateSourceCodeRequest updateSourceCodeRequest = updateSourceCodeRequest(sourceCode);

            UpdateTemplateRequest request = new UpdateTemplateRequest(
                    "Updated Template",
                    "Updated Description",
                    Collections.emptyList(),
                    List.of(updateSourceCodeRequest),
                    Collections.emptyList(),
                    othersCategory.getId(),
                    Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> sut.update(otherMember, template.getId(), request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 템플릿에 대한 권한이 없습니다.");
        }

        private UpdateSourceCodeRequest updateSourceCodeRequest(SourceCode sourceCode) {
            return new UpdateSourceCodeRequest(
                    sourceCode.getId(),
                    sourceCode.getFilename(),
                    sourceCode.getContent(),
                    sourceCode.getOrdinal());
        }
    }

    @Nested
    @DisplayName("템플릿 삭제")
    class DeleteByMemberAndIds {

        @Test
        @DisplayName("사용자 정보와 템플릿 ID로 템플릿 삭제 성공")
        void deleteByMemberAndIds() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template1 = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode1 = sourceCodeRepository.save(new SourceCode(template1, "filename1", "content1", 1));
            var template2 = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode2 = sourceCodeRepository.save(new SourceCode(template2, "filename2", "content2", 2));
            var template3 = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode3 = sourceCodeRepository.save(new SourceCode(template3, "filename3", "content3", 3));

            var deleteIds = List.of(1L, 2L);

            // when
            sut.deleteByMemberAndIds(member, deleteIds);

            // then
            Specification<Template> spec = new TemplateSpecification(member.getId(), null, null, null);
            var actualTemplatesLeft = templateRepository.findAll(spec, PageRequest.of(0, 10));
            var actualSourceCodeLeft = sourceCodeRepository.findAllByTemplate(template1);
            actualSourceCodeLeft.addAll(sourceCodeRepository.findAllByTemplate(template2));
            actualSourceCodeLeft.addAll(sourceCodeRepository.findAllByTemplate(template3));

            assertAll(
                    () -> assertThat(actualTemplatesLeft).containsExactly(template3),
                    () -> assertThat(actualTemplatesLeft).doesNotContain(template1, template2),
                    () -> assertThat(actualSourceCodeLeft).containsExactly(sourceCode3),
                    () -> assertThat(actualSourceCodeLeft).doesNotContain(sourceCode1, sourceCode2)
            );
        }
    }
}
