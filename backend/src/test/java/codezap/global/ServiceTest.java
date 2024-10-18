package codezap.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.repository.CategoryRepository;
import codezap.likes.repository.LikesRepository;
import codezap.member.repository.MemberRepository;
import codezap.tag.repository.TagRepository;
import codezap.tag.repository.TemplateTagRepository;
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
}
