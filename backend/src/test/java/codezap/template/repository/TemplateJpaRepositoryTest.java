package codezap.template.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.exception.CodeZapException;
import codezap.global.repository.JpaRepositoryTest;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.tag.domain.Tag;
import codezap.tag.repository.TagRepository;
import codezap.tag.repository.TemplateTagRepository;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;

@JpaRepositoryTest
class TemplateJpaRepositoryTest {

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

    @Test
    @DisplayName("카테고리 id로 템플릿 존재 여부 확인 ")
    void existsByCategoryId() {
        // given
        Member member = memberRepository.save(MemberFixture.getFirstMember());
        Member member2 = memberRepository.save(MemberFixture.getSecondMember());
        Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
        Category otherCategory = categoryRepository.save(CategoryFixture.getSecondCategory());
        templateRepository.save(new Template(member, "Template 1", "Description 1", category));

        assertAll(
                () -> assertThat(templateRepository.existsByCategoryId(category.getId())).isTrue(),
                () -> assertThat(templateRepository.existsByCategoryId(otherCategory.getId())).isFalse()
        );
    }

    @Nested
    @DisplayName("템플릿 id로 템플릿 조회")
    class fetchById {

        @Test
        @DisplayName("템플릿 id로 템플릿 조회 성공")
        void fetchById() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
            Template savedTemplate = templateRepository.save(TemplateFixture.get(member, category));

            assertThat(templateRepository.fetchById(savedTemplate.getId())).isEqualTo(savedTemplate);
        }

