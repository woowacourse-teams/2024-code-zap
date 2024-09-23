package codezap.template.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.DatabaseIsolation;
import codezap.likes.repository.LikesRepository;
import codezap.member.dto.MemberDto;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSourceCodeRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.repository.SourceCodeRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.ThumbnailRepository;

@SpringBootTest
@DatabaseIsolation
class MemberTemplateApplicationServiceTest {

    @Autowired
    MemberTemplateApplicationService sut;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TemplateRepository templateRepository;

    @Autowired
    SourceCodeRepository sourceCodeRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ThumbnailRepository thumbnailRepository;

    @Autowired
    LikesRepository likesRepository;

    @Nested
    class CreateTemplate {

        @Test
        @DisplayName("템플릿 생성 성공")
        void createTemplate() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var memberDto = MemberDto.from(member);
            var sourceCodeRequest = List.of(new CreateSourceCodeRequest("filename", "content", 1));
            var request = new CreateTemplateRequest(
                    "title",
                    "description",
                    sourceCodeRequest,
                    1,
                    category.getId(),
                    List.of());

            // when
            var actual = sut.createTemplate(memberDto, request);

            // then
            assertThat(actual).isEqualTo(1L);
        }
    }

    @Disabled("TemplateApplicationServiceTest.GetAllTagsByMemberId 테스트와 완전히 동일하므로 생략합니다.")
    @Nested
    class GetAllTagsByMemberId {

        @Test
        @DisplayName("사용자ID로 모든 태그 조회 성공")
        void getAllTagsByMemberId() {
        }
    }

    @Nested
    class GetAllTemplatesBy {

        @ParameterizedTest
        @MethodSource
        @DisplayName("사용자ID, 검색어, 카테고리ID, 태그 ID 목록으로 검색 성공")
        void getAllTemplatesBy(
                Long memberId,
                String keyword,
                Long categoryId,
                List<Long> tagIds,
                Pageable pageable
        ) {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template = templateRepository.save(new Template(member, "title1", "description", category));
            var sourceCode = sourceCodeRepository.save(new SourceCode(template, "filename1", "content", 1));
            thumbnailRepository.save(new Thumbnail(template, sourceCode));

            // when & then
            assertThatCode(() -> sut.getAllTemplatesBy(memberId, keyword, categoryId, tagIds, pageable))
                    .doesNotThrowAnyException();
        }

        static Stream<Arguments> getAllTemplatesBy() {
            return Stream.of(
                    Arguments.of(null, null, null, null, Pageable.ofSize(1)),
                    Arguments.of(1L, null, null, null, Pageable.ofSize(1)),
                    Arguments.of(null, "keyword", null, null, Pageable.ofSize(1)),
                    Arguments.of(null, null, 1L, null, Pageable.ofSize(1)),
                    Arguments.of(null, null, null, List.of(1L, 2L), Pageable.ofSize(1)),
                    Arguments.of(null, null, null, null, Pageable.ofSize(2))
            );
        }
    }

    @Nested
    @DisplayName("ID로 템플릿 조회 (비회원)")
    class GetTemplateById {

        @Test
        @DisplayName("성공")
        void getTemplateById() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template = templateRepository.save(TemplateFixture.get(member, category));

            // when
            var actual = sut.getTemplateById(template.getId());

            // then
            assertAll(

                    () -> assertThat(actual.id()).isEqualTo(1L),
                    () -> assertThat(actual.isLiked()).isFalse()
            );
        }
    }

    @Nested
    @DisplayName("ID로 템플릿 조회 (회원)")
    class GetTemplateByIdWithMember {

        @Test
        @DisplayName("성공: 좋아요를 했을 때")
        void getTemplateByIdWithMemberLikes() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template = templateRepository.save(TemplateFixture.get(member, category));
            likesRepository.save(template.like(member));

            // when
            var actual = sut.getTemplateByIdWithMember(template.getId(), MemberDto.from(member));

            // then
            assertAll(
                    () -> assertThat(actual.id()).isEqualTo(1L),
                    () -> assertThat(actual.isLiked()).isTrue()
            );
        }

        @Test
        @DisplayName("성공: 좋아요를 하지 않았을 때")
        void getTemplateByIdWithMemberNoLikes() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template = templateRepository.save(TemplateFixture.get(member, category));

            // when
            var actual = sut.getTemplateByIdWithMember(template.getId(), MemberDto.from(member));

            // then
            assertAll(
                    () -> assertThat(actual.id()).isEqualTo(1L),
                    () -> assertThat(actual.isLiked()).isFalse()
            );
        }
    }

    @Nested
    class Update {

        @Test
        @DisplayName("템플릿 업데이트 성공")
        void update() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var memberDto = MemberDto.from(member);
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template = templateRepository.save(new Template(member, "title1", "description", category));
            var sourceCode1 = sourceCodeRepository.save(new SourceCode(template, "filename1", "content1", 1));
            var sourceCode2 = sourceCodeRepository.save(new SourceCode(template, "filename2", "content2", 2));
            thumbnailRepository.save(new Thumbnail(template, sourceCode1));
            var createRequest = List.of(new CreateSourceCodeRequest("filename3", "content3", 3));
            var updateRequest1 = new UpdateSourceCodeRequest(
                    sourceCode1.getId(),
                    sourceCode1.getFilename(),
                    sourceCode1.getContent(),
                    sourceCode1.getOrdinal());
            var updateRequest2 = new UpdateSourceCodeRequest(
                    sourceCode2.getId(),
                    sourceCode2.getFilename(),
                    sourceCode2.getContent(),
                    sourceCode2.getOrdinal());
            var updateRequest = List.of(updateRequest1, updateRequest2);
            List<Long> deleteIds = List.of();
            var request = new UpdateTemplateRequest(
                    "title1",
                    "description1",
                    createRequest,
                    updateRequest,
                    deleteIds,
                    category.getId(),
                    List.of());

            // when & then
            assertThatCode(() -> sut.update(memberDto, template.getId(), request))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class DeleteByIds {

        @Test
        @DisplayName("사용자 정보와 템플릿 ID로 템플릿 삭제 성공")
        void deleteByMemberAndIds() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var memberDto = MemberDto.from(member);
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template1 = templateRepository.save(new Template(member, "title1", "description", category));
            var sourceCode1 = sourceCodeRepository.save(new SourceCode(template1, "filename1", "content1", 1));
            var template2 = templateRepository.save(new Template(member, "title2", "description", category));
            var sourceCode2 = sourceCodeRepository.save(new SourceCode(template2, "filename2", "content2", 2));
            var template3 = templateRepository.save(new Template(member, "title3", "description", category));
            var sourceCode3 = sourceCodeRepository.save(new SourceCode(template3, "filename3", "content3", 3));

            var deleteIds = List.of(1L, 2L);

            // when
            sut.deleteByIds(memberDto, deleteIds);

            // then
            var actualTemplatesLeft = templateRepository.findAll();
            var actualSourceCodeLeft = sourceCodeRepository.findAll();

            assertAll(
                    () -> assertThat(actualTemplatesLeft).containsExactly(template3),
                    () -> assertThat(actualSourceCodeLeft).containsExactly(sourceCode3)
            );
        }
    }

}
