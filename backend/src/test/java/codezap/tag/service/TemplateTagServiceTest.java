package codezap.tag.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.ServiceTest;
import codezap.member.domain.Member;
import codezap.tag.domain.Tag;
import codezap.tag.dto.response.FindAllTagsResponse;
import codezap.tag.dto.response.FindTagResponse;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;


@Transactional
class TemplateTagServiceTest extends ServiceTest {

    @Autowired
    private TemplateTagService templateTagService;

    @Nested
    @DisplayName("템플릿에 대한 태그 생성")
    class CreateTags {

        @Test
        @DisplayName("성공: 템플릿에 해당하는 태그 생성")
        void createTags() {
            // given
            Template template = createTemplate();
            List<String> tagNames = Arrays.asList("tag1", "tag2", "tag3");

            // when
            templateTagService.createTags(template, tagNames);

            // then
            List<String> savedTemplateTagNames = getSavedTagNames(template);
            assertThat(savedTemplateTagNames).containsExactlyElementsOf(tagNames);
        }

        @Test
        @DisplayName("성공: 이미 있는 템플릿 태그가 포함된 경우 중복 저장하지 않고 새 태그만 생성")
        void createTags_WhenExistTemplateTagContains() {
            // given
            Template template = createTemplate();
            Tag existTag = tagRepository.save(new Tag("tag1"));
            TemplateTag existTemplateTag = new TemplateTag(template, existTag);
            List<String> tagNames = Arrays.asList(existTag.getName(), "tag2", "tag3");

            // when
            templateTagService.createTags(template, tagNames);

            // then
            List<String> savedTemplateTagNames = getSavedTagNames(template);
            assertThat(savedTemplateTagNames).hasSize(3)
                    .containsExactlyElementsOf(tagNames);
        }

        @Test
        @DisplayName("성공: 이미 있는 태그이지만 이 템플릿의 태그가 아닌 경우 템플릿 태그만 추가")
        void createTags_WhenExistTagContains() {
            // given
            Template template = createTemplate();
            Tag existTag = tagRepository.save(new Tag("tag1"));
            List<String> tagNames = Arrays.asList(existTag.getName(), "tag2", "tag3");
            List<String> beforeTemplateTags = getSavedTagNames(template);

            // when
            templateTagService.createTags(template, tagNames);

            // then
            List<String> afterTemplateTags = getSavedTagNames(template);
            assertAll(
                    () -> assertThat(beforeTemplateTags).doesNotContain(existTag.getName()),
                    () -> assertThat(afterTemplateTags).containsExactlyElementsOf(tagNames)
            );
        }

        private List<String> getSavedTagNames(Template template) {
            return templateTagRepository.findAllByTemplate(template).stream()
                    .map(templateTag -> templateTag.getTag().getName())
                    .toList();
        }
    }

    @Nested
    @DisplayName("템플릿으로 태그 조회")
    class GetByTemplate {

        @Test
        @DisplayName("성공: 템플릿에 해당하는 태그 목록 반환")
        void getByTemplate() {
            // given
            Template template = createTemplate();
            Tag tag1 = tagRepository.save(new Tag("tag1"));
            Tag tag2 = tagRepository.save(new Tag("tag2"));
            TemplateTag templateTag1 = templateTagRepository.save(new TemplateTag(template, tag1));
            TemplateTag templateTag2 = templateTagRepository.save(new TemplateTag(template, tag2));

            // when & then
            assertThat(templateTagService.getByTemplate(template))
                    .containsExactly(templateTag1.getTag(), templateTag2.getTag());
        }

        @Test
        @DisplayName("성공: 템플릿에 해당하는 태그가 없는 경우 빈 목록 반환")
        void getByTemplate_WhenNotExistTemplateTag() {
            // given
            Template template = createTemplate();
            tagRepository.save(new Tag("tag1"));
            tagRepository.save(new Tag("tag2"));

            // when & then
            assertThat(templateTagService.getByTemplate(template)).isEmpty();
        }
    }

    @Nested
    @DisplayName("여러 템플릿으로 모든 태그 조회")
    class FindAllByTemplates {

