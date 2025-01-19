package codezap.tag.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.exception.CodeZapException;
import codezap.global.repository.RepositoryTest;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.tag.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.repository.TemplateRepository;

@RepositoryTest
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TemplateTagRepository templateTagRepository;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Nested
    @DisplayName("id로 태그 조회")
    class FetchById {

        @Test
        @DisplayName("id로 태그 조회 성공 : id로 태그를 알아낼 수 있다.")
        void fetchByIdSuccess() {
            Tag tag = tagRepository.save(new Tag("tag"));

            Tag actual = tagRepository.fetchById(tag.getId());

            assertThat(actual).isEqualTo(tag);
        }

        @Test
        @DisplayName("id로 태그 조회 실패 : 존재하지 않는 id인 경우 에러가 발생한다.")
        void fetchByIdFailByNotExistsId() {
            long notExistId = 100;

            assertThatThrownBy(() -> tagRepository.fetchById(notExistId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notExistId + "에 해당하는 태그가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("태그명으로 태그 조회(find)")
    class FindByName {

        @Test
        @DisplayName("태그명으로 태그 조회 성공 : 태그명으로 태그를 알아낼 수 있다.")
        void findByNameSuccess() {
            Tag tag = tagRepository.save(new Tag("태그"));

            Optional<Tag> actual = tagRepository.findByName(tag.getName());

            assertThat(actual).hasValue(tag);
        }

        @Test
        @DisplayName("태그명으로 태그 조회 실패 : 존재하지 않는 태그명인 경우 optional 값이 반환된다.")
        void findByNameFailByNotExistsId() {
            Optional<Tag> actual = tagRepository.findByName("태그");

            assertThat(actual).isEmpty();
        }
    }

    @Nested
    @DisplayName("태그명 리스트로 태그 조회")
    class FindAllByNames {

        @Test
        @DisplayName("태그명 리스트로 태그 조회 성공")
        void findAllByNamesSuccess() {
            Tag lowerCaseTag1 = tagRepository.save(new Tag("java"));
            Tag lowerCaseTag2 = tagRepository.save(new Tag("javascript"));
            Tag lowerCaseTag3 = tagRepository.save(new Tag("typescript"));
            Tag upperCaseTag1 = tagRepository.save(new Tag("Java"));
            Tag upperCaseTag2 = tagRepository.save(new Tag("Javascript"));
            Tag upperCaseTag3 = tagRepository.save(new Tag("Typescript"));
            tagRepository.saveAll(List.of(
                    lowerCaseTag1,
                    lowerCaseTag2,
                    lowerCaseTag3,
                    upperCaseTag1,
                    upperCaseTag2,
                    upperCaseTag3));

            var names = List.of(lowerCaseTag1.getName(), lowerCaseTag3.getName());
            var actual = tagRepository.findAllByNames(names);

            assertThat(actual)
                    .containsExactlyInAnyOrder(lowerCaseTag1, lowerCaseTag3)
                    .doesNotContain(lowerCaseTag2, upperCaseTag1, upperCaseTag2, upperCaseTag3);
        }
    }

    @Nested
    @DisplayName("특정 날짜 이후 템플릿 태그가 가장 많이 생성된 태그 목록을 최신순으로 조회")
    class FindMostUsedTagsWithinDateRange {

        @Test
        @DisplayName("성공")
        void findMostUsedTagsWithinDateRange() {
            // Given
            Member member1 = memberRepository.save(MemberFixture.getFirstMember());
            Category category1 = categoryRepository.save(CategoryFixture.getDefaultCategory(member1));
            Template template1 = templateRepository.save(TemplateFixture.get(member1, category1));

            Member member2 = memberRepository.save(MemberFixture.getSecondMember());
            Category category2 = categoryRepository.save(CategoryFixture.getCategory(member2));
            Template template2 = templateRepository.save(TemplateFixture.get(member2, category2));

            Tag tag1 = tagRepository.save(new Tag("Tag1"));
            TemplateTag templateTag1 = templateTagRepository.save(new TemplateTag(template1, tag1));

            Tag tag2 = tagRepository.save(new Tag("Tag2"));
            TemplateTag templateTag2 = templateTagRepository.save(new TemplateTag(template2, tag2));
            TemplateTag templateTag3 = templateTagRepository.save(new TemplateTag(template2, tag2));

            // When
            List<Tag> tags = tagRepository.findMostUsedTagsWithinDateRange(1, template1.getCreatedAt().toLocalDate());

            // Then
            assertThat(tags).containsExactlyInAnyOrder(tag2);
        }

        @Test
        @DisplayName("성공: 가장 많이 사용된 태그 목록인 경우 최근 태그 조회")
        void findMostUsedTagsWithinDateRangeWhen() {
            // Given
            Member member1 = memberRepository.save(MemberFixture.getFirstMember());
            Category category1 = categoryRepository.save(CategoryFixture.getDefaultCategory(member1));
            Template template1 = templateRepository.save(TemplateFixture.get(member1, category1));

            Tag tag1 = tagRepository.save(new Tag("Tag1"));
            TemplateTag templateTag1 = templateTagRepository.save(new TemplateTag(template1, tag1));

            Tag tag2 = tagRepository.save(new Tag("Tag2"));
            TemplateTag templateTag2 = templateTagRepository.save(new TemplateTag(template1, tag2));

            // When
            List<Tag> tags = tagRepository.findMostUsedTagsWithinDateRange(1, templateTag2.getCreatedAt().toLocalDate());

            // Then
            assertThat(tags).containsExactlyInAnyOrder(tag2);
        }

        @Test
        @DisplayName("성공: 새로 생성된 템플릿태그가 없는 경우 빈 리스트 반환")
        void findMostUsedTagsWithinDateRangeWhenNoTags() {
            // Given
            Tag tag1 = tagRepository.save(new Tag("Tag1"));

            // When
            List<Tag> tags = tagRepository.findMostUsedTagsByRecentTemplates(1);

            // Then
            assertThat(tags).isEmpty();
        }
    }

    @Nested
    @DisplayName("템플릿 태그가 가장 많이 생성된 태그 목록을 최신순으로 조회")
    class FindMostUsedTagsByRecentTemplates {

        @Test
        @DisplayName("성공")
        void findMostUsedTagsByRecentTemplates() {
            // Given
            Member member1 = memberRepository.save(MemberFixture.getFirstMember());
            Category category1 = categoryRepository.save(CategoryFixture.getDefaultCategory(member1));
            Template template1 = templateRepository.save(TemplateFixture.get(member1, category1));

            Member member2 = memberRepository.save(MemberFixture.getSecondMember());
            Category category2 = categoryRepository.save(CategoryFixture.getCategory(member2));
            Template template2 = templateRepository.save(TemplateFixture.get(member2, category2));

            Tag tag1 = tagRepository.save(new Tag("Tag1"));
            TemplateTag templateTag1 = templateTagRepository.save(new TemplateTag(template1, tag1));

            Tag tag2 = tagRepository.save(new Tag("Tag2"));
            TemplateTag templateTag2 = templateTagRepository.save(new TemplateTag(template2, tag2));
            TemplateTag templateTag3 = templateTagRepository.save(new TemplateTag(template2, tag2));

            // When
            List<Tag> tags = tagRepository.findMostUsedTagsByRecentTemplates(1);

            // Then
            assertThat(tags).containsExactlyInAnyOrder(tag2);
        }

        @Test
        @DisplayName("성공: 가장 많이 사용된 태그 목록인 경우 최근 태그 조회")
        void findMostUsedTagsWithinDateRangeWhenCountSame() {
            // Given
            Member member1 = memberRepository.save(MemberFixture.getFirstMember());
            Category category1 = categoryRepository.save(CategoryFixture.getDefaultCategory(member1));
            Template template1 = templateRepository.save(TemplateFixture.get(member1, category1));

            Tag tag1 = tagRepository.save(new Tag("Tag1"));
            TemplateTag templateTag1 = templateTagRepository.save(new TemplateTag(template1, tag1));

            Tag tag2 = tagRepository.save(new Tag("Tag2"));
            TemplateTag templateTag2 = templateTagRepository.save(new TemplateTag(template1, tag2));

            // When
            List<Tag> tags = tagRepository.findMostUsedTagsByRecentTemplates(1);

            // Then
            assertThat(tags).containsExactlyInAnyOrder(tag2);
        }

        @Test
        @DisplayName("성공: 새로 생성된 템플릿태그가 없는 경우 빈 리스트 반환")
        void findMostUsedTagsWithinDateRangeWhenNoCount() {
            // Given
            Tag tag1 = tagRepository.save(new Tag("Tag1"));

            // When
            List<Tag> tags = tagRepository.findMostUsedTagsByRecentTemplates(1);

            // Then
            assertThat(tags).isEmpty();
        }
    }
}
