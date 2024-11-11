package codezap.likes.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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

    @Nested
    @DisplayName("좋아요")
    class LikesTest {

        @Test
        @DisplayName("성공")
        void success() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

            likesService.like(member, template.getId());

            assertThat(likesRepository.existsByMemberAndTemplate(member, template)).isTrue();
        }

        @Test
        @DisplayName("성공: 동일한 사람이 동일한 템플릿에 여러번 좋아요를 해도 Likes 가 한번만 생성된다.")
        void multipleLikes() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

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
            Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
            Template template = templateRepository.save(TemplateFixture.get(member, category));
            likesService.like(member, template.getId());

            likesService.cancelLike(member, template.getId());

            assertThat(likesRepository.existsByMemberAndTemplate(member, template)).isFalse();
        }

        @Test
        @DisplayName("성공: 본인의 좋아요만 취소 가능")
        void cancelMyLikes() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Member otherMember = memberRepository.save(MemberFixture.getSecondMember());
            Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
            Template template = templateRepository.save(TemplateFixture.get(member, category));

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
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

            likesRepository.save(new Likes(template, member));

            assertThat(likesService.isLiked(member, template)).isTrue();
        }

        @Test
        @DisplayName("성공: 좋아요를 하지 않았을 때")
        void successWithNoLike() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

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
            Template template1 = templateRepository.save(TemplateFixture.get(
                    member1,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));
            Template template2 = templateRepository.save(TemplateFixture.get(
                    member1,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));
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
            Template template1 = templateRepository.save(TemplateFixture.get(
                    member1,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));
            Template template2 = templateRepository.save(TemplateFixture.get(
                    member1,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));
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

    @Nested
    @DisplayName("좋아요한 템플릿 조회")
    class FindAllByMemberId {

        @Test
        @DisplayName("성공")
        void findAllByMemberId() {
            // given
            Member member1 = memberRepository.save(MemberFixture.getFirstMember());
            Member member2 = memberRepository.save(MemberFixture.getSecondMember());
            Category category1 = categoryRepository.save(CategoryFixture.get(member1));
            Category category2 = categoryRepository.save(CategoryFixture.get(member2));
            Template template1 = templateRepository.save(TemplateFixture.get(member1, category1));
            Template template2 = templateRepository.save(TemplateFixture.get(member1, category1));
            Template template3 = templateRepository.save(TemplateFixture.get(member2, category2));
            likesRepository.save(new Likes(template1, member1));
            likesRepository.save(new Likes(template2, member2));
            likesRepository.save(new Likes(template3, member1));

            // when
            Page<Template> actual = likesService.findAllByMemberId(member1.getId(), PageRequest.of(0, 5));

            // then
            assertThat(actual).containsExactlyInAnyOrder(template1, template3);
        }
    }
}