        @Test
        @DisplayName("성공: 템플릿 목록에 해당하는 모든 태그 반환")
        void findAllByTemplates() {
            // given
            Tag tag1 = tagRepository.save(new Tag("tag1"));
            Tag tag2 = tagRepository.save(new Tag("tag2"));

            Template template1 = createTemplate();
            templateTagRepository.save(new TemplateTag(template1, tag1));

            Template template2 = createSecondTemplate();
            templateTagRepository.save(new TemplateTag(template2, tag2));

            // when & then
            FindAllTagsResponse actual = templateTagService.findAllByTemplates(List.of(template1, template2));
            assertThat(actual).isEqualTo(new FindAllTagsResponse(
                    List.of(FindTagResponse.from(tag1), FindTagResponse.from(tag2)))
            );
        }

        @Test
        @DisplayName("성공: 템플릿들이 중복된 태그를 가지는 경우 중복 제거 후 반환")
        void findAllByTemplates_WhenDuplicatedTags() {
            // given
            Tag tag1 = tagRepository.save(new Tag("tag1"));
            Tag tag2 = tagRepository.save(new Tag("tag2"));

            Template template1 = createTemplate();
            templateTagRepository.save(new TemplateTag(template1, tag1));
            templateTagRepository.save(new TemplateTag(template1, tag2));

            Template template2 = createSecondTemplate();
            templateTagRepository.save(new TemplateTag(template2, tag2));

            // when & then
            FindAllTagsResponse actual = templateTagService.findAllByTemplates(List.of(template1, template2));
            assertThat(actual).isEqualTo(new FindAllTagsResponse(
                    List.of(FindTagResponse.from(tag1), FindTagResponse.from(tag2)))
            );
        }
    }

    @Nested
    @DisplayName("태그 업데이트")
    class UpdateTags {

        @Test
        @DisplayName("성공: 새 태그 생성")
        void updateTags() {
            // given
            String tagName = "tag1";
            Template template = createTemplate();
            List<String> newTags = List.of(tagName);

            // when
            templateTagService.updateTags(template, newTags);

            // then
            List<String> saveTemplateTags = templateTagRepository.findAllByTemplate(template).stream()
                    .map(templateTag -> templateTag.getTag().getName())
                    .toList();
            assertAll(
                    () -> assertThat(tagRepository.findByName(tagName)).isPresent(),
                    () -> assertThat(saveTemplateTags).containsExactlyElementsOf(newTags)
            );
        }

        @Test
        @DisplayName("성공: 동일한 태그가 이미 있는 경우 중복 저장하지 않음")
        void updateTags_Success() {
            // given
            Template template = createTemplate();
            Tag tag1 = tagRepository.save(new Tag("tag1"));
            templateTagRepository.save(new TemplateTag(template, tag1));

            List<String> newTags = List.of(tag1.getName(), "tag2");

            // when
            templateTagService.updateTags(template, newTags);

            // then
            List<String> saveTemplateTags = templateTagRepository.findAllByTemplate(template).stream()
                    .map(templateTag -> templateTag.getTag().getName())
                    .toList();
            assertThat(saveTemplateTags).hasSize(2)
                    .containsExactlyElementsOf(newTags);
        }
    }

    @Nested
    @DisplayName("템플릿에 해당하는 태그 전체 삭제")
    class DeleteByIds {

        @Test
        @DisplayName("성공")
        void deleteByIds() {
            // given
            Template template = createTemplate();
            Tag tag1 = tagRepository.save(new Tag("tag1"));
            Tag tag2 = tagRepository.save(new Tag("tag2"));
            templateTagRepository.save(new TemplateTag(template, tag1));
            templateTagRepository.save(new TemplateTag(template, tag2));

            // when
            templateTagService.deleteByIds(List.of(template.getId()));

            // then
            assertThat(templateTagRepository.findAllByTemplate(template)).isEmpty();
        }

        @Test
        @DisplayName("성공: 템플릿에 해당하는 태그가 없는 경우")
        void deleteByIds_WhenNotExistTemplateTag() {
            // given
            Template template = createTemplate();

            // when
            templateTagService.deleteByIds(List.of(template.getId()));

            // then
            assertThat(templateTagRepository.findAllByTemplate(template)).isEmpty();
        }
    }

    private Template createTemplate() {
        Member member = memberRepository.save(MemberFixture.getFirstMember());
        Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
        return templateRepository.save(TemplateFixture.get(member, category));
    }

    private Template createSecondTemplate() {
        Member member = memberRepository.save(MemberFixture.getSecondMember());
        Category category = categoryRepository.save(CategoryFixture.getSecondCategory());
        return templateRepository.save(TemplateFixture.get(member, category));
    }
}

