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

            Likes likes = new Likes(null, template, member);

            assertThatCode(() -> likesRepository.save(likes))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("템플릿과 멤버 정보로 좋아요 조회")
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
}
