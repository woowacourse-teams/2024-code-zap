package codezap.likes.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.repository.RepositoryTest;
import codezap.likes.domain.Likes;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.Template;
import codezap.template.repository.TemplateRepository;

@RepositoryTest
class LikesRepositoryTest {

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TemplateRepository templateRepository;

    private Member member;
    private Category category;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MemberFixture.getFirstMember());
        category = categoryRepository.save(CategoryFixture.getDefaultCategory(member));
    }

    @Nested
    @DisplayName("좋아요 저장 테스트")
    class SaveTest {

        @Test
        @DisplayName("성공")
        void success() {
            Template template = templateRepository.save(TemplateFixture.get(member, category));

            likesRepository.save(new Likes(null, template, member));

            assertThat(likesRepository.countByTemplate(template)).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("멤버와 템플릿 정보로 좋아요 조회 테스트")
    class FindByTemplateAndMemberTest {

        @Test
        @DisplayName("성공")
        void success() {
            Template template = templateRepository.save(TemplateFixture.get(member, category));
            likesRepository.save(new Likes(null, template, member));

            assertThat(likesRepository.existsByMemberAndTemplate(member, template))
                    .isTrue();
        }

        @Test
        @DisplayName("성공: 저장되지 않은 좋아요를 찾을 수 없다.")
        void successWithNoData() {
            Template template = templateRepository.save(TemplateFixture.get(member, category));

            assertThat(likesRepository.existsByMemberAndTemplate(member, template))
                    .isFalse();
        }
    }

    @Nested
    @DisplayName("멤버와 템플릿으로 좋아요 삭제 테스트")
    class DeleteByTemplateAndMember {

        @Test
        @DisplayName("성공")
        void success() {
            Template template = templateRepository.save(TemplateFixture.get(member, category));

            likesRepository.save(new Likes(null, template, member));

            likesRepository.deleteByMemberAndTemplate(member, template);

            assertThat(likesRepository.existsByMemberAndTemplate(member, template)).isFalse();
        }

        @Test
        @DisplayName("성공: 삭제할 데이터가 존재하지 않아도 정상 동작으로 판단")
        void successWithNoLike() {
            Template template = templateRepository.save(TemplateFixture.get(member, category));

            assertThatCode(() -> likesRepository.deleteByMemberAndTemplate(member, template))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("템플릿 좋아요 수 조회 테스트")
    class CountByTemplate {

        @Test
        @DisplayName("성공")
        void success() {
            Member otherMember = memberRepository.save(MemberFixture.getSecondMember());
            Template template = templateRepository.save(TemplateFixture.get(member, category));

            likesRepository.save(new Likes(null, template, member));
            likesRepository.save(new Likes(null, template, otherMember));

            assertThat(likesRepository.countByTemplate(template)).isEqualTo(2);
        }

        @Test
        @DisplayName("성공: 좋아요가 없으면 0개가 조회된다.")
        void successWithNoLikes() {
            Template template = templateRepository.save(TemplateFixture.get(member, category));

            assertThat(likesRepository.countByTemplate(template)).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("템플릿 ID로 템플릿에 존재하는 좋아요 삭제 테스트")
    class DeleteByTemplateIds {

        @Test
        @DisplayName("성공: 템플릿 ID로 템플릿에 존재하는 좋아요 삭제 (템플릿 1개)")
        void testDeleteByTemplateId() {
            Member member1 = memberRepository.save(MemberFixture.getFirstMember());
            Member member2 = memberRepository.save(MemberFixture.getSecondMember());

            Template template1 = templateRepository.save(TemplateFixture.get(member, category));
            Template template2 = templateRepository.save(TemplateFixture.get(member, category));

            likesRepository.save(new Likes(template1, member1));
            likesRepository.save(new Likes(template1, member2));
            likesRepository.save(new Likes(template2, member1));

            likesRepository.deleteAllByTemplateIds(List.of(template1.getId()));

            assertAll(
                    () -> assertThat(likesRepository.countByTemplate(template1)).isEqualTo(0),
                    () -> assertThat(likesRepository.countByTemplate(template2)).isEqualTo(1)
            );
        }

        @Test
        @DisplayName("성공: 템플릿 ID로 템플릿에 존재하는 좋아요 삭제 (템플릿 2개)")
        void testDeleteByTemplateIds() {
            Member otherMember = memberRepository.save(MemberFixture.getSecondMember());
            Template template1 = templateRepository.save(TemplateFixture.get(member, category));
            Template template2 = templateRepository.save(TemplateFixture.get(member, category));
            likesRepository.save(new Likes(template1, member));
            likesRepository.save(new Likes(template1, otherMember));
            likesRepository.save(new Likes(template2, member));

            likesRepository.deleteAllByTemplateIds(List.of(template1.getId(), template2.getId()));

            assertAll(
                    () -> assertThat(likesRepository.countByTemplate(template1)).isEqualTo(0),
                    () -> assertThat(likesRepository.countByTemplate(template2)).isEqualTo(0)
            );
        }
    }
}
