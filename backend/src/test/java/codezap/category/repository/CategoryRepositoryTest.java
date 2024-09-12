package codezap.category.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.global.exception.CodeZapException;
import codezap.global.repository.JpaRepositoryTest;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;

@JpaRepositoryTest
public class CategoryRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository sut;

    @Nested
    @DisplayName("Id로 카테고리 조회")
    class FetchById {

        @Test
        @DisplayName("성공: id로 카테고리를 조회할 수 있다.")
        void fetchByIdSuccess() {
            memberRepository.save(MemberFixture.getFirstMember());
            var category = sut.save(CategoryFixture.getFirstCategory());

            var actual = sut.fetchById(category.getId());

            assertThat(actual).isEqualTo(category);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 id로 조회할 경우 예외 발생")
        void fetchByIdFail() {
            var notExistId = 100L;
            assertThatThrownBy(() -> sut.fetchById(notExistId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notExistId + "에 해당하는 카테고리가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("회원의 모든 카테고리를 오름차순으로 조회")
    class FindAllByMemberOrderById {

        @Test
        @DisplayName("성공: 회원의 모든 카테고리를 id 오름차순으로 조회할 수 있다.")
        void findAllByMemberOrderByIdSuccess() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category1 = sut.save(new Category("category1", member));
            var category2 = sut.save(new Category("category2", member));
            var category3 = sut.save(new Category("category3", member));
            var category4 = sut.save(new Category("category4", member));
            var category5 = sut.save(new Category("category5", member));

            var actual = sut.findAllByMemberOrderById(member);

            assertThat(actual).containsExactly(category1, category2, category3, category4, category5);
        }
    }

    @Nested
    @DisplayName("카테고리 이름과 회원으로 해당 카테고리가 존재하는지 조회")
    class ExistsByNameAndMember {

        @Test
        @DisplayName("성공: 카테고리 이름과 회원으로 해당 카테고리가 존재하는지 조회")
        void existsByNameAndMemberSuccess() {
            var member = new Member("Zappy", "password", "salt");
            memberRepository.save(member);
            var category1 = new Category("category1", member);
            sut.save(category1);

            var actual = sut.existsByNameAndMember("category1", member);

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("실패: 일치하는 카테고리가 없을 경우")
        void existsByNameAndMemberFail() {
            var member = new Member("Zappy", "password", "salt");
            memberRepository.save(member);
            var category1 = new Category("category1", member);
            sut.save(category1);

            var actual = sut.existsByNameAndMember("category2", member);

            assertThat(actual).isFalse();
        }
    }
}
