package codezap.template.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.global.auditing.JpaAuditingConfiguration;
import codezap.global.querydsl.QueryDSLConfig;
import codezap.global.rds.DataSourceConfig;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.tag.domain.Tag;
import codezap.tag.repository.TagRepository;
import codezap.template.domain.Template;
import codezap.template.domain.Visibility;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class), useDefaultFilters = false)
@Import({JpaAuditingConfiguration.class, DataSourceConfig.class, QueryDSLConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:search.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class TemplateSearchJpaRepositoryTest {

    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("검색 테스트: 회원 ID로 템플릿 조회 성공")
    void testFindByMemberId() {
        Member member1 = memberRepository.fetchById(1L);
        Page<Template> result = templateRepository.findAll(
                member1.getId(),
                null,
                null,
                null,
                null,
                PageRequest.of(0, 10));

        assertAll(
                () -> assertThat(result.getContent()).hasSize(2),
                () -> assertThat(result.getContent())
                        .allMatch(template -> template.getMember().getId().equals(member1.getId()))
        );
    }

    @Test
    @DisplayName("검색 테스트: 키워드로 템플릿 조회 성공")
    void testFindByKeyword() {
        String keyword = "Template";
        Page<Template> result = templateRepository.findAll(
                null,
                keyword,
                null,
                null,
                null,
                PageRequest.of(0, 10));

        assertAll(
                () -> assertThat(result.getContent())
                        .allMatch(template -> template.getTitle().contains(keyword)
                                || template.getDescription().contains(keyword)),
                () -> assertThat(result.getContent()).hasSize(4)
        );
    }

    @Test
    @DisplayName("검색 테스트: 카테고리 ID로 템플릿 조회 성공")
    void testFindByCategoryId() {
        Category category1 = categoryRepository.fetchById(1L);
        Page<Template> result = templateRepository.findAll(
                null,
                null,
                category1.getId(),
                null,
                null,
                PageRequest.of(0, 10));

        assertAll(
                () -> assertThat(result.getContent()).hasSize(2),
                () -> assertThat(result.getContent())
                        .allMatch(template -> template.getCategory().getId().equals(category1.getId()))
        );
    }

    @Test
    @DisplayName("검색 테스트: 태그 ID 목록으로 템플릿 조회, 태그 중 하나라도 가진 템플릿 전체 조회 성공")
    void testFindByTagIds() {
        Tag tag1 = tagRepository.fetchById(1L);
        Tag tag2 = tagRepository.fetchById(2L);
        List<Long> tagIds = Arrays.asList(tag1.getId(), tag2.getId());
        Page<Template> result = templateRepository.findAll(
                null,
                null,
                null,
                tagIds,
                null,
                PageRequest.of(0, 10));

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
    @DisplayName("검색 테스트: 단일 태그 ID로 템플릿 조회 성공")
    void testFindBySingleTagId() {
        Tag tag2 = tagRepository.fetchById(2L);
        List<Long> tagIds = List.of(tag2.getId());
        Page<Template> result = templateRepository.findAll(
                null,
                null,
                null,
                tagIds,
                null,
                PageRequest.of(0, 10));

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
    @DisplayName("검색 테스트: 공개 범위로 템플릿 조회 성공")
    void testFindByVisibility() {
        Page<Template> actual = templateRepository.findAll(
                null,
                null,
                null,
                null,
                Visibility.PRIVATE,
                PageRequest.of(0, 10));

        assertThat(actual.getContent()).hasSize(1)
                .containsExactlyInAnyOrder(templateRepository.fetchById(4L));
    }

    @Test
    @DisplayName("검색 테스트: 회원 ID와 키워드로 템플릿 조회 성공")
    void testFindByMemberIdAndKeyword() {
        Member member1 = memberRepository.fetchById(1L);
        String keyword = "Template";
        Page<Template> result = templateRepository.findAll(
                member1.getId(),
                keyword,
                null,
                null,
                null,
                PageRequest.of(0, 10));

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
        Member member1 = memberRepository.fetchById(1L);
        Category category1 = categoryRepository.fetchById(1L);
        Page<Template> result = templateRepository.findAll(
                member1.getId(),
                null,
                category1.getId(),
                null,
                null,
                PageRequest.of(0, 10));

        assertAll(
                () -> assertThat(result.getContent()).hasSize(1),
                () -> assertThat(result.getContent().get(0).getMember().getId()).isEqualTo(member1.getId()),
                () -> assertThat(result.getContent().get(0).getCategory().getId()).isEqualTo(category1.getId())
        );
    }

    @Test
    @DisplayName("검색 테스트: 회원 ID와 태그 ID 목록으로 템플릿 조회 성공")
    void testFindByMemberIdAndTagIds() {
        Member member1 = memberRepository.fetchById(1L);
        Tag tag1 = tagRepository.fetchById(1L);
        Tag tag2 = tagRepository.fetchById(2L);
        List<Long> tagIds = Arrays.asList(tag1.getId(), tag2.getId());
        Page<Template> result = templateRepository.findAll(
                member1.getId(),
                null,
                null,
                tagIds,
                null,
                PageRequest.of(0, 10));

        assertAll(
                () -> assertThat(result.getContent()).hasSize(2),
                () -> assertThat(result.getContent()).containsExactlyInAnyOrder(templateRepository.fetchById(1L),
                        templateRepository.fetchById(2L))
        );
    }

    @Test
    @DisplayName("검색 테스트: 회원 ID와 공개 범위로 템플릿 조회 성공")
    void testFindByMemberIdAndVisibility() {
        Member member1 = memberRepository.fetchById(2L);
        Page<Template> actual = templateRepository.findAll(
                member1.getId(),
                null,
                null,
                null,
                Visibility.PUBLIC,
                PageRequest.of(0, 10));

        assertThat(actual.getContent()).hasSize(1)
                .containsExactlyInAnyOrder(templateRepository.fetchById(3L));
    }

    @Test
    @DisplayName("검색 테스트: 모든 검색 기준으로 템플릿 조회 성공")
    void testFindWithAllCriteria() {
        Member member1 = memberRepository.fetchById(1L);
        Category category1 = categoryRepository.fetchById(1L);
        Tag tag1 = tagRepository.fetchById(1L);
        Tag tag2 = tagRepository.fetchById(2L);
        String keyword = "Template";
        List<Long> tagIds = Arrays.asList(tag1.getId(), tag2.getId());
        Page<Template> result = templateRepository.findAll(
                member1.getId(),
                keyword,
                category1.getId(),
                tagIds,
                Visibility.PUBLIC,
                PageRequest.of(0, 10));

        assertAll(
                () -> assertThat(result.getContent()).hasSize(1),
                () -> assertThat(result.getContent()).containsExactlyInAnyOrder(templateRepository.fetchById(1L))
        );
    }

    @Test
    @DisplayName("검색 테스트: 검색 결과가 없는 경우 빈 리스트 반환 성공")
    void testFindWithNoResults() {
        Page<Template> result = templateRepository.findAll(
                null,
                "없지롱",
                null,
                null,
                null,
                PageRequest.of(0, 10));

        assertThat(result.getContent()).isEmpty();
    }
}
