package codezap.tag.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Disabled;
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
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.tag.domain.Tag;
import codezap.tag.dto.response.FindAllTagsResponse;
import codezap.tag.dto.response.FindTagResponse;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;


@Transactional
class TagServiceTest extends ServiceTest {

    @Autowired
    private TagService sut;

    @Nested
    @DisplayName("템플릿에 대한 태그 생성")
    class CreateTags {

        @Test
        @DisplayName("성공: 템플릿에 해당하는 태그 생성")
        void createTags() {
            // given
            Template template = createSavedTemplate();
            List<String> tagNames = Arrays.asList("tag1", "tag2", "tag3");

            // when
            sut.createTags(template, tagNames);

            // then
            List<String> savedTemplateTagNames = getSavedTemplateTagNames(template);
            assertThat(savedTemplateTagNames).containsExactlyElementsOf(tagNames);
        }

        @Test
        @DisplayName("성공: 이미 있는 태그가 포함된 경우 중복 저장하지 않고 새 태그만 생성")
        void createTags_WhenExistTemplateTagContains() {
            // given
            Template template = createSavedTemplate();
            Tag existTag = tagRepository.save(new Tag("tag1"));
            List<String> tagNames = Arrays.asList(existTag.getName(), "tag2", "tag3");

            // when
            sut.createTags(template, tagNames);

            // then
            List<String> savedTemplateTagNames = getSavedTemplateTagNames(template);
            assertThat(savedTemplateTagNames).hasSize(3)
                    .containsExactlyElementsOf(tagNames);
        }

        @Test
        @DisplayName("성공: 이미 있는 태그이지만 이 템플릿의 태그가 아닌 경우 템플릿 태그만 추가")
        void createTags_WhenExistTagContains() {
            // given
            Template template = createSavedTemplate();
            Tag existTag = tagRepository.save(new Tag("tag1"));
            List<String> newTagNames = Arrays.asList("tag2", "tag3");
            List<String> tagNames = Arrays.asList(existTag.getName(), newTagNames.get(0), newTagNames.get(1));
            List<String> beforeTemplateTags = getSavedTemplateTagNames(template);

            // when
            sut.createTags(template, tagNames);

            // then
            List<String> afterTemplateTags = getSavedTemplateTagNames(template);
            assertAll(
                    () -> assertThat(beforeTemplateTags).doesNotContainAnyElementsOf(newTagNames),
                    () -> assertThat(afterTemplateTags).containsExactlyElementsOf(tagNames)
            );
        }

        private List<String> getSavedTemplateTagNames(Template template) {
            return templateTagRepository.findAllByTemplate(template).stream()
                    .map(templateTag -> templateTag.getTag().getName())
                    .toList();
        }
    }

    @Nested
    @DisplayName("템플릿으로 태그 조회")
    class FindAllByTemplate {

        @Test
        @DisplayName("성공: 템플릿에 해당하는 태그 목록 반환")
        void findAllByTemplate() {
            // given
            Template template = createSavedTemplate();
            Tag tag1 = tagRepository.save(new Tag("tag1"));
            Tag tag2 = tagRepository.save(new Tag("tag2"));
            TemplateTag templateTag1 = templateTagRepository.save(new TemplateTag(template, tag1));
            TemplateTag templateTag2 = templateTagRepository.save(new TemplateTag(template, tag2));

            // when & then
            assertThat(sut.findAllByTemplate(template))
                    .containsExactly(templateTag1.getTag(), templateTag2.getTag());
        }

        @Test
        @DisplayName("성공: 템플릿에 해당하는 태그가 없는 경우 빈 목록 반환")
        void findAllByTemplate_WhenNotExistTemplateTag() {
            // given
            Template template = createSavedTemplate();
            tagRepository.save(new Tag("tag1"));
            tagRepository.save(new Tag("tag2"));

            // when & then
            assertThat(sut.findAllByTemplate(template)).isEmpty();
        }

