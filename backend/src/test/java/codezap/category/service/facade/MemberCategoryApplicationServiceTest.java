package codezap.category.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import codezap.category.domain.Category;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.CreateCategoryResponse;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.category.dto.response.FindCategoryResponse;
import codezap.category.repository.CategoryRepository;
import codezap.global.DatabaseIsolation;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.fixture.MemberFixture;
import codezap.member.repository.MemberRepository;

@SpringBootTest
@DatabaseIsolation
class MemberCategoryApplicationServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberCategoryApplicationService sut;

    @Nested
    @DisplayName("카테고리 생성")
    class Create {
        @Test
        @DisplayName("카테고리 생성 성공")
        void createSuccess() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            MemberDto memberDto = MemberDto.from(member);
            String categoryName = "카테고리 1";
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest(categoryName);

            CreateCategoryResponse response = sut.create(memberDto, createCategoryRequest);

            Category category = categoryRepository.fetchById(response.id());
            assertAll(
                    () -> assertThat(response).isEqualTo(CreateCategoryResponse.from(category)),
                    () -> assertThat(response.name()).isEqualTo(categoryName)
            );
        }

        @Test
        @DisplayName("카테고리 생성 실패 : 멤버 존재하지 않음")
        void createFailNotExistsMember() {
            MemberDto memberDto = MemberDto.from(MemberFixture.memberFixture());
            String categoryName = "카테고리 1";
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest(categoryName);

            assertThatThrownBy(() -> sut.create(memberDto, createCategoryRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + memberDto.id() + "에 해당하는 멤버가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("카테고리 생성 실패 : 카테고리 이름 중복")
        void createFailDuplicatedCategory() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            MemberDto memberDto = MemberDto.from(member);
            String categoryName = "카테고리 1";
            categoryRepository.save(new Category(categoryName, member));
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest(categoryName);

            assertThatThrownBy(() -> sut.create(memberDto, createCategoryRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("이름이 " + categoryName + "인 카테고리가 이미 존재합니다.");
        }
    }

    @Nested
    @DisplayName("멤버에 따른 카테고리 조회")
    class FindAllByMember {
        @Test
        @DisplayName("멤버에 따른 카테고리 조회 성공")
        void findAllByMemberSuccess() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Member otherMember = memberRepository.save(MemberFixture.createFixture("gugu"));
            Category category1 = categoryRepository.save(new Category("카테고리 1", member));
            Category category2 = categoryRepository.save(new Category("카테고리 2", member));
            Category category3 = categoryRepository.save(new Category("카테고리 3", otherMember));

            FindAllCategoriesResponse response = sut.findAllByMember(member.getId());

            assertThat(response.categories()).hasSize(2)
                    .containsExactly(FindCategoryResponse.from(category1), FindCategoryResponse.from(category2))
                    .doesNotContain(FindCategoryResponse.from(category3));
        }

        @Test
        @DisplayName("멤버에 따른 카테고리 조회 실패 : 멤버 존재하지 않음")
        void findAllByMemberFailNotExistsMember() {
            Long notExistsId = 100L;

            assertThatThrownBy(() -> sut.findAllByMember(notExistsId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notExistsId + "에 해당하는 멤버가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("카테고리 수정")
    class Update {
        @Test
        @DisplayName("카테고리 수정 성공")
        void updateSuccess() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            MemberDto memberDto = MemberDto.from(member);
            Category category = categoryRepository.save(new Category("카테고리 1", member));
            String renameCategoryName = "카테고리 수정";
            UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest(renameCategoryName);

            sut.update(memberDto, category.getId(), updateCategoryRequest);

            Category actual = categoryRepository.fetchById(category.getId());
            assertAll(
                    () -> assertThat(actual).isEqualTo(category),
                    () -> assertThat(actual.getName()).isEqualTo(renameCategoryName)
            );
        }

        @Test
        @DisplayName("카테고리 수정 실패 : 멤버 존재하지 않음")
        void updateFailNotExistsMember() {
            MemberDto memberDto = MemberDto.from(MemberFixture.memberFixture());
            String renameCategoryName = "카테고리 수정";
            UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest(renameCategoryName);

            assertThatThrownBy(() -> sut.update(memberDto, 1L, updateCategoryRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + memberDto.id() + "에 해당하는 멤버가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("카테고리 수정 실패 : 카테고리 이름 중복")
        void updateFailDuplicatedCategory() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            MemberDto memberDto = MemberDto.from(member);
            Category category = categoryRepository.save(new Category("카테고리 1", member));
            categoryRepository.save(new Category("카테고리 2", member));
            String renameCategoryName = "카테고리 2";
            UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest(renameCategoryName);
            Long notExistsId = category.getId() + 1;

            assertThatThrownBy(() -> sut.update(memberDto, notExistsId, updateCategoryRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("이름이 " + renameCategoryName + "인 카테고리가 이미 존재합니다.");
        }

        @Test
        @DisplayName("카테고리 수정 실패 : 카테고리가 존재하지 않음")
        void updateFailNotExistsCategory() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            MemberDto memberDto = MemberDto.from(member);
            String renameCategoryName = "카테고리 수정";
            UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest(renameCategoryName);
            Long notExistsId = 100L;

            assertThatThrownBy(() -> sut.update(memberDto, notExistsId, updateCategoryRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notExistsId + "에 해당하는 카테고리가 존재하지 않습니다.");
        }
    }
}
