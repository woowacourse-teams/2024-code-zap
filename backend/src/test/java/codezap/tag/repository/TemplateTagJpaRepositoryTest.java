package codezap.tag.repository;

import static org.assertj.core.api.Assertions.assertThat;

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
    @DisplayName("findAllByTemplateTest 조회 성공")
    void findAllByTemplateTest() {
        //given
        Template template = templateRepository.save(
                new Template(member, "testTemplate", "testTemplate", category));

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
    @DisplayName("findDistinctByTemplateIn 조회 테스트")
    void testFindDistinctByTemplateIn() {
        // given
        Template template1 = templateRepository.save(
                new Template(member, "testTemplate1", "testTemplate1", category));
        Template template2 = templateRepository.save(
                new Template(member, "testTemplate2", "testTemplate2", category));
        Template template3 = templateRepository.save(
                new Template(member, "testTemplate3", "testTemplate3", category));

        Tag tag1 = tagRepository.save(new Tag("tag1"));
        Tag tag2 = tagRepository.save(new Tag("tag2"));
        Tag tag3 = tagRepository.save(new Tag("tag3"));

        templateTagRepository.save(new TemplateTag(template1, tag1));
        templateTagRepository.save(new TemplateTag(template3, tag1));
        templateTagRepository.save(new TemplateTag(template3, tag3));

        // when
        List<Long> result = templateTagRepository.findDistinctByTemplateIn(
                List.of(template1.getId(), template3.getId())
        );

        // then
        assertThat(result).hasSize(2)
                .containsExactly(tag1.getId(), tag3.getId());
    }

    @Test
    @DisplayName("findAllTemplateIdInTagIds 조회 테스트")
    void findAllTemplateIdInTagIds() {
        //given
        Template template1 = templateRepository.save(
                new Template(member, "title1", "description1", category));
        Template template2 = templateRepository.save(
                new Template(member, "title2", "description2", category));
        Template template3 = templateRepository.save(
                new Template(member, "title3", "description3", category));

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
        assertThat(allTemplateIdInTagIds).containsExactly(template1.getId(), template2.getId())
                .hasSize(2);
    }

    @Test
    @DisplayName("deleteAllByTemplateId 조회 테스트")
    void deleteAllByTemplateId() {
        //given
        Template template = templateRepository.save(
                new Template(member, "title1", "description1", category));

        Tag tag1 = tagRepository.save(new Tag("tag1"));
        Tag tag2 = tagRepository.save(new Tag("tag2"));

        templateTagRepository.save(new TemplateTag(template, tag1));
        templateTagRepository.save(new TemplateTag(template, tag2));


        //when
        templateTagRepository.deleteAllByTemplateId(template.getId());
        List<TemplateTag> templateTags = templateTagRepository.findAllByTemplate(template);

        //then
        assertThat(templateTags).isEmpty();
    }
}
