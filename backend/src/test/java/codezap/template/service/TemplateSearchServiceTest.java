package codezap.template.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.DatabaseIsolation;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.tag.domain.Tag;
import codezap.tag.repository.TagRepository;
import codezap.tag.repository.TemplateTagRepository;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.domain.Thumbnail;
import codezap.template.repository.SourceCodeRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.ThumbnailRepository;

@SpringBootTest
@DatabaseIsolation
class TemplateSearchServiceTest {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TemplateTagRepository templateTagRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SourceCodeRepository sourceCodeRepository;

    @Autowired
    private ThumbnailRepository thumbnailRepository;

    @Autowired
    private TemplateService sut;

    private Member member1, member2;
    private Category category1, category2;
    private Tag tag1, tag2;
    private Template template1, template2, template3;

    @Nested
    class FindAll {

        @BeforeEach
        void setUp() {
            saveInitialData();
            member1 = memberRepository.fetchById(1L);
            member2 = memberRepository.fetchById(2L);

            category1 = categoryRepository.fetchById(1L);
            category2 = categoryRepository.fetchById(2L);

            tag1 = tagRepository.fetchById(1L);
            tag2 = tagRepository.fetchById(2L);

            template1 = templateRepository.fetchById(1L);
            template2 = templateRepository.fetchById(2L);
            template3 = templateRepository.fetchById(3L);
        }

