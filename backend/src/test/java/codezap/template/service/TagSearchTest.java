package codezap.template.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.category.repository.FakeCategoryRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberDtoFixture;
import codezap.fixture.MemberFixture;
import codezap.member.domain.Member;
import codezap.member.repository.FakeMemberRepository;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.repository.FakeSourceCodeRepository;
import codezap.template.repository.FakeTagRepository;
import codezap.template.repository.FakeTemplateRepository;
import codezap.template.repository.FakeTemplateTagRepository;
import codezap.template.repository.FakeThumbnailRepository;
import codezap.template.repository.SourceCodeRepository;
import codezap.template.repository.TagRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.TemplateTagRepository;
import codezap.template.repository.ThumbnailRepository;

public class TagSearchTest {

    private Member firstMember = new Member(1L, "name1", "password1234");
    private Member secondMember = new Member(2L, "name2", "password1234");
    private Category firstCategory = new Category(1L, firstMember, "카테고리 없음", true);

    private final TemplateRepository templateRepository = new FakeTemplateRepository();
    private final SourceCodeRepository sourceCodeRepository = new FakeSourceCodeRepository();
    private final ThumbnailRepository thumbnailRepository = new FakeThumbnailRepository();
    private final CategoryRepository categoryRepository = new FakeCategoryRepository(
            List.of(CategoryFixture.getFirstCategory()));
    private final TemplateTagRepository templateTagRepository = new FakeTemplateTagRepository();
    private final TagRepository tagRepository = new FakeTagRepository();
    private final MemberRepository memberRepository = new FakeMemberRepository(List.of(
            MemberFixture.getFirstMember(), MemberFixture.getSecondMember()
    ));

    private final TemplateService templateService = new TemplateService(
            thumbnailRepository,
            templateRepository,
            sourceCodeRepository,
            categoryRepository,
            tagRepository,
            templateTagRepository,
            memberRepository);

    private void saveDefault15Templates(Member member, Category category) {
        saveTemplate(makeTemplateRequest("hello keyword 1"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 2"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 3"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 4"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 5"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 6"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 7"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 8"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 9"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 10"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 11"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 12"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 13"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 14"), member, category);
        saveTemplate(makeTemplateRequest("hello keyword 15"), member, category);
    }

    private CreateTemplateRequest makeTemplateRequest(String title) {
        return new CreateTemplateRequest(title, "description",
                List.of(new CreateSourceCodeRequest("filename1", "content1", 1),
                        new CreateSourceCodeRequest("filename2", "content2", 2)),
                1,
                1L,
                List.of());
    }

    private Template saveTemplate(CreateTemplateRequest createTemplateRequest, Member member, Category category) {
        Template savedTemplate = templateRepository.save(
                new Template(member, createTemplateRequest.title(), createTemplateRequest.description(), category));
        SourceCode savedFirstSourceCode = sourceCodeRepository.save(
                new SourceCode(savedTemplate, "filename1", "content1", 1));
        sourceCodeRepository.save(new SourceCode(savedTemplate, "filename2", "content2", 2));
        thumbnailRepository.save(new Thumbnail(savedTemplate, savedFirstSourceCode));

        return savedTemplate;
    }

    @Nested
    @DisplayName("조건에 따른 페이지 조회 메서드 동작 확인")
    class FilteringPageTest {

        @Test
        @DisplayName("사용자별 태그 목록 조회 성공")
        void findBySingleTagPageSuccess() {
            Member member1 = memberRepository.fetchById(MemberDtoFixture.getFirstMemberDto().id());
            Member member2 = memberRepository.fetchById(MemberDtoFixture.getSecondMemberDto().id());
            Category category1 = categoryRepository.save(new Category("category1", member1));
            tagRepository.save(new Tag("tag1"));
            tagRepository.save(new Tag("tag2"));
            saveDefault15Templates(member1, category1);
            saveDefault15Templates(member2, category1);

            templateTagRepository.save(
                    new TemplateTag(templateRepository.fetchById(1L), tagRepository.fetchById(1L)));
            templateTagRepository.save(
                    new TemplateTag(templateRepository.fetchById(2L), tagRepository.fetchById(2L)));
            templateTagRepository.save(
                    new TemplateTag(templateRepository.fetchById(30L), tagRepository.fetchById(1L)));

            assertAll(
                    () -> assertThat(templateService.findAllTagsByMemberId(member1.getId()).tags()).hasSize(2),
                    () -> assertThat(templateService.findAllTagsByMemberId(member2.getId()).tags()).hasSize(1)
            );
        }
    }
}
