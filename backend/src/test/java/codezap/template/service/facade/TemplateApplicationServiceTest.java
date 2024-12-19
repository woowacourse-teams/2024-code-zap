package codezap.template.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import codezap.category.domain.Category;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.SourceCodeFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.ServiceTest;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.likes.domain.Likes;
import codezap.member.domain.Member;
import codezap.tag.domain.Tag;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;
import codezap.template.domain.Visibility;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSourceCodeRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTemplateItemResponse;
import codezap.template.dto.response.FindAllTemplatesResponse;

class TemplateApplicationServiceTest extends ServiceTest {

    @Autowired
    TemplateApplicationService sut;

    @Nested
    @DisplayName("템플릿 생성")
    class CreateTemplate {

        @Test
        @DisplayName("성공: 작성자의 권한 확인 후 템플릿 생성")
        void createTemplate() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var request = createTemplateRequest(category);

            // when
            var actual = sut.create(member, request);

            // then
            assertThat(categoryRepository.fetchById(actual)).isNotNull();
        }

        @Test
        @DisplayName("실패: 카테고리에 대한 권한이 없는 경우")
        void createTemplate_Fail_NoAuthorization() {
            // given
            var ownerMember = memberRepository.save(MemberFixture.getFirstMember());
            var otherMember = memberRepository.save(MemberFixture.getSecondMember());
            var category = categoryRepository.save(CategoryFixture.get(ownerMember));
            var request = createTemplateRequest(category);

            // when & then
            assertThatThrownBy(() -> sut.create(otherMember, request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
        }

        @Test
        @Disabled("@Transactional 과 함께 사용 시 처리 불가능")
        @DisplayName("동시성 테스트 - 태그 중복이 발생하지 않는지 확인")
        void createTagsConcurrencyTest() throws InterruptedException {
            // Given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            CreateTemplateRequest createTemplateRequest = createTemplateRequest(category);

            var member2 = memberRepository.save(MemberFixture.getSecondMember());
            var category2 = categoryRepository.save(CategoryFixture.getSecondCategory());
            CreateTemplateRequest createTemplateRequest2 = createTemplateRequest(category2);

            // When
            ExecutorService executorService = Executors.newFixedThreadPool(2);

            executorService.execute(() -> sut.create(member, createTemplateRequest));
            executorService.execute(() -> sut.create(member2, createTemplateRequest2));

            executorService.shutdown();
            executorService.awaitTermination(30, TimeUnit.SECONDS);

            // Then
            List<Tag> tags = tagRepository.findAllByNames(List.of("Spring"));
            assertThat(tags).hasSize(1);
        }

        private static CreateTemplateRequest createTemplateRequest(Category category) {
            var title = "title1";
            var description = "description1";
            var sourceCodeRequest = new CreateSourceCodeRequest("filename1", "content1", 1);
            var sourceCodes = List.of(sourceCodeRequest);
            int thumbnailOrdinal = 1;
            List<String> tags = List.of();
            return new CreateTemplateRequest(
                    title,
                    description,
                    sourceCodes,
                    thumbnailOrdinal,
                    category.getId(),
                    tags,
                    Visibility.PUBLIC);
        }
    }

    @Nested
    @DisplayName("ID로 템플릿 조회 (비회원)")
    class FindByTemplateId {

        @Test
        @DisplayName("ID로 템플릿 조회 성공")
        void findByTemplateId() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template = templateRepository.save(TemplateFixture.get(member, category));

            // when
            var actual = sut.findById(template.getId());

            // then
            assertAll(
                    () -> assertThat(actual.id()).isEqualTo(1L),
                    () -> assertThat(actual.isLiked()).isFalse()
            );
        }

        @Test
        @DisplayName("ID로 템플릿 조회 실패: 다른 사람의 private 템플릿 조회 불가")
        void findByTemplateIdFailOtherPersonPrivateTemplate() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template = templateRepository.save(TemplateFixture.getPrivate(member, category));

            // when & then
            assertThatThrownBy(() -> sut.findById(template.getId()))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 템플릿은 비공개 템플릿입니다.")
                    .extracting("errorCode").isEqualTo(ErrorCode.FORBIDDEN_ACCESS);
        }
    }

    @Nested
    @DisplayName("ID로 템플릿 조회 (회원)")
    class GetByIdWithMember {

        @Test
        @DisplayName("ID로 템플릿 조회 성공: 좋아요를 했을 때")
        void getByIdWithMemberLikes() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template = templateRepository.save(TemplateFixture.get(member, category));
            likesRepository.save(new Likes(null, template, member));

            // when
            var actual = sut.findById(template.getId(), member);

            // then
            assertAll(
                    () -> assertThat(actual.id()).isEqualTo(1L),
                    () -> assertThat(actual.isLiked()).isTrue()
            );
        }

        @Test
        @DisplayName("ID로 템플릿 조회 성공: 좋아요를 하지 않았을 때")
        void getByIdWithMemberNoLikes() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template = templateRepository.save(TemplateFixture.get(member, category));

            // when
            var actual = sut.findById(template.getId(), member);

            // then
            assertAll(
                    () -> assertThat(actual.id()).isEqualTo(1L),
                    () -> assertThat(actual.isLiked()).isFalse()
            );
        }

        @Test
        @DisplayName("ID로 템플릿 조회 실패: 다른 사람의 private 템플릿 조회 불가")
        void findByTemplateIdFailOtherPersonPrivateTemplate() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var otherMember = memberRepository.save(MemberFixture.getSecondMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template = templateRepository.save(TemplateFixture.getPrivate(member, category));

            // when & then
            assertThatThrownBy(() -> sut.findById(template.getId(), otherMember))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 템플릿은 비공개 템플릿입니다.")
                    .extracting("errorCode").isEqualTo(ErrorCode.FORBIDDEN_ACCESS);
        }

        @Test
        @DisplayName("ID로 템플릿 조회 성공: 내 private 템플릿 조회 가능")
        void findByTemplateIdSuccessPrivateTemplate() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template = templateRepository.save(TemplateFixture.getPrivate(member, category));

            // when
            var actual = sut.findById(template.getId(), member);

            // then
            assertAll(
                    () -> assertThat(actual.id()).isEqualTo(1L),
                    () -> assertThat(actual.isLiked()).isFalse()
            );
        }
    }

    @Nested
    @DisplayName("템플릿 목록 조회 (비회원)")
    class FindAllBy {

        @ParameterizedTest
        @MethodSource
        @DisplayName("사용자ID, 검색어, 카테고리ID, 태그ID로 템플릿 조회 성공")
        void findAllBy(
                Long memberId,
                String keyword,
                Long categoryId,
                List<Long> tagIds,
                Pageable pageable
        ) {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));

            var template = savePublicTemplate(member, category);
            var privateTemplate = savePrivateTemplate(member, category);

            // when & then
            assertAll(
                    () -> assertThatCode(() -> sut.findAllBy(memberId, keyword, categoryId, tagIds, pageable))
                            .doesNotThrowAnyException(),
                    () -> assertThat(sut.findAllBy(memberId, keyword, categoryId, tagIds, pageable).templates())
                            .extracting("id").doesNotContain(privateTemplate.getId())
            );

        }

        static Stream<Arguments> findAllBy() {
            return Stream.of(
                    Arguments.of(null, null, null, null, Pageable.ofSize(5)),
                    Arguments.of(1L, null, null, null, Pageable.ofSize(5)),
                    Arguments.of(null, "keyword", null, null, Pageable.ofSize(5)),
                    Arguments.of(null, null, 1L, null, Pageable.ofSize(5)),
                    Arguments.of(null, null, null, List.of(1L, 2L), Pageable.ofSize(5)),
                    Arguments.of(null, null, null, null, Pageable.ofSize(5))
            );
        }

        @Test
        @DisplayName("좋아요 정보 조회 테스트")
        void findAllByIsLikedTest() {
            // given
            saveDummyTemplates20();

            // when
            List<FindAllTemplateItemResponse> searchedTemplates = sut.findAllBy(null, null, null, null,
                            Pageable.ofSize(1))
                    .templates();

            //then
            assertThat(searchedTemplates).allMatch((findAllTemplateItem) -> !findAllTemplateItem.isLiked());
        }

        private void saveDummyTemplates20() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            for (int i = 0; i < 20; i++) {
                var template = templateRepository.save(new Template(member, "title" + i, "description" + i, category));
                var sourceCode = sourceCodeRepository.save(new SourceCode(template, "filename" + i, "content" + i, 1));
                thumbnailRepository.save(new Thumbnail(template, sourceCode));
            }
        }
    }

    @Nested
    @DisplayName("템플릿 목록 조회 (회원)")
    class FindAllByWithMember {

        @ParameterizedTest
        @MethodSource
        @DisplayName("사용자ID, 검색어, 카테고리ID, 태그ID로 템플릿 검색 성공")
        void findAllBy(
                Long memberId,
                String keyword,
                Long categoryId,
                List<Long> tagIds,
                Pageable pageable
        ) {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template1 = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template1, 1));
            thumbnailRepository.save(new Thumbnail(template1, sourceCode1));

            // when & then
            assertThatCode(() -> sut.findAllBy(memberId, keyword, categoryId, tagIds, pageable, member))
                    .doesNotThrowAnyException();
        }

        static Stream<Arguments> findAllBy() {
            return Stream.of(
                    Arguments.of(null, null, null, null, Pageable.ofSize(5)),
                    Arguments.of(1L, null, null, null, Pageable.ofSize(5)),
                    Arguments.of(null, "keyword", null, null, Pageable.ofSize(5)),
                    Arguments.of(null, null, 1L, null, Pageable.ofSize(5)),
                    Arguments.of(null, null, null, List.of(1L, 2L), Pageable.ofSize(5)),
                    Arguments.of(null, null, null, null, Pageable.ofSize(5))
            );
        }

        @Test
        @DisplayName("검색 성공: 사용자ID가 없을 경우 (private 템플릿 제외)")
        void findAllByWithNotMemberId() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            Pageable pageable = Pageable.ofSize(5);

            var template = savePublicTemplate(member, category);
            var privateTemplate = savePrivateTemplate(member, category);

            // when
            var actual = sut.findAllBy(null, null, null, null, pageable, member);

            // then
            assertThat(actual.templates()).extracting("id")
                    .containsExactly(template.getId())
                    .doesNotContain(privateTemplate.getId());
        }

        @Test
        @DisplayName("검색 성공: 사용자ID와 로그인 정보가 같을 경우 (private 템플릿 포함)")
        void findAllByWithMemberIdSameLoginMember() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            Pageable pageable = Pageable.ofSize(5);

            var template = savePublicTemplate(member, category);
            var privateTemplate = savePrivateTemplate(member, category);

            // when
            var actual = sut.findAllBy(1L, null, null, null, pageable, member);

            // then
            assertThat(actual.templates()).extracting("id")
                    .containsExactlyInAnyOrder(template.getId(), privateTemplate.getId());
        }

        @Test
        @DisplayName("검색 성공: 사용자ID와 로그인 정보가 다를 경우 (private 템플릿 제외)")
        void findAllByWithMemberIdDifferentLoginMember() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var loginMember = memberRepository.save(MemberFixture.getSecondMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            Pageable pageable = Pageable.ofSize(5);

            var template = savePublicTemplate(member, category);
            var privateTemplate = savePrivateTemplate(member, category);

            // when
            var actual = sut.findAllBy(1L, null, null, null, pageable, loginMember);

            // then
            assertThat(actual.templates()).extracting("id")
                    .containsExactly(template.getId())
                    .doesNotContain(privateTemplate.getId());
        }

        @Test
        @DisplayName("검색 성공: 좋아요 정보 조회")
        void findAllByIsLikedTest() {
            // given
            Member loginMember = memberRepository.save(MemberFixture.getFirstMember());
            saveDummyTemplates20();
            List<Long> likeTemplatesIds = List.of(1L, 5L, 7L, 9L, 13L);
            for (long likeTemplateId : likeTemplatesIds) {
                likesRepository.save(new Likes(null, templateRepository.fetchById(likeTemplateId), loginMember));
            }

            // when
            List<FindAllTemplateItemResponse> templates =
                    sut.findAllBy(null, null, null, null, Pageable.ofSize(1), loginMember)
                            .templates();
            List<FindAllTemplateItemResponse> likesTemplate = templates.stream()
                    .filter((template) -> likeTemplatesIds.contains(template.id()))
                    .toList();
            List<FindAllTemplateItemResponse> notLikesTemplate = templates.stream()
                    .filter((template) -> !likeTemplatesIds.contains(template.id()))
                    .toList();

            //then
            assertAll(
                    () -> assertThat(likesTemplate).allMatch(FindAllTemplateItemResponse::isLiked),
                    () -> assertThat(notLikesTemplate).allMatch((template) -> !template.isLiked())
            );
        }

        private void saveDummyTemplates20() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            for (int i = 0; i < 20; i++) {
                var template = templateRepository.save(new Template(member, "title" + i, "description" + i, category));
                var sourceCode = sourceCodeRepository.save(new SourceCode(template, "filename" + i, "content" + i, 1));
                thumbnailRepository.save(new Thumbnail(template, sourceCode));
            }
        }
    }

    @Nested
    @DisplayName("템플릿 수정")
    class Update {

        @Test
        @DisplayName("템플릿 업데이트 성공")
        void update() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            var sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));
            thumbnailRepository.save(new Thumbnail(template, sourceCode1));

            var createRequest = List.of(new CreateSourceCodeRequest("filename3", "content3", 3));
            var updateRequest1 = updateSourceCodeRequest(sourceCode1);
            var updateRequest2 = updateSourceCodeRequest(sourceCode2);
            var updateRequest = List.of(updateRequest1, updateRequest2);
            List<Long> deleteIds = List.of();
            var request = new UpdateTemplateRequest(
                    "Updated Template",
                    "Updated Description",
                    createRequest,
                    updateRequest,
                    deleteIds,
                    category.getId(),
                    List.of(),
                    Visibility.PUBLIC
            );

            // when
            sut.update(member, template.getId(), request);

            // when & then
            var updatedTemplate = templateRepository.fetchById(template.getId());
            assertAll(
                    () -> assertEquals("Updated Template", updatedTemplate.getTitle()),
                    () -> assertEquals("Updated Description", updatedTemplate.getDescription())
            );
        }

        @Test
        @DisplayName("실패: 카테고리에 대한 권한이 없는 경우")
        void updateTemplate_WhenNoAuthorization() {
            // given
            Member otherMember = memberRepository.save(MemberFixture.getFirstMember());
            Category othersCategory = categoryRepository.save(CategoryFixture.get(otherMember));

            Member member = memberRepository.save(MemberFixture.getSecondMember());
            Category category = categoryRepository.save(CategoryFixture.get(member));
            Template template = templateRepository.save(TemplateFixture.get(member, category));

            SourceCode sourceCode = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            thumbnailRepository.save(new Thumbnail(template, sourceCode));
            UpdateSourceCodeRequest updateSourceCodeRequest = updateSourceCodeRequest(sourceCode);

            UpdateTemplateRequest request = new UpdateTemplateRequest(
                    "Updated Template",
                    "Updated Description",
                    Collections.emptyList(),
                    List.of(updateSourceCodeRequest),
                    Collections.emptyList(),
                    othersCategory.getId(),
                    Collections.emptyList(),
                    Visibility.PUBLIC);

            // when & then
            assertThatThrownBy(() -> sut.update(otherMember, template.getId(), request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
        }

        private UpdateSourceCodeRequest updateSourceCodeRequest(SourceCode sourceCode) {
            return new UpdateSourceCodeRequest(
                    sourceCode.getId(),
                    sourceCode.getFilename(),
                    sourceCode.getContent(),
                    sourceCode.getOrdinal());
        }
    }

    @Nested
    @DisplayName("템플릿 삭제")
    class DeleteByMemberAndIds {

        @Test
        @DisplayName("성공: 썸네일, 소스코드, 태그, 좋아요가 모두 존재하는 템플릿")
        void deleteByMemberAndIds() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template1 = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template1, 1));
            var template2 = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template2, 1));
            var template3 = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode3 = sourceCodeRepository.save(SourceCodeFixture.get(template3, 1));
            likesRepository.save(new Likes(template1, member));
            likesRepository.save(new Likes(template2, member));

            var deleteIds = List.of(template1.getId(), template2.getId());

            // when
            sut.deleteAllByMemberAndTemplateIds(member, deleteIds);

            // then
            var actualTemplatesLeft = templateRepository.findAll(member.getId(), null, null, null, null, PageRequest.of(0, 10));
            var actualSourceCodeLeft = sourceCodeRepository.findAllByTemplate(template1);
            actualSourceCodeLeft.addAll(sourceCodeRepository.findAllByTemplate(template2));
            actualSourceCodeLeft.addAll(sourceCodeRepository.findAllByTemplate(template3));

            assertAll(
                    () -> assertThat(actualTemplatesLeft.contents()).containsExactly(template3),
                    () -> assertThat(actualTemplatesLeft.contents()).doesNotContain(template1, template2),
                    () -> assertThat(actualSourceCodeLeft).containsExactly(sourceCode3),
                    () -> assertThat(actualSourceCodeLeft).doesNotContain(sourceCode1, sourceCode2)
            );
        }
    }

    @Nested
    @DisplayName("회원이 좋아요한 템플릿 목록 조회")
    class FindAllByLiked {

        @Test
        @DisplayName("성공: 로그인 정보와 조회하려는 멤버 ID가 같을 경우")
        void findAllByLikedSuccessSameMember() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var otherMember = memberRepository.save(MemberFixture.getSecondMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template1 = savePublicTemplate(member, category);
            var template2 = savePublicTemplate(member, category);
            var template3 = savePublicTemplate(member, category);

            likesRepository.save(new Likes(template1, member));
            likesRepository.save(new Likes(template2, member));
            likesRepository.save(new Likes(template3, otherMember));

            // when
            FindAllTemplatesResponse actual = sut.findAllByLiked(member.getId(), PageRequest.of(0, 5));

            // then
            assertAll(
                    () -> assertThat(actual.templates()).extracting("id")
                            .containsExactlyInAnyOrder(template1.getId(), template2.getId()),
                    () -> assertThat(actual.templates()).extracting("isLiked")
                            .containsExactlyInAnyOrder(true, true)
            );
        }
    }

    private Template savePrivateTemplate(Member member, Category category) {
        var privateTemplate = templateRepository.save(TemplateFixture.getPrivate(member, category));
        var privateSourceCode = sourceCodeRepository.save(SourceCodeFixture.get(privateTemplate, 1));
        thumbnailRepository.save(new Thumbnail(privateTemplate, privateSourceCode));
        return privateTemplate;
    }

    private Template savePublicTemplate(Member member, Category category) {
        var template = templateRepository.save(TemplateFixture.get(member, category));
        var sourceCode = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
        thumbnailRepository.save(new Thumbnail(template, sourceCode));
        return template;
    }
}
