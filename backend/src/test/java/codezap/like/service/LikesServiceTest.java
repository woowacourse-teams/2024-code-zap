package codezap.like.service;

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
import codezap.like.domain.Likes;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.template.domain.Template;

@SpringBootTest
@DatabaseIsolation
class LikesServiceTest extends ServiceTest {

    @Autowired
    private LikesService likesService;

    @Nested
    @DisplayName("좋아요 기능 테스트")
    class LikesTest {

        @Test
        @DisplayName("성공")
        void success() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

            likesService.like(MemberDto.from(member), template.getId());

            assertThat(likesRepository.existsByTemplateAndMember(template, member)).isTrue();
        }

        @Test
        @DisplayName("성공: 동일한 사람이 동일한 템플릿에 여러번 좋아요를 해도 Likes 가 한번만 생성된다.")
        void multipleLikes() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

            likesService.like(MemberDto.from(member), template.getId());
            likesService.like(MemberDto.from(member), template.getId());

            assertThat(likesRepository.countByTemplate(template)).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("좋아요 취소 기능 테스트")
    class CancelLikesTest {

        @Test
        @DisplayName("성공")
        void success() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));
            likesService.like(MemberDto.from(member), template.getId());

            likesService.cancelLike(MemberDto.from(member), template.getId());

            assertThat(likesRepository.existsByTemplateAndMember(template, member)).isFalse();
        }

        @Test
        @DisplayName("성공: 여러번 좋아요를 취소해도 정상 동작으로 판단")
        void multipleLikes() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

            likesService.cancelLike(MemberDto.from(member), template.getId());

            assertThatCode(() -> likesService.cancelLike(MemberDto.from(member), template.getId()))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("좋아요 개수 조회 기능 테스트")
    class CountByTemplateTest {

        @Test
        @DisplayName("성공")
        void success() {
            Member member1 = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member1,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));
            Member member2 = memberRepository.save(MemberFixture.getSecondMember());

            likesService.like(MemberDto.from(member1), template.getId());
            likesService.like(MemberDto.from(member2), template.getId());

            assertThat(likesService.getLikesCount(template)).isEqualTo(2L);
        }

        @Test
        @DisplayName("성공: 좋아요가 없으면 0개가 조회된다.")
        void successWithNoLikes() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

            assertThat(likesService.getLikesCount(template)).isEqualTo(0L);
        }
    }

    @Nested
    @DisplayName("좋아요 여부 조회 테스트")
    class IsLikeTest {

        @Test
        @DisplayName("성공: 좋아요를 했을 때")
        void successWithLike() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

            likesRepository.save(template.like(member));

            assertThat(likesService.isLike(template, member)).isTrue();
        }

        @Test
        @DisplayName("성공: 좋아요를 하지 않았을 때")
        void successWithNoLike() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

            assertThat(likesService.isLike(template, member)).isFalse();
        }
    }
}
