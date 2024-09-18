package codezap.template.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.DatabaseIsolation;
import codezap.member.repository.MemberRepository;
import codezap.tag.domain.Tag;
import codezap.tag.dto.response.FindAllTagsResponse;
import codezap.tag.dto.response.FindTagResponse;
import codezap.tag.repository.TagRepository;
import codezap.tag.repository.TemplateTagRepository;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSourceCodeRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.repository.SourceCodeRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.ThumbnailRepository;

@SpringBootTest
@DatabaseIsolation
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
    TagRepository tagRepository;
    @Autowired
    TemplateTagRepository templateTagRepository;
    @Autowired
    ThumbnailRepository thumbnailRepository;

    @Nested
    class CreateTemplate {

        @Test
        @DisplayName("템플릿 생성 성공")
        void createTemplate() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));

            var request = createTemplateRequest(category);

            // when
            var actual = sut.createTemplate(member, category, request);

            // then
            assertThat(actual).isEqualTo(1L);
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
    class GetById {

        @Test
        @DisplayName("ID로 템플릿 조회 성공")
        void getById() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));

            var template = templateRepository.save(TemplateFixture.get(member, category));

            // when
            var actual = sut.getById(template.getId());

            // then
            assertThat(actual.id()).isEqualTo(1L);
        }
    }

    @Nested
    class GetAllTagsByMemberId {

        @Test
        @DisplayName("사용자 ID로 모든 태그 조회 성공")
        void getAllTagsByMemberId() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());

            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template1 = templateRepository.save(new Template(member, "title1", "description", category));
            var template2 = templateRepository.save(new Template(member, "title2", "description", category));
            var template3 = templateRepository.save(new Template(member, "title3", "description", category));
            var tag1 = tagRepository.save(new Tag("tag1"));
            var tag2 = tagRepository.save(new Tag("tag2"));
            var tag3 = tagRepository.save(new Tag("tag3"));
            templateTagRepository.save(new TemplateTag(template1, tag1));
            templateTagRepository.save(new TemplateTag(template1, tag2));
            templateTagRepository.save(new TemplateTag(template2, tag2));
            templateTagRepository.save(new TemplateTag(template2, tag3));
            templateTagRepository.save(new TemplateTag(template3, tag3));
            templateTagRepository.save(new TemplateTag(template3, tag1));

            // when
            var actual = sut.getAllTagsByMemberId(member.getId());

            // then
            var expected = new FindAllTagsResponse(List.of(
                    FindTagResponse.from(tag1),
                    FindTagResponse.from(tag2),
                    FindTagResponse.from(tag3)));

            assertThat(actual).isEqualTo(expected);
        }
    }


    @Nested
    class FindAllBy {

        @ParameterizedTest
        @MethodSource
        @DisplayName("사용자ID, 검색어, 카테고리ID, 태그ID로 템플릿 검색 성공")
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
            assertThatCode(() -> sut.findAllBy(memberId, keyword, categoryId, tagIds, pageable))
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
            var updateRequest1 = new UpdateSourceCodeRequest(
                    sourceCode1.getId(),
                    sourceCode1.getFilename(),
                    sourceCode1.getContent(),
                    sourceCode1.getOrdinal());
            var updateRequest2 = new UpdateSourceCodeRequest(
                    sourceCode2.getId(),
                    sourceCode2.getFilename(),
                    sourceCode2.getContent(),
                    sourceCode2.getOrdinal());
            var updateRequest = List.of(updateRequest1, updateRequest2);
            List<Long> deleteIds = List.of();
            var request = new UpdateTemplateRequest(
                    "title1",
                    "description1",
                    createRequest,
                    updateRequest,
                    deleteIds,
                    category.getId(),
                    List.of());

            // when & then
            assertThatCode(() -> sut.update(member, template.getId(), request, category))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class DeleteByMemberAndIds {

        @Test
        @DisplayName("사용자 정보와 템플릿 ID로 템플릿 삭제 성공")
        void deleteByMemberAndIds() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template1 = templateRepository.save(new Template(member, "title1", "description", category));
            var sourceCode1 = sourceCodeRepository.save(new SourceCode(template1, "filename1", "content1", 1));
            var template2 = templateRepository.save(new Template(member, "title2", "description", category));
            var sourceCode2 = sourceCodeRepository.save(new SourceCode(template2, "filename2", "content2", 2));
            var template3 = templateRepository.save(new Template(member, "title3", "description", category));
            var sourceCode3 = sourceCodeRepository.save(new SourceCode(template3, "filename3", "content3", 3));

            var deleteIds = List.of(1L, 2L);

            // when
            sut.deleteByMemberAndIds(member, deleteIds);

            // then
            var actualTemplatesLeft = templateRepository.findAll();
            var actualSourceCodeLeft = sourceCodeRepository.findAll();

            assertAll(
                    () -> assertThat(actualTemplatesLeft).containsExactly(template3),
                    () -> assertThat(actualSourceCodeLeft).containsExactly(sourceCode3)
            );
        }
    }
}
