package codezap.tag.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
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

    private Member member1, member2;
    private Category category1, category2;
    private Tag tag1, tag2;

    @BeforeEach
    void setUp() {
        member1 = memberRepository.save(new Member("user1@test.com", "pp", "salt1"));
        member2 = memberRepository.save(new Member("user2@test.com", "pp", "salt2"));

        category1 = categoryRepository.save(new Category("Category 1", member1));
        category2 = categoryRepository.save(new Category("Category 2", member1));

        tag1 = tagRepository.save(new Tag("Tag 1"));
        tag2 = tagRepository.save(new Tag("Tag 2"));

        Template template1 = new Template(member1, "Template 1", "Description 1", category1);
        TemplateTag templateTag11 = new TemplateTag(template1, tag1);
        TemplateTag templateTag12 = new TemplateTag(template1, tag2);
        templateRepository.save(template1);
        templateTagRepository.save(templateTag11);
        templateTagRepository.save(templateTag12);

        Template template2 = new Template(member1, "Template 2", "Description 2", category2);
        TemplateTag templateTag21 = new TemplateTag(template2, tag1);
        TemplateTag templateTag22 = new TemplateTag(template2, tag2);
        templateRepository.save(template2);
        templateTagRepository.save(templateTag21);
        templateTagRepository.save(templateTag22);

        Template template3 = new Template(member2, "Another Template", "Another Description", category1);
        TemplateTag templateTag31 = new TemplateTag(template3, tag2);
        templateRepository.save(template3);
        templateTagRepository.save(templateTag31);
    }

    @Test
    @DisplayName("findAllByTemplateTest 조회 성공")
    void findAllByTemplateTest() {
        //given
        Template template = templateRepository.save(new Template(member1, "testTemplate", "testTemplate", category1));

        Tag tag1 = tagRepository.save(new Tag("tag1"));
        Tag tag2 = tagRepository.save(new Tag("tag2"));
        tagRepository.save(new Tag("tag3"));

        TemplateTag templateTag1 = templateTagRepository.save(new TemplateTag(template, tag1));
        TemplateTag templateTag2 = templateTagRepository.save(new TemplateTag(template, tag2));

        //when
        List<TemplateTag> templateTags = templateTagRepository.findAllByTemplate(template);

        //then
        assertThat(templateTags).containsExactly(templateTag1, templateTag2);
    }

    @Test
    void testFindDistinctByTemplateIn() {
        // given
        List<Long> templateIds = List.of(1L, 2L, 3L);

        // when
        List<Long> result = templateTagRepository.findDistinctByTemplateIn(templateIds);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.stream().distinct().count())
                .isEqualTo(result.size());
    }
}
