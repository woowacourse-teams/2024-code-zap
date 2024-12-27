package codezap.template.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.exception.CodeZapException;
import codezap.global.pagination.FixedPage;
import codezap.global.repository.RepositoryTest;
import codezap.likes.domain.Likes;
import codezap.likes.repository.LikesRepository;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.Template;

@RepositoryTest
class TemplateRepositoryTest {

    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LikesRepository likesRepository;

    @Test
    @DisplayName("카테고리 id로 템플릿 존재 여부 확인 ")
    void existsByCategoryId() {
        // given
        Member member = memberRepository.save(MemberFixture.getFirstMember());
        Member member2 = memberRepository.save(MemberFixture.getSecondMember());
        Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
        Category otherCategory = categoryRepository.save(CategoryFixture.getSecondCategory());
        templateRepository.save(new Template(member, "Template 1", "Description 1", category));

        assertAll(
                () -> assertThat(templateRepository.existsByCategoryId(category.getId())).isTrue(),
                () -> assertThat(templateRepository.existsByCategoryId(otherCategory.getId())).isFalse()
        );
    }

    @Nested
    @DisplayName("템플릿 id로 템플릿 조회")
    class FetchById {

        @Test
        @DisplayName("템플릿 id로 템플릿 조회 성공")
        void fetchById() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
            Template savedTemplate = templateRepository.save(TemplateFixture.get(member, category));

            assertThat(templateRepository.fetchById(savedTemplate.getId())).isEqualTo(savedTemplate);
        }

        @Test
        @DisplayName("템플릿 id로 템플릿 조회 실패: 존재하지 않는 id")
        void fetchById_WhenNotExistsId() {
            Long notSavedId = 1L;
            assertThatThrownBy(() -> templateRepository.fetchById(notSavedId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notSavedId + "에 해당하는 템플릿이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("회원이 좋아요한 템플릿 조회 테스트")
    class FindAllByMemberId {

        @Test
        @DisplayName("성공: 다른 사람의 private은 제외, 내 private은 포함")
        void findAllByMemberId() {
            // given
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Member otherMember = memberRepository.save(MemberFixture.getSecondMember());

            Category category = categoryRepository.save(CategoryFixture.get(member));
            Category otherCategory = categoryRepository.save(CategoryFixture.get(otherMember));

            Template myPublicTemplate = templateRepository.save(TemplateFixture.get(member, category));
            Template myPrivateTemplate = templateRepository.save(TemplateFixture.getPrivate(member, category));
            Template otherPublicTemplate = templateRepository.save(TemplateFixture.get(otherMember, otherCategory));
            Template otherPrivateTemplate = templateRepository.save(TemplateFixture.getPrivate(otherMember, otherCategory));

            likesRepository.save(new Likes(myPublicTemplate, member));
            likesRepository.save(new Likes(myPrivateTemplate, member));
            likesRepository.save(new Likes(otherPublicTemplate, member));
            likesRepository.save(new Likes(otherPrivateTemplate, member));

            // when
            FixedPage<Template> actual = templateRepository.findAllLikedByMemberId(member.getId(), PageRequest.of(0, 5));

            // then
            assertThat(actual.contents()).containsExactlyInAnyOrder(myPublicTemplate, myPrivateTemplate, otherPublicTemplate);
        }
    }
}
