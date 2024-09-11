package codezap.template.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.repository.JpaRepositoryTest;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.tag.domain.Tag;
import codezap.tag.repository.TagRepository;
import codezap.tag.repository.TemplateTagRepository;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;

@JpaRepositoryTest
class TemplateRepositoryFindAllTest {

    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TemplateTagRepository templateTagRepository;

    private Member member1, member2;
    private Category category1, category2;
    private Tag tag1, tag2;
    private Template template1, template2, template3;

    @BeforeEach
    void setUp() {
        member1 = memberRepository.save(MemberFixture.getFirstMember());
        member2 = memberRepository.save(MemberFixture.getSecondMember());

        category1 = categoryRepository.save(new Category("Category 1", member1));
        category2 = categoryRepository.save(new Category("Category 2", member1));

        tag1 = tagRepository.save(new Tag("Tag 1"));
        tag2 = tagRepository.save(new Tag("Tag 2"));

        template1 = templateRepository.save(TemplateFixture.get(member1, category1));
        template2 = templateRepository.save(TemplateFixture.get(member1, category2));
        template3 = templateRepository.save(TemplateFixture.get(member2, category1));

        templateTagRepository.save(new TemplateTag(template1, tag1));
        templateTagRepository.save(new TemplateTag(template1, tag2));

        templateTagRepository.save(new TemplateTag(template2, tag1));
        templateTagRepository.save(new TemplateTag(template2, tag2));

        templateTagRepository.save(new TemplateTag(template3, tag2));
    }

    @Nested
    @DisplayName("단일 조건 검색 테스트")
    class SingleCriteriaTests {

        @Test
        @DisplayName("회원 ID로 템플릿 조회")
        void testFindByMemberId() {
            Specification<Template> spec = new TemplateSpecification(member1.getId(), null, null, null);
            Page<Template> result = templateRepository.findAll(spec, PageRequest.of(0, 10));

            assertAll(
                    () -> assertThat(result.getContent()).hasSize(2),
                    () -> assertThat(result.getContent())
                            .allMatch(template -> template.getMember().getId().equals(member1.getId()))
            );
        }

        @Test
        @DisplayName("키워드로 템플릿 조회")
        void testFindByKeyword() {
            String keyword = "Template";
            Specification<Template> spec = new TemplateSpecification(null, keyword, null, null);
            Page<Template> result = templateRepository.findAll(spec, PageRequest.of(0, 10));

            assertAll(
                    () -> assertThat(result.getContent())
                            .allMatch(template ->
                                    template.getTitle().contains(keyword) || template.getDescription().contains(keyword)),
                    () -> assertThat(result.getContent()).hasSize(3)
            );
        }

        @Test
        @DisplayName("카테고리 ID로 템플릿 조회")
        void testFindByCategoryId() {
            Specification<Template> spec = new TemplateSpecification(null, null, category1.getId(),
                    null);
            Page<Template> result = templateRepository.findAll(spec, PageRequest.of(0, 10));

            assertAll(
                    () -> assertThat(result.getContent()).hasSize(2),
                    () -> assertThat(result.getContent())
                            .allMatch(template -> template.getCategory().getId().equals(category1.getId()))
            );
        }

        @Test
        @DisplayName("태그 ID 목록으로 템플릿 조회: 모든 태그를 가진 템플릿만 조회")
        void testFindByTagIds() {
            List<Long> tagIds = Arrays.asList(tag1.getId(), tag2.getId());
            Specification<Template> spec = new TemplateSpecification(null, null, null, tagIds);
            Page<Template> result = templateRepository.findAll(spec, PageRequest.of(0, 10));

            assertAll(
                    () -> assertThat(result.getContent())
                            .containsExactlyInAnyOrder(
                                    templateRepository.fetchById(1L),
                                    templateRepository.fetchById(2L)),
                    () -> assertThat(result.getContent()).hasSize(2)
            );
        }