        @Test
        @Disabled("현재 InvalidDataAccessApiUsageException 발생하므로 조회 직전에 검증 처리가 필요")
        @DisplayName("실패: 존재하지 않는 템플릿으로 태그 조회")
        void findAllByTemplate_WhenNotExistTemplate() {
            // given
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
            Template unSavedTemplate = TemplateFixture.get(member, category);
            tagRepository.save(new Tag("tag1"));
            tagRepository.save(new Tag("tag2"));

            // when & then
            assertThatThrownBy(() -> sut.findAllByTemplate(unSavedTemplate))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("템플릿이 존재하지 않아 태그를 조회할 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("템플릿 Id로 태그 조회")
    class FindAllByTemplateId {

        @Test
        @DisplayName("성공: 템플릿 Id에 해당하는 태그 목록 반환")
        void findAllByTemplate() {
            // given
            Template template = createSavedTemplate();
            Tag tag1 = tagRepository.save(new Tag("tag1"));
            Tag tag2 = tagRepository.save(new Tag("tag2"));
            TemplateTag templateTag1 = templateTagRepository.save(new TemplateTag(template, tag1));
            TemplateTag templateTag2 = templateTagRepository.save(new TemplateTag(template, tag2));

            // when & then
            assertThat(sut.findAllByTemplateId(template.getId()))
                    .containsExactly(templateTag1.getTag(), templateTag2.getTag());
        }

        @Test
        @DisplayName("성공: 템플릿에 해당하는 태그가 없는 경우 빈 목록 반환")
        void findAllByTemplate_WhenNotExistTemplateTag() {
            // given
            Template template = createSavedTemplate();
            tagRepository.save(new Tag("tag1"));
            tagRepository.save(new Tag("tag2"));

            // when & then
            assertThat(sut.findAllByTemplateId(template.getId())).isEmpty();
        }

        @Test
        @Disabled("현재 InvalidDataAccessApiUsageException 발생하므로 조회 직전에 검증 처리가 필요")
        @DisplayName("실패: 존재하지 않는 템플릿으로 태그 조회")
        void findAllByTemplate_WhenNotExistTemplate() {
            // given
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
            Template unSavedTemplate = TemplateFixture.get(member, category);
            tagRepository.save(new Tag("tag1"));
            tagRepository.save(new Tag("tag2"));

            // when & then
            assertThatThrownBy(() -> sut.findAllByTemplateId(unSavedTemplate.getId()))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("템플릿이 존재하지 않아 태그를 조회할 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("사용자 ID로 모든 태그 조회")
    class FindAllByMemberId {

        @Test
        @DisplayName("사용자 ID로 모든 태그 조회 성공")
        void findAllByMemberId() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());

            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template1 = templateRepository.save(new Template(member, "title1", "description", category));
            var template2 = templateRepository.save(new Template(member, "title2", "description", category));
            var template3 = templateRepository.save(new Template(member, "title3", "description", category));
            var tag1 = tagRepository.save(new Tag("tag1"));
            var tag2 = tagRepository.save(new Tag("tag2"));
            var tag3 = tagRepository.save(new Tag("tag3"));
            templateTagRepository.save(new TemplateTag(template1, tag1));
            templateTagRepository.save(new TemplateTag(template1, tag2));
            templateTagRepository.save(new TemplateTag(template2, tag2));
            templateTagRepository.save(new TemplateTag(template2, tag3));
            templateTagRepository.save(new TemplateTag(template3, tag3));
            templateTagRepository.save(new TemplateTag(template3, tag1));

            // when
            var actual = sut.findAllByMemberId(member.getId());

            // then
            var expected = new FindAllTagsResponse(List.of(
                    FindTagResponse.from(tag1),
                    FindTagResponse.from(tag2),
                    FindTagResponse.from(tag3)));

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("성공: 템플릿 목록에 해당하는 모든 태그 반환")
        void findAllByTemplates() {
            // given
            Tag tag1 = tagRepository.save(new Tag("tag1"));
            Tag tag2 = tagRepository.save(new Tag("tag2"));

            Template template1 = createSavedTemplate();
            templateTagRepository.save(new TemplateTag(template1, tag1));

            Template template2 = createSecondTemplate();
            templateTagRepository.save(new TemplateTag(template2, tag2));

            // when & then
            List<Tag> actual = new ArrayList<>();
            actual.addAll(sut.findAllByTemplateId(template1.getId()));
            actual.addAll(sut.findAllByTemplateId(template2.getId()));
            assertThat(actual).isEqualTo(List.of(tag1, tag2));
        }

        @Test
        @DisplayName("성공: 템플릿들이 중복된 태그를 가지는 경우 중복 제거 후 반환")
        void findAllByTemplates_WhenDuplicatedTags() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());

            Tag tag1 = tagRepository.save(new Tag("tag1"));
            Tag tag2 = tagRepository.save(new Tag("tag2"));

            Template template1 = createSavedTemplate();
            templateTagRepository.save(new TemplateTag(template1, tag1));
            templateTagRepository.save(new TemplateTag(template1, tag2));

            Template template2 = createSecondTemplate();
            templateTagRepository.save(new TemplateTag(template2, tag2));

            // when & then
            FindAllTagsResponse actual = sut.findAllByMemberId(member.getId());
            assertThat(actual).isEqualTo(new FindAllTagsResponse(
                    List.of(FindTagResponse.from(tag1), FindTagResponse.from(tag2)))
            );
        }

        @Test
        @DisplayName("성공: 해당하는 태그가 없는 경우 빈 목록 반환")
        void findAllByTemplates_WhenNotExist() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            createSavedTemplate();

            // when & then
            FindAllTagsResponse actual = sut.findAllByMemberId(member.getId());
            assertThat(actual).isEqualTo(new FindAllTagsResponse(Collections.EMPTY_LIST));
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
            Template template = createSavedTemplate();
            List<String> newTags = List.of(tagName);

            // when
            sut.updateTags(template, newTags);

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
        @DisplayName("성공: 기존 태그 전체 삭제로 태그 업데이트 가능")
        void updateTags_WhenRemoveAllTags() {
            // given
            Template template = createSavedTemplate();
            List<Tag> tags = tagRepository.saveAll(List.of(new Tag("tag1"), new Tag("tag2")));
            templateTagRepository.saveAll(
                    List.of(new TemplateTag(template, tags.get(0)), new TemplateTag(template, tags.get(1))));

            List<String> updatedTags = List.of();

            // when
            sut.updateTags(template, updatedTags);

            // then
            assertThat(templateTagRepository.findAllByTemplate(template)).isEmpty();
        }

        @Test
        @DisplayName("성공: 동일한 태그가 이미 있는 경우 중복 저장하지 않음")
        void updateTags_Success() {
            // given
            Template template = createSavedTemplate();
            Tag tag1 = tagRepository.save(new Tag("tag1"));
            templateTagRepository.save(new TemplateTag(template, tag1));

            List<String> newTags = List.of(tag1.getName(), "tag2");

            // when
            sut.updateTags(template, newTags);

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
            Template template = createSavedTemplate();
            Tag tag1 = tagRepository.save(new Tag("tag1"));
            Tag tag2 = tagRepository.save(new Tag("tag2"));
            templateTagRepository.save(new TemplateTag(template, tag1));
            templateTagRepository.save(new TemplateTag(template, tag2));

            // when
            sut.deleteByIds(List.of(template.getId()));

            // then
            assertAll(
                    () -> assertThat(templateTagRepository.findAllByTemplate(template)).isEmpty(),
                    () -> assertThat(tagRepository.findByName(tag1.getName())).isPresent(),
                    () -> assertThat(tagRepository.findByName(tag2.getName())).isPresent()
            );
        }

        @Test
        @DisplayName("성공: 템플릿에 해당하는 태그가 없는 경우")
        void deleteByIds_WhenNotExistTemplateTag() {
            // given
            Template template = createSavedTemplate();

            // when
            sut.deleteByIds(List.of(template.getId()));

            // then
            assertThat(templateTagRepository.findAllByTemplate(template)).isEmpty();
        }
    }

    private Template createSavedTemplate() {
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
