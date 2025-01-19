package codezap.template.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.repository.CategoryRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.SourceCodeFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.exception.CodeZapException;
import codezap.global.repository.RepositoryTest;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.SourceCode;

@RepositoryTest
public class SourceCodeRepositoryTest {

    @Autowired
    private SourceCodeRepository sut;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TemplateRepository templateRepository;

    @Nested
    @DisplayName("ID로 소스코드 조회")
    class FetchById {

        @Test
        @DisplayName("성공: 소스코드 ID로 소스코드 조회")
        void fetchByIdSuccess() {
            var member1 = memberRepository.save(MemberFixture.getFirstMember());
            var category1 = categoryRepository.save(CategoryFixture.getDefaultCategory(member1));
            var template1 = templateRepository.save(TemplateFixture.get(member1, category1));
            var sourceCode1 = sut.save(SourceCodeFixture.get(template1, 1));
            var sourceCode2 = sut.save(SourceCodeFixture.get(template1, 2));

            var result = sut.fetchById(1L);

            assertThat(result).isEqualTo(sourceCode1);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 ID로 조회할 경우 예외 발생")
        void fetchByIdFailWithWrongId() {
            var id = 100L;
            assertThatThrownBy(() -> sut.fetchById(id))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessageContaining("식별자 " + id + "에 해당하는 소스 코드가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("템플릿으로 전체 소스코드 조회")
    class FindAllByTemplate {
        @Test
        @DisplayName("성공: 템플릿으로 전체 소스코드 조회")
        void findAllByTemplateSuccess() {
            var member1 = memberRepository.save(MemberFixture.getFirstMember());
            var category1 = categoryRepository.save(CategoryFixture.getDefaultCategory(member1));
            var template1 = templateRepository.save(TemplateFixture.get(member1, category1));
            var template2 = templateRepository.save(TemplateFixture.get(member1, category1));
            var sourceCodes = sut.saveAll(SourceCodeFixture.getList(template1, 2));
            var sourceCode2 = sut.save(SourceCodeFixture.get(template2, 1));

            var result = sut.findAllByTemplate(template1);

            assertThat(result).hasSize(2)
                    .hasSameElementsAs(sourceCodes);
        }

        @Test
        @DisplayName("성공: 소스코드가 없는 템플릿으로 조회하는 경우 빈 리스트가 반환된다.")
        void findAllByTemplateSuccessWithNoSourceCodeTemplate() {
            var member1 = memberRepository.save(MemberFixture.getFirstMember());
            var category1 = categoryRepository.save(CategoryFixture.getDefaultCategory(member1));
            var template1 = templateRepository.save(TemplateFixture.get(member1, category1));

            var result = sut.findAllByTemplate(template1);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("템플릿과 소스코드 순서로 조회")
    class FetchByTemplateAndOrdinal {
        @Test
        @DisplayName("성공: 템플릿과 소스코드 순서로 조회")
        void fetchByTemplateAndOrdinalSuccess() {
            var member1 = memberRepository.save(MemberFixture.getFirstMember());
            var category1 = categoryRepository.save(CategoryFixture.getDefaultCategory(member1));
            var template1 = templateRepository.save(TemplateFixture.get(member1, category1));
            var template2 = templateRepository.save(TemplateFixture.get(member1, category1));
            var sourceCode1 = sut.save(SourceCodeFixture.get(template1, 1));
            var sourceCode2 = sut.save(SourceCodeFixture.get(template1, 2));
            var sourceCode3 = sut.save(SourceCodeFixture.get(template2, 1));

            var result = sut.fetchByTemplateAndOrdinal(template1, 1);

            assertThat(result).isEqualTo(sourceCode1);
        }

        @Test
        @DisplayName("실패: 잘못된 순서로 조회하는 경우 예외가 발생")
        void fetchByTemplateAndOrdinalFailWithWrongOrdinal() {
            var member1 = memberRepository.save(MemberFixture.getFirstMember());
            var category1 = categoryRepository.save(CategoryFixture.getDefaultCategory(member1));
            var template1 = templateRepository.save(TemplateFixture.get(member1, category1));
            var sourceCode1 = sut.save(SourceCodeFixture.get(template1, 1));

            var ordinal = 100;
            assertThatThrownBy(() -> sut.fetchByTemplateAndOrdinal(template1, ordinal))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessageContaining("템플릿에 " + ordinal + "번째 소스 코드가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("템플릿과 순서에 해당하는 전체 소스코드 조회")
    class FindAllByTemplateAndOrdinal {

        @Test
        @DisplayName("성공: 템플릿과 순서에 해당하는 전체 소스코드 조회")
        void findAllByTemplateAndOrdinalSuccess() {
            var ordinal = 1;
            var member1 = memberRepository.save(MemberFixture.getFirstMember());
            var category1 = categoryRepository.save(CategoryFixture.getDefaultCategory(member1));
            var template1 = templateRepository.save(TemplateFixture.get(member1, category1));
            var template2 = templateRepository.save(TemplateFixture.get(member1, category1));
            var sourceCode1 = sut.save(SourceCodeFixture.get(template1, ordinal));
            var sourceCode2 = sut.save(SourceCodeFixture.get(template1, ordinal + 1));
            var sourceCode3 = sut.save(SourceCodeFixture.get(template2, ordinal));

            var result = sut.findAllByTemplateAndOrdinal(template1, ordinal);

            assertThat(result).hasSize(1)
                    .containsExactly(sourceCode1);
        }
    }

    @Nested
    @DisplayName("템플릿에 존재하는 소스코드 개수 조회")
    class CountByTemplate {

        @Test
        @DisplayName("성공: 템플릿에 존재하는 소스코드 개수 조회")
        void countByTemplateSuccess() {
            var member1 = memberRepository.save(MemberFixture.getFirstMember());
            var category1 = categoryRepository.save(CategoryFixture.getDefaultCategory(member1));
            var template1 = templateRepository.save(TemplateFixture.get(member1, category1));
            var template2 = templateRepository.save(TemplateFixture.get(member1, category1));
            List<SourceCode> sourceCodes = sut.saveAll(SourceCodeFixture.getList(template1, 2));
            sut.saveAll(SourceCodeFixture.getList(template2,  1));

            System.out.println(sourceCodes);
            var result = sut.countByTemplate(template1);

            assertThat(result).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("템플릿 ID로 템플릿에 존재하는 소스코드 삭제")
    class DeleteByTemplateId {
        @Test
        @DisplayName("성공: 템플릿 ID로 템플릿에 존재하는 소스코드 삭제")
        void testDeleteByTemplateId() {
            var member1 = memberRepository.save(MemberFixture.getFirstMember());
            var category1 = categoryRepository.save(CategoryFixture.getDefaultCategory(member1));
            var template1 = templateRepository.save(TemplateFixture.get(member1, category1));
            var template2 = templateRepository.save(TemplateFixture.get(member1, category1));
            sut.saveAll(SourceCodeFixture.getList(template1,  2));
            sut.saveAll(SourceCodeFixture.getList(template2,  1));

            sut.deleteAllByTemplateIds(List.of(1L));
            var result = sut.findAllByTemplate(template1);

            assertThat(result).isEmpty();
        }
    }
}