        @Test
        @DisplayName("템플릿 id로 템플릿 조회 실패: 존재하지 않는 id")
        void fetchById_WhenNotExistsId() {
            Long notSavedId = 1L;
            assertThatThrownBy(() -> templateRepository.fetchById(notSavedId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notSavedId + "에 해당하는 템플릿이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("회원 id로 템플릿 조회")
    class findByMemberId {

        @Test
        @DisplayName("회원 id로 템플릿 조회 성공")
        void findByMemberId() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
            Template savedTemplate = templateRepository.save(TemplateFixture.get(member, category));

            assertThat(templateRepository.findByMemberId(member.getId())).containsExactly(savedTemplate);
        }

        @Test
        @DisplayName("회원 id로 템플릿 조회 실패: 존재하지 않는 회원")
        void findByMemberIdWhenNotExistsMember() {
            Long notSavedId = 1L;
            assertThat(templateRepository.findByMemberId(notSavedId)).isEmpty();
        }
    }

    @Nested
    @DisplayName("검색 테스트")
    class FindAll {

        private Member member1, member2;
        private Category category1, category2;
        private Tag tag1, tag2;
        private Template template1, template2, template3;

        @Test
        @DisplayName("검색 테스트: 회원 ID로 템플릿 조회 성공")
        void testFindByMemberId() {
            saveTwoMembers();
            saveTwoCategory();
            saveTwoTags();
            saveThreeTemplates();
            saveTemplateTags();

            Specification<Template> spec = new TemplateSpecification(member1.getId(), null, null, null);
            Page<Template> result = templateRepository.findAll(spec, PageRequest.of(0, 10));

            assertAll(
                    () -> assertThat(result.getContent()).hasSize(2),
                    () -> assertThat(result.getContent())
                            .allMatch(template -> template.getMember().getId().equals(member1.getId()))
            );
        }

        @Test
        @DisplayName("검색 테스트: 키워드로 템플릿 조회 성공")
        void testFindByKeyword() {
            saveTwoMembers();
            saveTwoCategory();
            saveTwoTags();
            saveThreeTemplates();
            saveTemplateTags();

            String keyword = "Template";
            Specification<Template> spec = new TemplateSpecification(null, keyword, null, null);
            Page<Template> result = templateRepository.findAll(spec, PageRequest.of(0, 10));

            assertAll(
                    () -> assertThat(result.getContent())
                            .allMatch(template -> template.getTitle().contains(keyword)
                                    || template.getDescription().contains(keyword)),
                    () -> assertThat(result.getContent()).hasSize(3)
            );
        }

        @Test
        @DisplayName("검색 테스트: 카테고리 ID로 템플릿 조회 성공")
        void testFindByCategoryId() {
            saveTwoMembers();
            saveTwoCategory();
            saveTwoTags();
            saveThreeTemplates();
            saveTemplateTags();

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
        @DisplayName("검색 테스트: 태그 ID 목록으로 템플릿 조회, 모든 태그를 가진 템플릿만 조회 성공")
        void testFindByTagIds() {
            saveTwoMembers();
            saveTwoCategory();
            saveTwoTags();
            saveThreeTemplates();
            saveTemplateTags();

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
        @DisplayName("검색 테스트: 단일 태그 ID로 템플릿 조회 성공")
        void testFindBySingleTagId() {
            saveTwoMembers();
            saveTwoCategory();
            saveTwoTags();
            saveThreeTemplates();
            saveTemplateTags();

            List<Long> tagIds = Arrays.asList(tag2.getId());
            Specification<Template> spec = new TemplateSpecification(null, null, null, tagIds);
            Page<Template> result = templateRepository.findAll(spec, PageRequest.of(0, 10));

            assertAll(
                    () -> assertThat(result.getContent())
                            .containsExactlyInAnyOrder(
                                    templateRepository.fetchById(1L),
                                    templateRepository.fetchById(2L),
                                    templateRepository.fetchById(3L)),
                    () -> assertThat(result.getContent()).hasSize(3)
            );
        }

        @Test
        @DisplayName("검색 테스트: 회원 ID와 키워드로 템플릿 조회 성공")
        void testFindByMemberIdAndKeyword() {
            saveTwoMembers();
            saveTwoCategory();
            saveTwoTags();
            saveThreeTemplates();
            saveTemplateTags();

            String keyword = "Template";
            Specification<Template> spec = new TemplateSpecification(member1.getId(), keyword, null,
                    null);
            Page<Template> result = templateRepository.findAll(spec, PageRequest.of(0, 10));

            assertAll(
                    () -> assertThat(result.getContent()).hasSize(2),
                    () -> assertThat(result.getContent())
                            .allMatch(template -> template.getMember().getId().equals(member1.getId())
                                    && (template.getTitle().contains(keyword)
                                    || template.getDescription().contains(keyword)))
            );
        }

        @Test
        @DisplayName("검색 테스트: 회원 ID와 카테고리 ID로 템플릿 조회 성공")
        void testFindByMemberIdAndCategoryId() {
            saveTwoMembers();
            saveTwoCategory();
            saveTwoTags();
            saveThreeTemplates();
            saveTemplateTags();

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
        @DisplayName("검색 테스트: 회원 ID와 태그 ID 목록으로 템플릿 조회 성공")
        void testFindByMemberIdAndTagIds() {
            saveTwoMembers();
            saveTwoCategory();
            saveTwoTags();
            saveThreeTemplates();
            saveTemplateTags();

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
        @DisplayName("검색 테스트: 모든 검색 기준으로 템플릿 조회 성공")
        void testFindWithAllCriteria() {
            saveTwoMembers();
            saveTwoCategory();
            saveTwoTags();
            saveThreeTemplates();
            saveTemplateTags();

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

        @Test
        @DisplayName("검색 테스트: 검색 결과가 없는 경우 빈 리스트 반환 성공")
        void testFindWithNoResults() {
            saveTwoMembers();
            saveTwoCategory();
            saveTwoTags();
            saveThreeTemplates();
            saveTemplateTags();

            Specification<Template> spec = new TemplateSpecification(null, "NonexistentKeyword", null, null);
            Page<Template> result = templateRepository.findAll(spec, PageRequest.of(0, 10));

            assertThat(result.getContent()).isEmpty();
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
        }

        private void saveTemplateTags() {
            templateTagRepository.save(new TemplateTag(template1, tag1));
            templateTagRepository.save(new TemplateTag(template1, tag2));

            templateTagRepository.save(new TemplateTag(template2, tag1));
            templateTagRepository.save(new TemplateTag(template2, tag2));

            templateTagRepository.save(new TemplateTag(template3, tag2));
        }
    }
}
