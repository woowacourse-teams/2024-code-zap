package codezap.likes.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.ServiceTest;
import codezap.likes.domain.Likes;
import codezap.member.domain.Member;
import codezap.template.domain.Template;

class LikesServiceTest extends ServiceTest {

    @Autowired
    private LikesService likesService;

    @Autowired
    private EntityManager entityManager;

    @Nested
    @DisplayName("좋아요")
    class LikesTest {

        @Test
        @DisplayName("성공: 템플릿 수정 시간은 변경되지 않는다.")
        void success() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getDefaultCategory(member));
            Template template = templateRepository.save(TemplateFixture.get(member, category));

            LocalDateTime modifiedAtBeforeLike = template.getModifiedAt();

            likesService.like(member, template.getId());
            entityManager.flush();

            assertAll(
                    () -> assertThat(likesRepository.existsByMemberAndTemplate(member, template)).isTrue(),
                    () -> assertThat(template.getModifiedAt()).isEqualTo(modifiedAtBeforeLike)
            );
        }

        @Test
        @DisplayName("성공: 동일한 사람이 동일한 템플릿에 여러번 좋아요를 해도 Likes 가 한번만 생성된다.")
        void multipleLikes() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getDefaultCategory(member));
            Template template = templateRepository.save(TemplateFixture.get(member, category));

            likesService.like(member, template.getId());
            likesService.like(member, template.getId());

            assertThat(likesRepository.countByTemplate(template)).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("좋아요 취소")
    class CancelLikesTest {

        @Test
        @DisplayName("성공")
        void success() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getDefaultCategory(member));
            Template template = templateRepository.save(TemplateFixture.get(member, category));

            likesService.like(member, template.getId());
            LocalDateTime modifiedAtBeforeLike = template.getModifiedAt();

            likesService.cancelLike(member, template.getId());
            entityManager.flush();

            assertAll(
                    () -> assertThat(likesRepository.existsByMemberAndTemplate(member, template)).isFalse(),
                    () -> assertThat(template.getModifiedAt()).isEqualTo(modifiedAtBeforeLike)
            );
        }

        @Test
        @DisplayName("성공: 본인의 좋아요만 취소 가능")
        void cancelMyLikes() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getDefaultCategory(member));
            Template template = templateRepository.save(TemplateFixture.get(member, category));

            Member otherMember = memberRepository.save(MemberFixture.getSecondMember());

            likesService.like(otherMember, template.getId());
            likesService.cancelLike(member, template.getId());

            assertThat(likesRepository.countByTemplate(template)).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("좋아요 여부 조회")
    class IsLikeTest {

        @Test
        @DisplayName("성공: 좋아요를 했을 때")
        void successWithLike() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getDefaultCategory(member));
            Template template = templateRepository.save(TemplateFixture.get(member, category));

            likesRepository.save(new Likes(template, member));

            assertThat(likesService.isLiked(member, template)).isTrue();
        }

        @Test
        @DisplayName("성공: 좋아요를 하지 않았을 때")
        void successWithNoLike() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getDefaultCategory(member));
            Template template = templateRepository.save(TemplateFixture.get(member, category));

            assertThat(likesService.isLiked(member, template)).isFalse();
        }
    }

    @Nested
    @DisplayName("템플릿에 해당하는 좋아요 삭제")
    class DeleteLikeByTemplateIds {

        @Test
        @DisplayName("성공: 템플릿 1개 삭제")
        void deleteAllByTemplateIdSuccess() {
            Member member1 = memberRepository.save(MemberFixture.getFirstMember());
            Member member2 = memberRepository.save(MemberFixture.getSecondMember());
            Category category = categoryRepository.save(CategoryFixture.getDefaultCategory(member1));
            Template template1 = templateRepository.save(TemplateFixture.get(member1, category));
            Template template2 = templateRepository.save(TemplateFixture.get(member1, category));
            likesRepository.save(new Likes(template1, member1));
            likesRepository.save(new Likes(template1, member2));
            likesRepository.save(new Likes(template2, member1));

            likesService.deleteAllByTemplateIds(List.of(template1.getId()));

            assertAll(
                    () -> assertThat(likesRepository.countByTemplate(template1)).isEqualTo(0),
                    () -> assertThat(likesRepository.countByTemplate(template2)).isEqualTo(1)
            );
        }

        @Test
        @DisplayName("성공: 템플릿 2개 이상 삭제")
        void deleteAllByTemplateIdsSuccess() {
            Member member1 = memberRepository.save(MemberFixture.getFirstMember());
            Member member2 = memberRepository.save(MemberFixture.getSecondMember());
            Category category = categoryRepository.save(CategoryFixture.getDefaultCategory(member1));
            Template template1 = templateRepository.save(TemplateFixture.get(member1, category));
            Template template2 = templateRepository.save(TemplateFixture.get(member1, category));
            likesRepository.save(new Likes(template1, member1));
            likesRepository.save(new Likes(template1, member2));
            likesRepository.save(new Likes(template2, member1));

            likesService.deleteAllByTemplateIds(List.of(template1.getId(), template2.getId()));

            assertAll(
                    () -> assertThat(likesRepository.countByTemplate(template1)).isEqualTo(0),
                    () -> assertThat(likesRepository.countByTemplate(template2)).isEqualTo(0)
            );
        }
    }
}
