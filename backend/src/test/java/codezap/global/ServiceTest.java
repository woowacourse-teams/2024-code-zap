package codezap.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.likes.repository.LikesRepository;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.tag.repository.TagRepository;
import codezap.tag.repository.TemplateTagRepository;
import codezap.template.domain.Template;
import codezap.template.repository.SourceCodeRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.ThumbnailRepository;

@SpringBootTest
@DatabaseIsolation
@Transactional
public class ServiceTest {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected TemplateRepository templateRepository;

    @Autowired
    protected TagRepository tagRepository;

    @Autowired
    protected TemplateTagRepository templateTagRepository;

    @Autowired
    protected SourceCodeRepository sourceCodeRepository;

    @Autowired
    protected ThumbnailRepository thumbnailRepository;

    @Autowired
    protected LikesRepository likesRepository;

    protected Template createSavedTemplate() {
        Member member = memberRepository.save(MemberFixture.getFirstMember());
        Category category = categoryRepository.save(CategoryFixture.getDefaultCategory(member));
        return templateRepository.save(TemplateFixture.get(member, category));
    }

    protected Template createSavedSecondTemplate() {
        Member member = memberRepository.save(MemberFixture.getSecondMember());
        Category category = categoryRepository.save(CategoryFixture.getCategory(member));
        return templateRepository.save(TemplateFixture.get(member, category));
    }
}
