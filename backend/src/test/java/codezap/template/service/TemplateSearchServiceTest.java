package codezap.template.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.DatabaseIsolation;
import codezap.global.pagination.FixedPage;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.tag.domain.Tag;
import codezap.tag.repository.TagRepository;
import codezap.tag.repository.TemplateTagRepository;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.domain.Thumbnail;
import codezap.template.domain.Visibility;
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
    private List<Template> templates;

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

            var template1 = templateRepository.fetchById(1L);
            var template2 = templateRepository.fetchById(2L);
            var template3 = templateRepository.fetchById(3L);
            var template4 = templateRepository.fetchById(4L);

            templates = List.of(template1, template2, template3, template4);
        }

        @Test
        @DisplayName("검색 기능: 회원 ID로 템플릿 목록 조회 성공")
        void findAllSuccessByMemberId() {
            Long memberId = member1.getId();
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = null;
            Visibility visibility = null;
            Pageable pageable = PageRequest.of(0, 10);

            FixedPage<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, visibility, pageable);

            assertThat(actual.contents()).containsExactlyInAnyOrder(templates.stream()
                    .filter(template -> template.getMember().getId().equals(member1.getId()))
                    .toArray(Template[]::new));
        }

        @ParameterizedTest
        @DisplayName("검색 기능: 키워드로 템플릿 목록 조회 성공")
        @ValueSource(strings = {"안녕", "+안녕", "안+녕", " 안녕", "안녕+", "안녕-", "-안녕"})
        void findAllSuccessByKeyword(String keyword) {
            Long memberId = null;
            Long categoryId = null;
            List<Long> tagIds = null;
            Visibility visibility = null;
            Pageable pageable = PageRequest.of(0, 10);

            FixedPage<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, visibility, pageable);

            assertThat(actual.contents()).containsExactlyInAnyOrder(templates.stream()
                    .filter(template -> template.getTitle().contains("안녕") || template.getDescription().contains("안녕"))
                    .toArray(Template[]::new));
        }

        @Test
        @DisplayName("검색 기능: 카테고리 ID로 템플릿 목록 조회 성공")
        void findAllSuccessByCategoryId() {
            Long memberId = null;
            String keyword = null;
            Long categoryId = category1.getId();
            List<Long> tagIds = null;
            Visibility visibility = null;
            Pageable pageable = PageRequest.of(0, 10);

            FixedPage<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, visibility, pageable);

            assertThat(actual.contents()).containsExactlyInAnyOrder(templates.stream()
                    .filter(template -> template.getCategory().getId().equals(category1.getId()))
                    .toArray(Template[]::new));
        }

        @Test
        @DisplayName("검색 기능: 복수 태그 ID로 템플릿 목록 조회 성공")
        void findAllSuccessByTagIds() {
            Long memberId = null;
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = List.of(tag1.getId(), tag2.getId());
            Visibility visibility = null;
            Pageable pageable = PageRequest.of(0, 10);

            FixedPage<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, visibility, pageable);

            assertAll(
                    () -> assertThat(actual.contents())
                            .containsExactlyInAnyOrder(
                                    templateRepository.fetchById(1L),
                                    templateRepository.fetchById(2L),
                                    templateRepository.fetchById(3L)),
                    () -> assertThat(actual.contents()).hasSize(3)
            );
        }

        @Test
        @DisplayName("검색 기능: 단일 태그 ID로 템플릿 목록 조회 성공")
        void findAllSuccessBySingleTagId() {
            Long memberId = null;
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = List.of(tag1.getId(), tag2.getId());
            Visibility visibility = null;
            Pageable pageable = PageRequest.of(0, 10);

            FixedPage<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, visibility, pageable);

            assertAll(
                    () -> assertThat(actual.contents()).containsExactlyInAnyOrder(
                            templateRepository.fetchById(1L),
                            templateRepository.fetchById(2L),
                            templateRepository.fetchById(3L)),
                    () -> assertThat(actual.contents()).hasSize(3)
            );
        }

        @Test
        @DisplayName("검색 기능: 공개 범위로 템플릿 목록 조회 성공")
        void findAllSuccessByVisibility() {
            Long memberId = null;
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = null;
            Visibility visibility = Visibility.PUBLIC;
            Pageable pageable = PageRequest.of(0, 10);

            FixedPage<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, visibility, pageable);

            assertThat(actual.contents()).containsExactlyInAnyOrder(templates.stream()
                    .filter(template -> template.getVisibility().equals(visibility))
                    .toArray(Template[]::new));
        }

        @Test
        @DisplayName("검색 기능: 회원 ID와 키워드로 템플릿 목록 조회 성공")
        void findAllSuccessByMemberIdAndKeyword() {
            Long memberId = member1.getId();
            String keyword = "Template";
            Long categoryId = null;
            List<Long> tagIds = null;
            Visibility visibility = null;
            Pageable pageable = PageRequest.of(0, 10);

            FixedPage<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, visibility, pageable);

            assertThat(actual.contents()).containsExactlyInAnyOrder(templates.stream()
                    .filter(template -> template.getMember().getId().equals(member1.getId())
                            && (template.getTitle().contains(keyword) || template.getDescription().contains(keyword)))
                    .toArray(Template[]::new));
        }

        @Test
        @DisplayName("검색 기능: 회원 ID와 카테고리 ID로 템플릿 목록 조회 성공")
        void findAllSuccessByMemberIdAndCategoryId() {
            Long memberId = member1.getId();
            String keyword = null;
            Long categoryId = category1.getId();
            List<Long> tagIds = null;
            Visibility visibility = null;
            Pageable pageable = PageRequest.of(0, 10);

            FixedPage<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, visibility, pageable);

            assertThat(actual.contents()).containsExactlyInAnyOrder(templates.stream()
                    .filter(template -> template.getMember().getId().equals(member1.getId()) && template.getCategory()
                            .getId().equals(category1.getId()))
                    .toArray(Template[]::new));
        }

        @Test
        @DisplayName("검색 기능: 회원 ID와 공개 범위로 템플릿 목록 조회 성공")
        void findAllSuccessByMemberIdAndTagIds() {
            Long memberId = member1.getId();
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = List.of(tag1.getId(), tag2.getId());
            Visibility visibility = null;
            Pageable pageable = PageRequest.of(0, 10);

            FixedPage<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, visibility, pageable);

            assertAll(
                    () -> assertThat(actual.contents()).hasSize(2),
                    () -> assertThat(actual.contents())
                            .containsExactlyInAnyOrder(
                                    templateRepository.fetchById(1L),
                                    templateRepository.fetchById(2L))
            );
        }

        @Test
        @DisplayName("검색 기능: 회원 ID와 공개 범위으로 템플릿 목록 조회 성공")
        void findAllSuccessByMemberIdAndVisibility() {
            Long memberId = member2.getId();
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = null;
            Visibility visibility = Visibility.PUBLIC;
            Pageable pageable = PageRequest.of(0, 10);

            FixedPage<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, visibility, pageable);

            assertThat(actual.contents()).containsExactlyInAnyOrder(templates.stream()
                    .filter(template -> template.getMember().getId().equals(member2.getId())
                            && template.getVisibility().equals(visibility))
                    .toArray(Template[]::new));
        }

        @Test
        @DisplayName("검색 기능: 모든 검색 기준으로 템플릿 목록 조회 성공")
        void findAllSuccessWithAllCriteria() {
            Long memberId = member1.getId();
            String keyword = "안녕";
            Long categoryId = category1.getId();
            List<Long> tagIds = List.of(tag1.getId(), tag2.getId());
            Visibility visibility = Visibility.PUBLIC;
            Pageable pageable = PageRequest.of(0, 10);

            FixedPage<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, visibility, pageable);

            assertAll(
                    () -> assertThat(actual.contents()).hasSize(1),
                    () -> assertThat(actual.contents()).containsExactlyInAnyOrder(templateRepository.fetchById(1L))
            );
        }

        @Test
        @DisplayName("검색 기능: 검색 결과가 없는 경우 빈 리스트 반환 성공")
        void findAllSuccessWithNoResults() {
            Long memberId = null;
            String keyword = "설명";
            Long categoryId = null;
            List<Long> tagIds = null;
            Visibility visibility = null;
            Pageable pageable = PageRequest.of(0, 10);

            FixedPage<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, visibility, pageable);

            assertThat(actual.contents()).isEmpty();
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
            Visibility visibility = null;
            Pageable pageable = PageRequest.of(1, 10);

            FixedPage<Template> actual = sut.findAllBy(memberId, keyword, categoryId, tagIds, visibility, pageable);

            assertAll(
                    () -> assertThat(actual.contents()).hasSize(5),
                    () -> assertThat(actual.contents().get(0).getId()).isEqualTo(11L)
            );
        }
    }

    private void saveInitialData() {
        saveTwoMembers();
        saveTwoCategory();
        saveTwoTags();
        saveFourTemplates();
        saveTemplateTwoTags();
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

    private void saveFourTemplates() {
        var template1 = templateRepository.save(TemplateFixture.get(member1, category1));
        var template2 = templateRepository.save(TemplateFixture.get(member1, category2));
        var template3 = templateRepository.save(TemplateFixture.get(member2, category1));
        var template4 = templateRepository.save(TemplateFixture.getPrivate(member2, category2));

        SourceCode sourceCode1 = sourceCodeRepository.save(new SourceCode(template1, "filename1", "content1", 1));
        SourceCode sourceCode2 = sourceCodeRepository.save(new SourceCode(template2, "filename2", "content2", 2));
        SourceCode sourceCode3 = sourceCodeRepository.save(new SourceCode(template3, "filename1", "content1", 1));
        SourceCode sourceCode4 = sourceCodeRepository.save(new SourceCode(template4, "filename1", "content1", 1));

        thumbnailRepository.save(new Thumbnail(template1, sourceCode1));
        thumbnailRepository.save(new Thumbnail(template2, sourceCode2));
        thumbnailRepository.save(new Thumbnail(template3, sourceCode3));
        thumbnailRepository.save(new Thumbnail(template4, sourceCode4));
    }

    private void saveTemplateTwoTags() {
        var template1 = templateRepository.fetchById(1L);

        templateTagRepository.save(new TemplateTag(template1, tag1));
        templateTagRepository.save(new TemplateTag(template1, tag2));

        var template2 = templateRepository.fetchById(2L);
        templateTagRepository.save(new TemplateTag(template2, tag1));
        templateTagRepository.save(new TemplateTag(template2, tag2));

        var template3 = templateRepository.fetchById(3L);
        templateTagRepository.save(new TemplateTag(template3, tag2));
    }

}
