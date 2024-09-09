package codezap.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryJpaRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.global.exception.CodeZapException;
import codezap.global.repository.JpaRepositoryTest;
import codezap.member.domain.Member;
import codezap.template.domain.Template;
import codezap.template.repository.TemplateJpaRepository;

@JpaRepositoryTest
class MemberJpaRepositoryTest {

    private final MemberJpaRepository memberJpaRepository;
    private final TemplateJpaRepository templateJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    @Autowired
    MemberJpaRepositoryTest(
            MemberJpaRepository memberJpaRepository,
            TemplateJpaRepository templateJpaRepository,
            CategoryJpaRepository categoryJpaRepository
    ) {
        this.memberJpaRepository = memberJpaRepository;
        this.templateJpaRepository = templateJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Nested
    @DisplayName("id로 Member 조회")
    class fetchById {
        @Test
        @DisplayName("성공 : id로 Member를 알아낼 수 있다.")
        void fetchByIdSuccess() {
            Member member = MemberFixture.getFirstMember();
            member = ((JpaRepository<Member, Long>) memberJpaRepository).save(member);

            Member actual = memberJpaRepository.fetchById(member.getId());

            assertThat(actual).isEqualTo(member);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 id인 경우 에러가 발생한다.")
        void fetchByIdFailByNotExistsId() {
            long id = 100;

            assertThatThrownBy(() -> memberJpaRepository.fetchById(id))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + id + "에 해당하는 멤버가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("아이디로 Member 조회")
    class fetchByName {
        @Test
        @DisplayName("성공 : 아이디로 Member를 알아낼 수 있다.")
        void fetchByNameSuccess() {
            Member member = MemberFixture.getFirstMember();
            member = ((JpaRepository<Member, Long>) memberJpaRepository).save(member);

            Member actual = memberJpaRepository.fetchByName(member.getName());

            assertThat(actual).isEqualTo(member);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 아이디인 경우 에러가 발생한다.")
        void fetchByNameFailByNotExistsId() {
            String name = "켬미";

            assertThatThrownBy(() -> memberJpaRepository.fetchByName(name))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("존재하지 않는 아이디 " + name + " 입니다.");
        }
    }

    @Nested
    @DisplayName("아이디로 Member 조회")
    class findByName {
        @Test
        @DisplayName("성공 : 아이디로 Member를 알아낼 수 있다.")
        void findByNameSuccess() {
            Member member = MemberFixture.getFirstMember();
            member = ((JpaRepository<Member, Long>) memberJpaRepository).save(member);

            Optional<Member> actual = memberJpaRepository.findByName(member.getName());

            assertThat(actual).isEqualTo(Optional.of(member));
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 아이디인 경우 optional 값이 반환된다.")
        void findByNameFailByNotExistsId() {
            Optional<Member> actual = memberJpaRepository.findByName("kyummi");

            assertThat(actual).isEmpty();
        }
    }

    @Nested
    @DisplayName("템플릿 id로 Member 조회")
    class fetchByTemplateId {
        @Test
        @DisplayName("성공 : 템플릿 id로 Member를 알아낼 수 있다.")
        void fetchByTemplateIdSuccess() {
            Member member = ((JpaRepository<Member, Long>) memberJpaRepository).save(MemberFixture.getFirstMember());
            Category category = ((JpaRepository<Category, Long>) categoryJpaRepository).save(
                    CategoryFixture.getFirstCategory());
            Template template = new Template(member, "title", "description", category);
            template = ((JpaRepository<Template, Long>) templateJpaRepository).save(template);

            Member actual = memberJpaRepository.fetchByTemplateId(template.getId());

            assertThat(actual).isEqualTo(member);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 템플릿 id인 경우 에러가 발생한다.")
        void fetchByTemplateIdFailByNotExistsId() {
            long id = 100;

            assertThatThrownBy(() -> memberJpaRepository.fetchByTemplateId(id))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("템플릿에 대한 멤버가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("템플릿 id로 Member 조회")
    class findByTemplateId {
        @Test
        @DisplayName("성공 : 템플릿 id로 Member를 알아낼 수 있다.")
        void findByTemplateIdSuccess() {
            Member member = ((JpaRepository<Member, Long>) memberJpaRepository).save(MemberFixture.getFirstMember());
            Category category = ((JpaRepository<Category, Long>) categoryJpaRepository).save(
                    CategoryFixture.getFirstCategory());
            Template template = new Template(member, "title", "description", category);
            template = ((JpaRepository<Template, Long>) templateJpaRepository).save(template);

            Optional<Member> actual = memberJpaRepository.findByTemplateId(template.getId());

            assertThat(actual).isEqualTo(Optional.of(member));
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 템플릿 id인 경우 optional 값이 반환된다.")
        void findByTemplateIdFailByNotExistsId() {
            Optional<Member> actual = memberJpaRepository.findByTemplateId(100L);

            assertThat(actual).isEmpty();
        }
    }

    @Nested
    @DisplayName("아이디가 존재 여부")
    class existsByName {
        @Test
        @DisplayName("성공 : 해당 아이디가 존재하면 true를 반환한다.")
        void existsByNameReturnTrue() {
            Member member = ((JpaRepository<Member, Long>) memberJpaRepository).save(MemberFixture.getFirstMember());

            boolean actual = memberJpaRepository.existsByName(member.getName());

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("성공 : 해당 아이디가 존재하면 false를 반환한다.")
        void existsByNameReturnFalse() {
            boolean actual = memberJpaRepository.existsByName("kkk");

            assertThat(actual).isFalse();
        }
    }
}
