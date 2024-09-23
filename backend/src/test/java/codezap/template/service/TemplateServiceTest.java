package codezap.template.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.DatabaseIsolation;
import codezap.global.exception.CodeZapException;
import codezap.like.domain.Likes;
import codezap.like.repository.LikesRepository;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.tag.domain.Tag;
import codezap.tag.repository.TagRepository;
import codezap.tag.repository.TemplateTagRepository;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.repository.TemplateRepository;

@SpringBootTest
@DatabaseIsolation
@Transactional
class TemplateServiceTest {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TemplateTagRepository templateTagRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private TemplateService sut;

    @Nested
    @DisplayName("템플릿 생성")
    class CreateTemplate {

        @Test
        @DisplayName("템플릿 생성 성공")
        void createTemplateSuccess() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var templateRequest = new CreateTemplateRequest(
                    "title",
                    "description",
                    List.of(
                            new CreateSourceCodeRequest("filename1", "content1", 1),
                            new CreateSourceCodeRequest("filename2", "content2", 2)
                    ),
                    1,
                    category.getId(),
                    List.of("tag1", "tag2")
            );

            var actual = sut.createTemplate(member, templateRequest, category);

            assertAll(
                    () -> assertThat(actual.getTitle()).isEqualTo(templateRequest.title()),
                    () -> assertThat(actual.getMember()).isEqualTo(member),
                    () -> assertThat(actual.getCategory()).isEqualTo(category),
                    () -> assertThat(actual.getDescription()).isEqualTo(templateRequest.description())
            );

        }
    }

    @Nested
    @DisplayName("ID로 템플릿 단건 조회")
    class GetById {

        @Test
        @DisplayName("템플릿 단건 조회 성공")
        void getByIdSuccess() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var template = templateRepository.save(TemplateFixture.get(member, category));

            var actual = sut.getById(template.getId());

            assertAll(
                    () -> assertThat(actual.getTitle()).isEqualTo(template.getTitle()),
                    () -> assertThat(actual.getMember()).isEqualTo(member),
                    () -> assertThat(actual.getCategory()).isEqualTo(category),
                    () -> assertThat(actual.getDescription()).isEqualTo(template.getDescription())
            );
        }

        @Test
        @DisplayName("템플릿 단건 조회 실패: 해당하는 ID의 템플릿이 없는 경우")
        void getByIdFailWithWrongId() {
            var nonExistentID = 100L;

            assertThatThrownBy(() -> sut.getById(nonExistentID))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + nonExistentID + "에 해당하는 템플릿이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("멤버 ID로 템플릿 조회")
    class GetByMemberId {

        @Test
        @DisplayName("멤버 id로 템플릿 조회 성공")
        void getByMemberIdSuccess() {
            var member1 = memberRepository.save(MemberFixture.getFirstMember());
            var member2 = memberRepository.save(MemberFixture.getSecondMember());
            var category1 = categoryRepository.save(CategoryFixture.getFirstCategory());
            var category2 = categoryRepository.save(CategoryFixture.getSecondCategory());
            var template1 = templateRepository.save(TemplateFixture.get(member1, category1));
            var template2 = templateRepository.save(TemplateFixture.get(member1, category1));
            var template3 = templateRepository.save(TemplateFixture.get(member2, category2));

            var actual = sut.getByMemberId(member1.getId());

            assertThat(actual).hasSize(2)
                    .containsExactly(template1, template2);
        }

        @Test
        @DisplayName("멤버 ID로 템플릿 조회 성공: 해당하는 템플릿이 없는 경우 빈 목록 반환")
        void getByMemberIdSuccessWithEmptyList() {
            var memberId = 100L;

            var actual = sut.getByMemberId(memberId);

            assertThat(actual).isEmpty();
        }
    }

    @Nested
    @DisplayName("검색 기능")
    class FindAll {

        private Member member1, member2;
        private Category category1, category2;
        private Tag tag1, tag2;
        private Template template1, template2, template3;

        @Test
        @DisplayName("검색 기능: 회원 ID로 템플릿 목록 조회 성공")
        void findAllSuccessByMemberId() {
            saveInitialData();
            Long memberId = member1.getId();
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = null;
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAll(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent()).hasSize(2),
                    () -> assertThat(actual.getContent())
                            .allMatch(template -> template.getMember().getId().equals(member1.getId()))
            );
        }

        @Test
        @Disabled("Pageable에 대한 null 검증이 필요함")
        @DisplayName("검색 기능 실패: Pageable을 전달하지 않은 경우")
        void findAllFailureWithNullPageable() {
            saveInitialData();
            Long memberId = member1.getId();
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = null;
            Pageable pageable = null;

            assertThatThrownBy(() -> sut.findAll(memberId, keyword, categoryId, tagIds, pageable))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("Pageable을 필수로 작성해야 합니다.");
        }

        @Test
        @DisplayName("검색 기능: 키워드로 템플릿 목록 조회 성공")
        void findAllSuccessByKeyword() {
            saveInitialData();
            Long memberId = null;
            String keyword = "Template";
            Long categoryId = null;
            List<Long> tagIds = null;
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAll(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent())
                            .allMatch(template ->
                                    template.getTitle().contains(keyword) || template.getDescription()
                                            .contains(keyword)),
                    () -> assertThat(actual.getContent()).hasSize(3)
            );
        }

        @Test
        @DisplayName("검색 기능: 카테고리 ID로 템플릿 목록 조회 성공")
        void findAllSuccessByCategoryId() {
            saveInitialData();
            Long memberId = null;
            String keyword = null;
            Long categoryId = category1.getId();
            List<Long> tagIds = null;
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAll(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent()).hasSize(2),
                    () -> assertThat(actual.getContent())
                            .allMatch(template -> template.getCategory().getId().equals(category1.getId()))
            );
        }

        @Test
        @DisplayName("검색 기능: 태그 ID 목록으로 템플릿 목록 조회, 모든 태그를 가진 템플릿만 조회 성공")
        void findAllSuccessByTagIds() {
            saveInitialData();
            Long memberId = null;
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = List.of(tag1.getId(), tag2.getId());
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAll(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent())
                            .containsExactlyInAnyOrder(
                                    templateRepository.fetchById(1L),
                                    templateRepository.fetchById(2L)),
                    () -> assertThat(actual.getContent()).hasSize(2)
            );
        }

        @Test
        @DisplayName("검색 기능: 단일 태그 ID로 템플릿 목록 조회 성공")
        void findAllSuccessBySingleTagId() {
            saveInitialData();
            Long memberId = null;
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = List.of(tag2.getId());
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAll(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent()).containsExactlyInAnyOrder(
                            templateRepository.fetchById(1L),
                            templateRepository.fetchById(2L),
                            templateRepository.fetchById(3L)),
                    () -> assertThat(actual.getContent()).hasSize(3)
            );
        }

        @Test
        @DisplayName("검색 기능: 회원 ID와 키워드로 템플릿 목록 조회 성공")
        void findAllSuccessByMemberIdAndKeyword() {
            saveInitialData();
            Long memberId = member1.getId();
            String keyword = "Template";
            Long categoryId = null;
            List<Long> tagIds = null;
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAll(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent()).hasSize(2),
                    () -> assertThat(actual.getContent())
                            .allMatch(template -> template.getMember().getId().equals(member1.getId())
                                    && (template.getTitle().contains(keyword)
                                    || template.getDescription().contains(keyword)))
            );
        }

        @Test
        @DisplayName("검색 기능: 회원 ID와 카테고리 ID로 템플릿 목록 조회 성공")
        void findAllSuccessByMemberIdAndCategoryId() {
            saveInitialData();
            Long memberId = member1.getId();
            String keyword = null;
            Long categoryId = category1.getId();
            List<Long> tagIds = null;
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAll(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent()).hasSize(1),
                    () -> assertThat(actual.getContent().get(0).getMember().getId()).isEqualTo(member1.getId()),
                    () -> assertThat(actual.getContent().get(0).getCategory().getId()).isEqualTo(category1.getId())
            );
        }

        @Test
        @DisplayName("검색 기능: 회원 ID와 태그 ID 목록으로 템플릿 목록 조회 성공")
        void findAllSuccessByMemberIdAndTagIds() {
            saveInitialData();
            Long memberId = member1.getId();
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = List.of(tag1.getId(), tag2.getId());
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAll(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent()).hasSize(2),
                    () -> assertThat(actual.getContent())
                            .containsExactlyInAnyOrder(
                                    templateRepository.fetchById(1L),
                                    templateRepository.fetchById(2L))
            );
        }

        @Test
        @DisplayName("검색 기능: 모든 검색 기준으로 템플릿 목록 조회 성공")
        void findAllSuccessWithAllCriteria() {
            saveInitialData();
            Long memberId = member1.getId();
            String keyword = "Template";
            Long categoryId = category1.getId();
            List<Long> tagIds = List.of(tag1.getId(), tag2.getId());
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAll(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent()).hasSize(1),
                    () -> assertThat(actual.getContent()).containsExactlyInAnyOrder(templateRepository.fetchById(1L))
            );
        }

        @Test
        @DisplayName("검색 기능: 검색 결과가 없는 경우 빈 리스트 반환 성공")
        void findAllSuccessWithNoResults() {
            saveInitialData();
            Long memberId = null;
            String keyword = "NonExistentKeyword";
            Long categoryId = null;
            List<Long> tagIds = null;
            Pageable pageable = PageRequest.of(0, 10);

            Page<Template> actual = sut.findAll(memberId, keyword, categoryId, tagIds, pageable);

            assertThat(actual.getContent()).isEmpty();
        }

        @Test
        @DisplayName("검색 기능: 두 번째 페이지 조회 성공")
        void findAllSuccessWithSecondPage() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            for (int i = 0; i < 15; i++) {
                templateRepository.save(TemplateFixture.get(member, category));
            }
            Long memberId = member.getId();
            String keyword = null;
            Long categoryId = null;
            List<Long> tagIds = null;
            Pageable pageable = PageRequest.of(1, 10);

            Page<Template> actual = sut.findAll(memberId, keyword, categoryId, tagIds, pageable);

            assertAll(
                    () -> assertThat(actual.getContent()).hasSize(5),
                    () -> assertThat(actual.getContent().get(0).getId()).isEqualTo(11L)
            );
        }

        @Nested
        @DisplayName("정렬 기능 테스트")
        class SortTest {

            @Test
            @DisplayName("좋아요 순 정렬 테스트")
            void sortByLikesCount() {
                saveMembers10();
                Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
                saveTemplates5(category);
                likeTemplate(3L, 10L);
                likeTemplate(5L, 7L);
                likeTemplate(2L, 5L);
                likeTemplate(4L, 1L);
                likeTemplate(1L, 0L);

                List<Template> templates = sut.findAll(null, "", null, null,
                                PageRequest.of(0, 10, Sort.by(Direction.DESC, "likesCount")))
                        .getContent();

                assertThat(templates).containsExactly(
                        templateRepository.fetchById(3L),
                        templateRepository.fetchById(5L),
                        templateRepository.fetchById(2L),
                        templateRepository.fetchById(4L),
                        templateRepository.fetchById(1L)
                );
            }

            private void saveMembers10() {
                for (int i = 0; i < 10; i++) {
                    memberRepository.save(new Member("name" + i, "password" + 1, "salt"));
                }
            }

            private void saveTemplates5(Category category) {
                for (int i = 0; i < 5; i++) {
                    templateRepository.save(new Template(
                            memberRepository.fetchById(1L),
                            "title" + i,
                            "description",
                            category
                    ));
                }
            }

            private void likeTemplate(long templateId, long likeCount) {
                for (long memberId = 1L; memberId <= likeCount; memberId++) {
                    likesRepository.save(new Likes(
                            null,
                            templateRepository.fetchById(templateId),
                            memberRepository.fetchById(memberId)
                    ));
                }
            }
        }

        private void saveInitialData() {

            saveTwoMembers();
            saveTwoCategory();
            saveTwoTags();
            saveThreeTemplates();
            saveTemplateTags();
        }

        private void saveTwoMembers() {
            member1 = memberRepository.save(MemberFixture.getFirstMember());
            member2 = memberRepository.save(MemberFixture.getSecondMember());
        }

        private void saveTwoCategory() {
            category1 = categoryRepository.save(new Category("Category 1", member1));
            category2 = categoryRepository.save(new Category("Category 2", member1));
        }

        private void saveTwoTags() {
            tag1 = tagRepository.save(new Tag("Tag 1"));
            tag2 = tagRepository.save(new Tag("Tag 2"));
        }

        private void saveThreeTemplates() {
            template1 = templateRepository.save(TemplateFixture.get(member1, category1));
            template2 = templateRepository.save(TemplateFixture.get(member1, category2));
            template3 = templateRepository.save(TemplateFixture.get(member2, category1));
        }

        private void saveTemplateTags() {
            templateTagRepository.save(new TemplateTag(template1, tag1));
            templateTagRepository.save(new TemplateTag(template1, tag2));

            templateTagRepository.save(new TemplateTag(template2, tag1));
            templateTagRepository.save(new TemplateTag(template2, tag2));

            templateTagRepository.save(new TemplateTag(template3, tag2));
        }
    }

    @Nested
    @DisplayName("템플릿 수정")
    class UpdateTemplate {

        @Test
        @DisplayName("템플릿 수정 성공")
        void updateTemplateSuccess() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var template = templateRepository.save(TemplateFixture.get(member, category));
            var updateTemplateRequest = new UpdateTemplateRequest(
                    "Update Title",
                    "Update Description",
                    List.of(),
                    List.of(),
                    List.of(),
                    category.getId(),
                    List.of()
            );

            sut.updateTemplate(member, template.getId(), updateTemplateRequest, category);

            assertAll(
                    () -> assertThat(template.getTitle()).isEqualTo(updateTemplateRequest.title()),
                    () -> assertThat(template.getDescription()).isEqualTo(updateTemplateRequest.description())
            );
        }

        @Test
        @DisplayName("템플릿 수정 실패: 해당하는 ID의 템플릿이 존재하지 않는 경우")
        void updateTemplateFailWithWrongID() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var updateTemplateRequest = new UpdateTemplateRequest(
                    "Update Title",
                    "Update Description",
                    List.of(),
                    List.of(),
                    List.of(),
                    category.getId(),
                    List.of()
            );
            var nonExistentID = 100L;

            assertThatThrownBy(() -> sut.updateTemplate(member, nonExistentID, updateTemplateRequest, category))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + nonExistentID + "에 해당하는 템플릿이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("템플릿 수정 실패: 템플릿을 수정할 권한이 없는 경우")
        void updateTemplateFailWithWrongMember() {
            var ownerMember = memberRepository.save(MemberFixture.getFirstMember());
            var otherMember = memberRepository.save(MemberFixture.getSecondMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var template = templateRepository.save(TemplateFixture.get(ownerMember, category));
            var updateTemplateRequest = new UpdateTemplateRequest(
                    "Update Title",
                    "Update Description",
                    List.of(),
                    List.of(),
                    List.of(),
                    category.getId(),
                    List.of()
            );

            assertThatThrownBy(() -> sut.updateTemplate(otherMember, template.getId(), updateTemplateRequest, category))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 템플릿에 대한 권한이 없습니다.");
        }
    }

    @Nested
    @DisplayName("템플릿 삭제")
    class DeleteTemplate {

        @Test
        @DisplayName("템플릿 삭제 성공: 1개 삭제")
        void deleteTemplateSuccessWithOneTemplate() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var template1 = templateRepository.save(TemplateFixture.get(member, category));
            var template2 = templateRepository.save(TemplateFixture.get(member, category));

            sut.deleteByMemberAndIds(member, List.of(template1.getId()));
            Page<Template> actual = sut.findAll(member.getId(), null, null, null, PageRequest.of(0, 10));

            assertThat(actual.getContent()).hasSize(1)
                    .containsExactly(template2);
        }

        @Test
        @DisplayName("템플릿 삭제 성공: 여러개 삭제")
        void deleteTemplateSuccessWithMultipleTemplate() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var template1 = templateRepository.save(TemplateFixture.get(member, category));
            var template2 = templateRepository.save(TemplateFixture.get(member, category));
            var template3 = templateRepository.save(TemplateFixture.get(member, category));
            var template4 = templateRepository.save(TemplateFixture.get(member, category));

            sut.deleteByMemberAndIds(member, List.of(template1.getId(), template4.getId()));
            Page<Template> actual = sut.findAll(member.getId(), null, null, null, PageRequest.of(0, 10));

            assertThat(actual.getContent()).hasSize(2)
                    .containsExactly(template2, template3);
        }

        @Test
        @DisplayName("템플릿 삭제 실패: 존재하지 않는 템플릿 삭제")
        void deleteTemplateSuccessWithNonExistentTemplate() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var template = templateRepository.save(TemplateFixture.get(member, category));
            Long nonExistentID = 100L;

            assertThatThrownBy(() -> sut.deleteByMemberAndIds(member, List.of(nonExistentID)))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + nonExistentID + "에 해당하는 템플릿이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("템플릿 삭제 실패: 삭제할 권한이 없는 경우")
        void deleteTemplateFailWithWrongMember() {
            var ownerMember = memberRepository.save(MemberFixture.getFirstMember());
            var otherMember = memberRepository.save(MemberFixture.getSecondMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var template = templateRepository.save(TemplateFixture.get(ownerMember, category));

            assertThatThrownBy(() -> sut.deleteByMemberAndIds(otherMember, List.of(template.getId())))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 템플릿에 대한 권한이 없습니다.");

        }

        @Test
        @DisplayName("템플릿 삭제 실패: 동일한 ID가 2개 들어있는 경우")
        void deleteTemplateFailWithDuplicateID() {
            var ownerMember = memberRepository.save(MemberFixture.getFirstMember());
            var otherMember = memberRepository.save(MemberFixture.getSecondMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var template = templateRepository.save(TemplateFixture.get(ownerMember, category));

            assertThatThrownBy(() -> sut.deleteByMemberAndIds(otherMember, List.of(template.getId(), template.getId())))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("삭제하고자 하는 템플릿 ID가 중복되었습니다.");

        }
    }
}
