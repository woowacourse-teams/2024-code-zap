package codezap.tag.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
class TemplateTagRepositoryTest {

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

    private Member member;
    private Category category;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MemberFixture.getFirstMember());
        category = categoryRepository.save(CategoryFixture.getDefaultCategory(member));
    }

    @Test
    @DisplayName("Template 을 이용한 Tag 목록 조회 성공")
    void findAllTagsByTemplateTest() {
        //given
        Template template = templateRepository.save(TemplateFixture.get(member, category));

        Tag tag1 = tagRepository.save(new Tag("tag1"));
        Tag tag2 = tagRepository.save(new Tag("tag2"));
        Tag tag3 = tagRepository.save(new Tag("tag3"));

        templateTagRepository.save(new TemplateTag(template, tag1));
        templateTagRepository.save(new TemplateTag(template, tag2));

        //when
        List<Tag> tags = templateTagRepository.findAllTagsByTemplate(template);

        //then
        assertThat(tags).containsExactly(tag1, tag2)
                .doesNotContain(tag3);
    }


    @Test
    @DisplayName("Template Id 을 이용한 TemplateTag 목록 조회 성공")
    void findAllByTemplateIdTest() {
        //given
        Template template = templateRepository.save(TemplateFixture.get(member, category));

        Tag tag1 = tagRepository.save(new Tag("tag1"));
        Tag tag2 = tagRepository.save(new Tag("tag2"));
        Tag tag3 = tagRepository.save(new Tag("tag3"));

        TemplateTag templateTag1 = templateTagRepository.save(new TemplateTag(template, tag1));
        TemplateTag templateTag2 = templateTagRepository.save(new TemplateTag(template, tag2));

        //when
        List<TemplateTag> templateTags = templateTagRepository.findAllByTemplateId(template.getId());

        //then
        assertThat(templateTags).containsExactly(templateTag1, templateTag2)
                .doesNotContain(new TemplateTag(template, tag3));
    }

    @Test
    @DisplayName("Template Id 목록 중 하나라도 일치하는 TemplateTag 목록 조회 성공")
    void findAllByTemplateIdsInTest() {
        //given
        Template template1 = templateRepository.save(TemplateFixture.get(member, category));
        Template template2 = templateRepository.save(TemplateFixture.get(member, category));

        Tag tag1 = tagRepository.save(new Tag("tag1"));
        Tag tag2 = tagRepository.save(new Tag("tag2"));
        Tag tag3 = tagRepository.save(new Tag("tag3"));

        TemplateTag templateTag1 = templateTagRepository.save(new TemplateTag(template1, tag1));

        TemplateTag templateTag2 = templateTagRepository.save(new TemplateTag(template2, tag2));

        //when
        List<TemplateTag> templateTags = templateTagRepository.findAllByTemplateIdsIn(
                List.of(template1.getId(), template2.getId())
        );

        //then
        assertThat(templateTags).containsExactly(templateTag1, templateTag2)
                .doesNotContain(new TemplateTag(template1, tag3));
    }

    @Nested
    @DisplayName("템플릿 id 목록이 사용하는 모든 태그 목록을 조회")
    class FindDistinctByTemplateIn {

        @Test
        @DisplayName("태그 목록 조회 성공 : 정상적으로 태그 목록 조회")
        void testFindDistinctByTemplateIn() {
            // given
            Member otherMember = memberRepository.save(MemberFixture.getSecondMember());
            Template template1 = templateRepository.save(TemplateFixture.get(member, category));
            Template template2 = templateRepository.save(TemplateFixture.get(otherMember, category));
            Template template3 = templateRepository.save(TemplateFixture.get(member, category));

            Tag tag1 = tagRepository.save(new Tag("tag1"));
            Tag tag2 = tagRepository.save(new Tag("tag2"));
            Tag tag3 = tagRepository.save(new Tag("tag3"));

            templateTagRepository.save(new TemplateTag(template1, tag1));
            templateTagRepository.save(new TemplateTag(template2, tag2));
            templateTagRepository.save(new TemplateTag(template3, tag1));
            templateTagRepository.save(new TemplateTag(template3, tag3));

            // when
            List<Tag> result = templateTagRepository.findAllTagDistinctByMemberId(member.getId());

            // then
            assertThat(result).hasSize(2)
                    .containsExactly(tag1, tag3)
                    .doesNotContain(tag2);
        }

        @Test
        @DisplayName("태그 목록 조회 성공 : 존재하지 않는 멤버 id를 사용하면 예외가 터지지 않고 해당 id 값이 무시된다.")
        void notExistTemplateIdTest() {
            long notExistTemplateId = 100L;

            assertAll(
                    () -> assertThatThrownBy(() -> templateRepository.fetchById(notExistTemplateId))
                            .isInstanceOf(CodeZapException.class)
                            .hasMessageContaining("템플릿이 존재하지 않습니다."),
                    () -> assertThatCode(() ->
                            templateTagRepository.findAllTagDistinctByMemberId(notExistTemplateId))
                            .doesNotThrowAnyException(),
                    () -> assertThat(templateTagRepository.findAllTagDistinctByMemberId(notExistTemplateId))
                            .isEmpty()
            );
        }
    }

    @Nested
    @DisplayName("주어진 id 의 템플릿에서 사용하는 모든 태그 삭제")
    class deleteAllByTemplateId {

        @Test
        @DisplayName("태그 삭제 성공 : 주어진 id 의 템플릿에선 태그가 삭제되고, 나머지 템플릿의 태그에는 영향을 주지 않는다.")
        void successTest() {
            //given
            Template template1 = templateRepository.save(TemplateFixture.get(member, category));
            Template template2 = templateRepository.save(TemplateFixture.get(member, category));

            Tag tag1 = tagRepository.save(new Tag("tag1"));
            Tag tag2 = tagRepository.save(new Tag("tag2"));

            templateTagRepository.save(new TemplateTag(template1, tag1));
            templateTagRepository.save(new TemplateTag(template1, tag2));
            TemplateTag template2Tag1 = templateTagRepository.save(new TemplateTag(template2, tag1));
            TemplateTag template2Tag2 = templateTagRepository.save(new TemplateTag(template2, tag2));

            //when
            templateTagRepository.deleteAllByTemplateId(template1.getId());

            //then
            assertAll(
                    () -> assertThat(templateTagRepository.findAllByTemplate(template1)).isEmpty(),
                    () -> assertThat(templateTagRepository.findAllByTemplate(template2)).hasSize(2)
                            .containsExactly(template2Tag1, template2Tag2)
            );
        }

        @Test
        @DisplayName("태그 삭제 성공 : 존재하지 않는 id 를 사용해도 예외가 발생하지 않고 아무 영향이 없다.")
        void notExistIdTest() {
            //given
            Template template = templateRepository.save(TemplateFixture.get(member, category));

            Tag tag1 = tagRepository.save(new Tag("tag1"));
            Tag tag2 = tagRepository.save(new Tag("tag2"));

            TemplateTag template1Tag1 = templateTagRepository.save(new TemplateTag(template, tag1));
            TemplateTag template1Tag2 = templateTagRepository.save(new TemplateTag(template, tag2));

            //when

            //then
            assertAll(
                    () -> assertThatCode(() ->
                            templateTagRepository.deleteAllByTemplateId(template.getId() + 1))
                            .doesNotThrowAnyException(),
                    () -> assertThat(templateTagRepository.findAllByTemplate(template)).hasSize(2)
                            .containsExactly(template1Tag1, template1Tag2)
            );
        }
    }
}
