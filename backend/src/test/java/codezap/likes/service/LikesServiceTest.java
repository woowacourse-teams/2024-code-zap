package codezap.likes.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.DatabaseIsolation;
import codezap.global.ServiceTest;
import codezap.likes.domain.Likes;
import codezap.member.domain.Member;
import codezap.template.domain.Template;

@SpringBootTest
@DatabaseIsolation
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
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));
            likesRepository.save(new Likes(null, template, member));

            likesService.cancelLike(member, template.getId());

            assertThat(likesRepository.existsByMemberAndTemplate(member, template)).isFalse();
        }

        @Test
        @DisplayName("성공: 여러번 좋아요를 취소해도 정상 동작으로 판단")
        void multipleLikes() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

            likesService.cancelLike(member, template.getId());

            assertThatCode(() -> likesService.cancelLike(member, template.getId()))
                    .doesNotThrowAnyException();
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

            likesRepository.save(new Likes(null, template, member));

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
}
