package codezap.template.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codezap.category.domain.Category;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;

class TemplateTest {

    @Nested
    @DisplayName("템플릿 생성")
    class CreateTemplate {

        @Test
        @DisplayName("성공: 소스 코드가 1개 이상이면 정상적으로 생성")
        void createTemplateWithSourceCode() {
            Member member = MemberFixture.getFirstMember();
            String title = "title";
            String description = "description";
            Category category = CategoryFixture.getDefaultCategory(member);
            Visibility visibility = Visibility.PUBLIC;
            List<SourceCode> sourceCodes = List.of(new SourceCode("filename", "content"));

            Template template = new Template(member, title, description, category, visibility, sourceCodes);

            assertAll(
                    () -> assertThat(template.getTitle()).isEqualTo(title),
                    () -> assertThat(template.getDescription()).isEqualTo(description),
                    () -> assertThat(template.getCategory()).isEqualTo(category),
                    () -> assertThat(template.getVisibility()).isEqualTo(visibility),
                    () -> assertThat(template.getSourceCodes()).hasSize(1)
            );
        }

        @Test
        @DisplayName("실패: 소스 코드가 1개 이상 없으면 예외 발생")
        void createTemplateWithoutSourceCode() {
            Member member = MemberFixture.getFirstMember();
            String title = "title";
            String description = "description";
            Category category = CategoryFixture.getDefaultCategory(member);
            Visibility visibility = Visibility.PUBLIC;
            List<SourceCode> sourceCodes = List.of();

            assertThatThrownBy(() -> new Template(member, title, description, category, visibility, sourceCodes))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("소스 코드는 최소 1개 입력해야 합니다.");
        }
    }

    @Nested
    @DisplayName("템플릿 수정")
    class UpdateTemplate {

        @Test
        @DisplayName("성공: 소스 코드가 1개 이상이면 정상적으로 수정")
        void updateTemplateWithSourceCode() {
            Member member = MemberFixture.getFirstMember();
            String title = "title";
            String description = "description";
            Category category = CategoryFixture.getDefaultCategory(member);
            Visibility visibility = Visibility.PUBLIC;
            List<SourceCode> sourceCodes = List.of(new SourceCode("filename", "content"));
            Template template = new Template(member, title, description, category, visibility, sourceCodes);

            String newTitle = "newTitle";
            String newDescription = "newDescription";
            Category newCategory = CategoryFixture.getDefaultCategory(member);
            Visibility newVisibility = Visibility.PRIVATE;
            List<SourceCode> newSourceCodes = List.of(new SourceCode("newFilename", "newContent"));
            template.updateTemplate(newTitle, newDescription, newCategory, newVisibility, newSourceCodes);

            assertAll(
                    () -> assertThat(template.getTitle()).isEqualTo(newTitle),
                    () -> assertThat(template.getDescription()).isEqualTo(newDescription),
                    () -> assertThat(template.getCategory()).isEqualTo(newCategory),
                    () -> assertThat(template.getVisibility()).isEqualTo(newVisibility),
                    () -> assertThat(template.getSourceCodes()).hasSize(1)
            );
        }

        @Test
        @DisplayName("실패: 소스 코드가 없으면 예외 발생")
        void updateTemplateWithoutSourceCode() {
            Member member = MemberFixture.getFirstMember();
            String title = "title";
            String description = "description";
            Category category = CategoryFixture.getDefaultCategory(member);
            Visibility visibility = Visibility.PUBLIC;
            List<SourceCode> sourceCodes = List.of(new SourceCode("filename", "content"));
            Template template = new Template(member, title, description, category, visibility, sourceCodes);

            assertThatThrownBy(() -> template.updateTemplate(title, description, category, visibility, List.of()))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("소스 코드는 최소 1개 입력해야 합니다.");
        }
    }

    @Nested
    @DisplayName("템플릿 소유자 확인")
    class MatchMember {

        @Test
        @DisplayName("참: 같은 사용자일 경우")
        void matchMemberSuccess() {
            Member member = MemberFixture.getFirstMember();
            String title = "title";
            String description = "description";
            Category category = CategoryFixture.getDefaultCategory(member);
            Visibility visibility = Visibility.PUBLIC;
            List<SourceCode> sourceCodes = List.of(new SourceCode("filename", "content"));
            Template template = new Template(member, title, description, category, visibility, sourceCodes);

            assertThat(template.matchMember(member)).isTrue();
        }