        @Test
        @DisplayName("단일 태그 ID로 템플릿 조회")
        void testFindBySingleTagId() {
            List<Long> tagIds = Arrays.asList(tag2.getId());
            Specification<Template> spec = new TemplateSpecification(null, null, null, tagIds);
            Page<Template> result = templateRepository.findAll(spec, PageRequest.of(0, 10));

            assertAll(
                    () -> assertThat(result.getContent()).containsExactlyInAnyOrder(
                            templateRepository.fetchById(1L),
                            templateRepository.fetchById(2L),
                            templateRepository.fetchById(3L)),
                    () -> assertThat(result.getContent()).hasSize(3)
            );
        }
    }

    @Nested
    @DisplayName("복합 조건 검색 테스트")
    class MultipleCriteriaTests {

        @Test
        @DisplayName("회원 ID와 키워드로 템플릿 조회")
        void testFindByMemberIdAndKeyword() {
            String keyword = "Template";
            Specification<Template> spec = new TemplateSpecification(member1.getId(), keyword, null,
                    null);
            Page<Template> result = templateRepository.findAll(spec, PageRequest.of(0, 10));

            assertAll(
                    () -> assertThat(result.getContent()).hasSize(2),
                    () -> assertThat(result.getContent())
                            .allMatch(template ->
                                    template.getMember().getId().equals(member1.getId())
                                            && (template.getTitle().contains(keyword) || template.getDescription().contains(keyword)))
            );
        }

        @Test
        @DisplayName("회원 ID와 카테고리 ID로 템플릿 조회")
        void testFindByMemberIdAndCategoryId() {
            Specification<Template> spec = new TemplateSpecification(member1.getId(), null, category1.getId(),
                    null);
            Page<Template> result = templateRepository.findAll(spec, PageRequest.of(0, 10));

            assertAll(
                    () -> assertThat(result.getContent()).hasSize(1),
                    () -> assertThat(result.getContent().get(0).getMember().getId()).isEqualTo(member1.getId()),
                    () -> assertThat(result.getContent().get(0).getCategory().getId()).isEqualTo(category1.getId())
            );
        }

        @Test
        @DisplayName("회원 ID와 태그 ID 목록으로 템플릿 조회")
        void testFindByMemberIdAndTagIds() {
            List<Long> tagIds = Arrays.asList(tag1.getId(), tag2.getId());
            Specification<Template> spec = new TemplateSpecification(member1.getId(), null, null,
                    tagIds);
            Page<Template> result = templateRepository.findAll(spec, PageRequest.of(0, 10));

            assertAll(
                    () -> assertThat(result.getContent()).hasSize(2),
                    () -> assertThat(result.getContent()).containsExactlyInAnyOrder(templateRepository.fetchById(1L),
                            templateRepository.fetchById(2L))
            );
        }

        @Test
        @DisplayName("모든 검색 기준으로 템플릿 조회")
        void testFindWithAllCriteria() {
            String keyword = "Template";
            List<Long> tagIds = Arrays.asList(tag1.getId(), tag2.getId());
            Specification<Template> spec = new TemplateSpecification(member1.getId(), keyword, category1.getId(),
                    tagIds);
            Page<Template> result = templateRepository.findAll(spec, PageRequest.of(0, 10));

            assertAll(
                    () -> assertThat(result.getContent()).hasSize(1),
                    () -> assertThat(result.getContent()).containsExactlyInAnyOrder(templateRepository.fetchById(1L))
            );
        }
    }

    @Test
    @DisplayName("검색 결과가 없는 경우 빈 리스트를 반환")
    void testFindWithNoResults() {
        Specification<Template> spec = new TemplateSpecification(member1.getId(), "NonexistentKeyword", null, null);
        Page<Template> result = templateRepository.findAll(spec, PageRequest.of(0, 10));

        assertThat(result.getContent()).isEmpty();
    }
}
