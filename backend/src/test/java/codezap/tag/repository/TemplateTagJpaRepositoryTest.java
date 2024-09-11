package codezap.tag.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.global.repository.JpaRepositoryTest;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.tag.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.repository.TemplateRepository;

@JpaRepositoryTest
class TemplateTagJpaRepositoryTest {

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

        category = categoryRepository.save(CategoryFixture.getFirstCategory());
    }

    @Test
    @DisplayName("성공 : Template 을 이용한 TemplateTag 목록 조회")
    void findAllByTemplateTest() {
        //given
        Template template = templateRepository.save(createNthTemplate(member, category, 1));

        Tag tag1 = tagRepository.save(new Tag("tag1"));
        Tag tag2 = tagRepository.save(new Tag("tag2"));
        Tag tag3 = tagRepository.save(new Tag("tag3"));

        TemplateTag templateTag1 = templateTagRepository.save(new TemplateTag(template, tag1));
        TemplateTag templateTag2 = templateTagRepository.save(new TemplateTag(template, tag2));

        //when
        List<TemplateTag> templateTags = templateTagRepository.findAllByTemplate(template);

        //then
        assertThat(templateTags).containsExactly(templateTag1, templateTag2)
                .doesNotContain(new TemplateTag(template, tag3));
    }

    @Test
    @DisplayName("성공 : 템플릿 id 목록이 사용하는 모든 태그 목록을 조회")
    void testFindDistinctByTemplateIn() {
        // given
        Template template1 = templateRepository.save(createNthTemplate(member, category, 1));
        Template template2 = templateRepository.save(createNthTemplate(member, category, 2));
        Template template3 = templateRepository.save(createNthTemplate(member, category, 3));

        Tag tag1 = tagRepository.save(new Tag("tag1"));
        Tag tag2 = tagRepository.save(new Tag("tag2"));
        Tag tag3 = tagRepository.save(new Tag("tag3"));

        templateTagRepository.save(new TemplateTag(template1, tag1));
        templateTagRepository.save(new TemplateTag(template2, tag2));
        templateTagRepository.save(new TemplateTag(template3, tag1));
        templateTagRepository.save(new TemplateTag(template3, tag3));

        // when
        List<Long> result = templateTagRepository.findDistinctByTemplateIn(
                List.of(template1.getId(), template3.getId())
        );

        // then
        assertThat(result).hasSize(2)
                .containsExactly(tag1.getId(), tag3.getId())
                .doesNotContain(tag2.getId());
    }

    @Test
    @DisplayName("성공 : 선택된 태그들을 모두 사용하는 템플릿 목록 조회")
    void findAllTemplateIdInTagIds() {
        //given
        Template template1 = templateRepository.save(createNthTemplate(member, category, 1));
        Template template2 = templateRepository.save(createNthTemplate(member, category, 2));
        Template template3 = templateRepository.save(createNthTemplate(member, category, 3));

        Tag tag1 = tagRepository.save(new Tag("tag1"));
        Tag tag2 = tagRepository.save(new Tag("tag2"));
        Tag tag3 = tagRepository.save(new Tag("tag3"));

        templateTagRepository.save(new TemplateTag(template1, tag1));
        templateTagRepository.save(new TemplateTag(template3, tag1));

        templateTagRepository.save(new TemplateTag(template1, tag2));
        templateTagRepository.save(new TemplateTag(template2, tag2));

        templateTagRepository.save(new TemplateTag(template1, tag3));
        templateTagRepository.save(new TemplateTag(template2, tag3));
        templateTagRepository.save(new TemplateTag(template3, tag3));

        //when
        List<Long> allTemplateIdInTagIds = templateTagRepository.findAllTemplateIdInTagIds(
                List.of(tag2.getId(), tag3.getId()), 2);

        //then
        assertThat(allTemplateIdInTagIds)
                .hasSize(2)
                .containsExactly(template1.getId(), template2.getId())
                .doesNotContain(template3.getId());
    }

    @Test
    @DisplayName("성공 : 템플릿에 해당하는 모든 태그 삭제")
    void deleteAllByTemplateId() {
        //given
        Template template1 = templateRepository.save(createNthTemplate(member, category, 1));
        Template template2 = templateRepository.save(createNthTemplate(member, category, 2));

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

    private Template createNthTemplate(Member member, Category category, int n) {
        return new Template(member, "mockTitle" + n, "mockDescription" + n, category);
    }
}