        @Test
        @DisplayName("거짓: 다른 사용자일 경우")
        void matchMemberFail() {
            Member member = MemberFixture.getFirstMember();
            String title = "title";
            String description = "description";
            Category category = CategoryFixture.getDefaultCategory(member);
            Visibility visibility = Visibility.PUBLIC;
            List<SourceCode> sourceCodes = List.of(new SourceCode("filename", "content"));
            Template template = new Template(member, title, description, category, visibility, sourceCodes);

            assertThat(template.matchMember(MemberFixture.getSecondMember())).isFalse();
        }
    }

    @Nested
    @DisplayName("템플릿 공개 범위 확인")
    class IsPrivate {

        @Test
        @DisplayName("참: 비공개 템플릿일 경우")
        void isPrivateTrue() {
            Member member = MemberFixture.getFirstMember();
            String title = "title";
            String description = "description";
            Category category = CategoryFixture.getDefaultCategory(member);
            List<SourceCode> sourceCodes = List.of(new SourceCode("filename", "content"));

            Template privateTemplate = new Template(member, title, description, category, Visibility.PRIVATE, sourceCodes);

            assertThat(privateTemplate.isPrivate()).isTrue();
        }

        @Test
        @DisplayName("거짓: 공개 템플릿일 경우")
        void isPrivateFalse() {
            Member member = MemberFixture.getFirstMember();
            String title = "title";
            String description = "description";
            Category category = CategoryFixture.getDefaultCategory(member);
            List<SourceCode> sourceCodes = List.of(new SourceCode("filename", "content"));

            Template publicTemplate = new Template(member, title, description, category, Visibility.PUBLIC, sourceCodes);

            assertThat(publicTemplate.isPrivate()).isFalse();
        }
    }

    @Nested
    @DisplayName("썸네일 소스 코드")
    class ThumbnailSourceCode {

        @Test
        @DisplayName("성공: 지정된 순서의 소스 코드를 썸네일로 반환")
        void getThumbnailSourceCode() {
            Member member = MemberFixture.getFirstMember();
            String title = "title";
            String description = "description";
            Category category = CategoryFixture.getDefaultCategory(member);
            Visibility visibility = Visibility.PUBLIC;
            List<SourceCode> sourceCodes = List.of(
                    new SourceCode("filename1", "content1"),
                    new SourceCode("filename2", "content2")
            );
            Template template = new Template(member, title, description, category, visibility, sourceCodes);

            SourceCode thumbnailSourceCode = template.getThumbnailSourceCode();

            assertThat(thumbnailSourceCode).isEqualTo(sourceCodes.get(0));
        }
    }

    @Nested
    @DisplayName("좋아요")
    class Likes {

        @Test
        @DisplayName("성공: 좋아요 수 증가")
        void increaseLike() {
            Member member = MemberFixture.getFirstMember();
            String title = "title";
            String description = "description";
            Category category = CategoryFixture.getDefaultCategory(member);
            Visibility visibility = Visibility.PUBLIC;
            List<SourceCode> sourceCodes = List.of(new SourceCode("filename", "content"));
            Template template = new Template(member, title, description, category, visibility, sourceCodes);

            template.increaseLike();

            assertThat(template.getLikesCount()).isEqualTo(1L);
        }

        @Test
        @DisplayName("취소: 좋아요 수 감소")
        void cancelLike() {
            Member member = MemberFixture.getFirstMember();
            String title = "title";
            String description = "description";
            Category category = CategoryFixture.getDefaultCategory(member);
            Visibility visibility = Visibility.PUBLIC;
            List<SourceCode> sourceCodes = List.of(new SourceCode("filename", "content"));
            Template template = new Template(member, title, description, category, visibility, sourceCodes);
            template.increaseLike();
            template.increaseLike();

            template.cancelLike();

            assertThat(template.getLikesCount()).isEqualTo(1L);
        }

        @Test
        @DisplayName("취소: 좋아요 수가 0일 때 취소해도 0이 유지")
        void cancelLikeWhenLikesCountIsZero() {
            Member member = MemberFixture.getFirstMember();
            String title = "title";
            String description = "description";
            Category category = CategoryFixture.getDefaultCategory(member);
            Visibility visibility = Visibility.PUBLIC;
            List<SourceCode> sourceCodes = List.of(new SourceCode("filename", "content"));
            Template template = new Template(member, title, description, category, visibility, sourceCodes);

            template.cancelLike();

            assertThat(template.getLikesCount()).isZero();
        }
    }
}