        @Test
        @DisplayName("검색 기능: 회원 ID로 템플릿 목록 조회 성공")
        void findAllSuccessByMemberId() {
            Long memberId = member1.getId();
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = null;
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent()).hasSize(2),
                    () -> assertThat(actual.getContent())
                            .allMatch(template -> template.getMember().getId().equals(member1.getId()))
            );
        }

        @Test
        @Disabled("Pageable에 대한 null 검증이 필요함")
        @DisplayName("검색 기능 실패: Pageable을 전달하지 않은 경우")
        void findAllFailureWithNullPageable() {
            Long memberId = member1.getId();
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = null;
            Pageable pageable = null;

            assertThatThrownBy(() -> sut.findAllBy(memberId, keyword, categoryId, tagIds, pageable))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("Pageable을 필수로 작성해야 합니다.");
        }

        @Test
        @DisplayName("검색 기능: 키워드로 템플릿 목록 조회 성공")
        void findAllSuccessByKeyword() {
            Long memberId = null;
            String keyword = "Template";
            Long categoryId = null;
            List<Long> tagIds = null;
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent())
                            .allMatch(template ->
                                    template.getTitle().contains(keyword) || template.getDescription()
                                            .contains(keyword)),
                    () -> assertThat(actual.getContent()).hasSize(3)
            );
        }

        @Test
        @DisplayName("검색 기능: 카테고리 ID로 템플릿 목록 조회 성공")
        void findAllSuccessByCategoryId() {
            Long memberId = null;
            String keyword = null;
            Long categoryId = category1.getId();
            List<Long> tagIds = null;
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent()).hasSize(2),
                    () -> assertThat(actual.getContent())
                            .allMatch(template -> template.getCategory().getId().equals(category1.getId()))
            );
        }

        @Test
        @DisplayName("검색 기능: 태그 ID 목록으로 템플릿 목록 조회, 모든 태그를 가진 템플릿만 조회 성공")
        void findAllSuccessByTagIds() {
            Long memberId = null;
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = List.of(tag1.getId(), tag2.getId());
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent())
                            .containsExactlyInAnyOrder(
                                    templateRepository.fetchById(1L),
                                    templateRepository.fetchById(2L)),
                    () -> assertThat(actual.getContent()).hasSize(2)
            );
        }

        @Test
        @DisplayName("검색 기능: 단일 태그 ID로 템플릿 목록 조회 성공")
        void findAllSuccessBySingleTagId() {
            Long memberId = null;
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = List.of(tag2.getId());
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent()).containsExactlyInAnyOrder(
                            templateRepository.fetchById(1L),
                            templateRepository.fetchById(2L),
                            templateRepository.fetchById(3L)),
                    () -> assertThat(actual.getContent()).hasSize(3)
            );
        }

        @Test
        @DisplayName("검색 기능: 회원 ID와 키워드로 템플릿 목록 조회 성공")
        void findAllSuccessByMemberIdAndKeyword() {
            Long memberId = member1.getId();
            String keyword = "Template";
            Long categoryId = null;
            List<Long> tagIds = null;
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent()).hasSize(2),
                    () -> assertThat(actual.getContent())
                            .allMatch(template -> template.getMember().getId().equals(member1.getId())
                                    && (template.getTitle().contains(keyword)
                                    || template.getDescription().contains(keyword)))
            );
        }

        @Test
        @DisplayName("검색 기능: 회원 ID와 카테고리 ID로 템플릿 목록 조회 성공")
        void findAllSuccessByMemberIdAndCategoryId() {
            Long memberId = member1.getId();
            String keyword = null;
            Long categoryId = category1.getId();
            List<Long> tagIds = null;
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent()).hasSize(1),
                    () -> assertThat(actual.getContent().get(0).getMember().getId()).isEqualTo(member1.getId()),
                    () -> assertThat(actual.getContent().get(0).getCategory().getId()).isEqualTo(category1.getId())
            );
        }

        @Test
        @DisplayName("검색 기능: 회원 ID와 태그 ID 목록으로 템플릿 목록 조회 성공")
        void findAllSuccessByMemberIdAndTagIds() {
            Long memberId = member1.getId();
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = List.of(tag1.getId(), tag2.getId());
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent()).hasSize(2),
                    () -> assertThat(actual.getContent())
                            .containsExactlyInAnyOrder(
                                    templateRepository.fetchById(1L),
                                    templateRepository.fetchById(2L))
            );
        }

        @Test
        @DisplayName("검색 기능: 모든 검색 기준으로 템플릿 목록 조회 성공")
        void findAllSuccessWithAllCriteria() {
            Long memberId = member1.getId();
            String keyword = "Template";
            Long categoryId = category1.getId();
            List<Long> tagIds = List.of(tag1.getId(), tag2.getId());
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent()).hasSize(1),
                    () -> assertThat(actual.getContent()).containsExactlyInAnyOrder(templateRepository.fetchById(1L))
            );
        }

        @Test
        @DisplayName("검색 기능: 검색 결과가 없는 경우 빈 리스트 반환 성공")
        void findAllSuccessWithNoResults() {
            Long memberId = null;
            String keyword = "NonExistentKeyword";
            Long categoryId = null;
            List<Long> tagIds = null;
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, pageable);

            assertThat(actual.getContent()).isEmpty();
        }
    }

    @Nested
    class FindAllPagination {

        @BeforeEach
        void setUp() {
            saveTwoMembers();
            saveTwoCategory();

            for (int i = 0; i < 15; i++) {
                templateRepository.save(TemplateFixture.get(member1, category1));
            }
        }

        @Test
        @DisplayName("검색 기능: 두 번째 페이지 조회 성공")
        void findAllSuccessWithSecondPage() {
            Long memberId = member1.getId();
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = null;
            Pageable pageable = PageRequest.of(1, 10);

            Page<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent()).hasSize(5),
                    () -> assertThat(actual.getContent().get(0).getId()).isEqualTo(11L)
            );
        }

    }

    private void saveInitialData() {
        saveTwoMembers();
        saveTwoCategory();
        saveTwoTags();
        saveThreeTemplates();
        saveTemplateTags();
    }

    private void saveTwoMembers() {
        member1 = memberRepository.save(MemberFixture.getFirstMember());
        member2 = memberRepository.save(MemberFixture.getSecondMember());
    }

    private void saveTwoCategory() {
        category1 = categoryRepository.save(new Category("Category 1", member1));
        category2 = categoryRepository.save(new Category("Category 2", member1));
    }

    private void saveTwoTags() {
        tag1 = tagRepository.save(new Tag("Tag 1"));
        tag2 = tagRepository.save(new Tag("Tag 2"));
    }

    private void saveThreeTemplates() {
        template1 = templateRepository.save(TemplateFixture.get(member1, category1));
        template2 = templateRepository.save(TemplateFixture.get(member1, category2));
        template3 = templateRepository.save(TemplateFixture.get(member2, category1));

        SourceCode sourceCode1 = sourceCodeRepository.save(new SourceCode(template1, "filename1", "content1", 1));
        SourceCode sourceCode2 = sourceCodeRepository.save(new SourceCode(template2, "filename2", "content2", 2));
        SourceCode sourceCode3 = sourceCodeRepository.save(new SourceCode(template3, "filename1", "content1", 1));

        thumbnailRepository.save(new Thumbnail(template1, sourceCode1));
        thumbnailRepository.save(new Thumbnail(template2, sourceCode2));
        thumbnailRepository.save(new Thumbnail(template3, sourceCode3));
    }

    private void saveTemplateTags() {
        templateTagRepository.save(new TemplateTag(template1, tag1));
        templateTagRepository.save(new TemplateTag(template1, tag2));

        templateTagRepository.save(new TemplateTag(template2, tag1));
        templateTagRepository.save(new TemplateTag(template2, tag2));

        templateTagRepository.save(new TemplateTag(template3, tag2));
    }

}
