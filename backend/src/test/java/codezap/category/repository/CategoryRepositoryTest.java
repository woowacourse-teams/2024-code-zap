package codezap.category.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import codezap.category.domain.Category;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.global.exception.CodeZapException;
import codezap.global.repository.JpaRepositoryTest;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;

@Import(CategoryRepository.class)
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
        @DisplayName("Id로 카테고리 조회 성공")
        void fetchByIdSuccess() {
            memberRepository.save(MemberFixture.getFirstMember());
            var category = sut.save(CategoryFixture.getFirstCategory());

            var actual = sut.fetchById(category.getId());

            assertThat(actual).isEqualTo(category);
        }

        @Test
        @DisplayName("Id로 카테고리 조회 실패: 존재하지 않는 id")
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
        @DisplayName("회원의 모든 카테고리를 오름차순으로 조회 성공")
        void findAllByMemberIdOrderByIdSuccess() {
            // given
            var member1 = memberRepository.save(MemberFixture.getFirstMember());
            var category1 = sut.save(new Category("category1", member1));
            var category3 = sut.save(new Category("category3", member1));
            var category5 = sut.save(new Category("category5", member1));

            var member2 = memberRepository.save(MemberFixture.getSecondMember());
            var category2 = sut.save(new Category("category2", member2));
            var category4 = sut.save(new Category("category4", member2));

            // when
            var actual1 = sut.findAllByMemberIdOrderById(member1.getId());
            var actual2 = sut.findAllByMemberIdOrderById(member2.getId());

            // then
            assertAll(
                    () -> assertThat(actual1).containsExactly(category1, category3, category5),
                    () -> assertThat(actual2).containsExactly(category2, category4)
            );
        }
    }

    @Nested
    @DisplayName("카테고리 이름과 회원으로 해당 카테고리가 존재하는지 조회")
    class ExistsByNameAndMember {

        @Test
        @DisplayName("카테고리 이름과 회원으로 해당 카테고리가 존재하는지 조회 성공")
        void existsByNameAndMemberSuccess() {
            var member = new Member("Zappy", "password", "salt");
            memberRepository.save(member);
            var category1 = new Category("category1", member);
            sut.save(category1);

            var actual = sut.existsByNameAndMember("category1", member);

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("카테고리 이름과 회원으로 해당 카테고리가 존재하는지 조회 성공: 일치하는 카테고리 없음")
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
