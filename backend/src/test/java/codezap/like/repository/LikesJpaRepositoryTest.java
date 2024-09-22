package codezap.like.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.repository.CategoryRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.repository.JpaRepositoryTest;
import codezap.like.domain.Likes;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.Template;
import codezap.template.repository.TemplateRepository;

@JpaRepositoryTest
class LikesJpaRepositoryTest {

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Nested
    @DisplayName("좋아요 저장 테스트")
    class SaveTest {

        @Test
        @DisplayName("성공")
        void success() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

            likesRepository.save(new Likes(null, template, member));

            assertThat(likesRepository.countByTemplate(template)).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("템플릿과 멤버 정보로 좋아요 조회 테스트")
    class FindByTemplateAndMemberTest {

        @Test
        @DisplayName("성공")
        void success() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));
            likesRepository.save(new Likes(null, template, member));

            assertThat(likesRepository.existsByTemplateAndMember(template, member))
                    .isTrue();
        }

        @Test
        @DisplayName("성공: 저장되지 않은 좋아요를 찾을 수 없다.")
        void successWithNoData() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

            assertThat(likesRepository.existsByTemplateAndMember(template, member))
                    .isFalse();
        }
    }

    @Nested
    @DisplayName("템플릿과 멤버로 좋아요 삭제 테스트")
    class DeleteByTemplateAndMember {

        @Test
        @DisplayName("성공")
        void success() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));
            likesRepository.save(template.like(member));

            likesRepository.deleteByTemplateAndMember(template, member);

            assertThat(likesRepository.existsByTemplateAndMember(template, member)).isFalse();
        }

        @Test
        @DisplayName("성공: 삭제할 데이터가 존재하지 않아도 정상 동작으로 판단")
        void successWithNoLike() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

            assertThatCode(() -> likesRepository.deleteByTemplateAndMember(template, member))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("템플릿 좋아요 수 조회 테스트")
    class CountByTemplate {

        @Test
        @DisplayName("성공")
        void success() {
            Member member1 = memberRepository.save(MemberFixture.getFirstMember());
            Member member2 = memberRepository.save(MemberFixture.getSecondMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member1,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

            likesRepository.save(template.like(member1));
            likesRepository.save(template.like(member2));

            assertThat(likesRepository.countByTemplate(template)).isEqualTo(2);
        }

        @Test
        @DisplayName("성공: 좋아요가 없으면 0개가 조회된다.")
        void successWithNoLikes() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

            assertThat(likesRepository.countByTemplate(template)).isEqualTo(0);
        }
    }
}
