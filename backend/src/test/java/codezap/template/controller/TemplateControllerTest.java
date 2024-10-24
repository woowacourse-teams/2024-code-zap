package codezap.template.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.SourceCodeFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.MockMvcTest;
import codezap.member.domain.Member;
import codezap.tag.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.Visibility;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSourceCodeRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTemplateItemResponse;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindTemplateResponse;

@Import(TemplateController.class)
class TemplateControllerTest extends MockMvcTest {

    private static final int MAX_LENGTH = 255;
    private static final int MAX_CONTENT_LENGTH = 65535;

    @Nested
    @DisplayName("템플릿 생성 테스트")
    class CreateTemplateTest {

        @Test
        @DisplayName("템플릿 생성 성공")
        void createTemplateSuccess() throws Exception {
            CreateTemplateRequest templateRequest = createValidTemplateRequest();

            mvc.perform(post("/templates")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(templateRequest)))
                    .andExpect(status().isCreated());
        }

        private static CreateTemplateRequest createValidTemplateRequest() {
            return new CreateTemplateRequest(
                    "Valid Title",
                    "Valid Description",
                    List.of(new CreateSourceCodeRequest("validFileName.txt", "Valid Content", 1)),
                    1,
                    1L,
                    List.of("tag1", "tag2"),
                    Visibility.PUBLIC
            );
        }

        @ParameterizedTest
        @MethodSource("invalidTemplateData")
        @DisplayName("템플릿 생성 실패: 문자열 잘못된 형식인 경우 400 반환")
        void createTemplateFailWithInvalidInput(CreateTemplateRequest request, String expectedError) throws Exception {
            mvc.perform(post("/templates")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value(expectedError));
        }

        private static Stream<Arguments> invalidTemplateData() {
            return Stream.of(
                    Arguments.of(createRequestWithInvalidTitle(""), "템플릿명이 비어 있거나 공백입니다."),
                    Arguments.of(createRequestWithInvalidTitle("a".repeat(MAX_LENGTH + 1)), "템플릿명은 최대 255자까지 입력 가능합니다."),
                    Arguments.of(createRequestWithInvalidDescription(null), "템플릿 설명이 null 입니다."),
                    Arguments.of(createRequestWithInvalidDescription("a".repeat(MAX_CONTENT_LENGTH + 1)), "템플릿 설명은 최대 65,535 Byte까지 입력 가능합니다."),
                    Arguments.of(createRequestWithInvalidFileName(""), "파일명이 비어 있거나 공백입니다."),
                    Arguments.of(createRequestWithInvalidFileName("a".repeat(MAX_LENGTH + 1)), "파일명은 최대 255자까지 입력 가능합니다."),
                    Arguments.of(createRequestWithInvalidSourceCode(""), "소스 코드가 비어 있거나 공백입니다."),
                    Arguments.of(createRequestWithInvalidSourceCode("a".repeat(MAX_CONTENT_LENGTH + 1)), "소스 코드는 최대 65,535 Byte까지 입력 가능합니다."),
                    Arguments.of(createRequestWithInvalidSourceCode("ㄱ".repeat(MAX_CONTENT_LENGTH / 3 + 1)), "소스 코드는 최대 65,535 Byte까지 입력 가능합니다."),
                    Arguments.of(createRequestWithNoSourceCodes(), "소스 코드 최소 1개 입력해야 합니다."),
                    Arguments.of(createRequestWithNullThumbnail(), "썸네일 순서가 null 입니다."),
                    Arguments.of(createRequestWithNullCategoryId(), "카테고리 ID가 null 입니다."),
                    Arguments.of(createTemplateRequestWithNullTags(), "태그 목록이 null 입니다."),
                    Arguments.of(createRequestWithLongTag(), "태그 명은 최대 30자까지 입력 가능합니다."),
                    Arguments.of(createRequestWithNullVisibility(), "템플릿 공개 여부가 null 입니다.")
            );
        }

        private static CreateTemplateRequest createRequestWithInvalidTitle(String invalidTitle) {
            CreateTemplateRequest validRequest = createValidTemplateRequest();
            return new CreateTemplateRequest(
                    invalidTitle,
                    validRequest.description(),
                    validRequest.sourceCodes(),
                    validRequest.thumbnailOrdinal(),
                    validRequest.categoryId(),
                    validRequest.tags(),
                    validRequest.visibility());
        }

        private static CreateTemplateRequest createRequestWithInvalidDescription(String invalidDescription) {
            CreateTemplateRequest validRequest = createValidTemplateRequest();
            return new CreateTemplateRequest(
                    validRequest.title(),
                    invalidDescription,
                    validRequest.sourceCodes(),
                    validRequest.thumbnailOrdinal(),
                    validRequest.categoryId(),
                    validRequest.tags(),
                    validRequest.visibility());
        }

        private static CreateTemplateRequest createRequestWithInvalidFileName(String invalidFileName) {
            CreateTemplateRequest validRequest = createValidTemplateRequest();
            List<CreateSourceCodeRequest> invalidSourceCodes = List.of(
                    new CreateSourceCodeRequest(invalidFileName, "Valid Content", 1)
            );
            return new CreateTemplateRequest(
                    validRequest.title(),
                    validRequest.description(),
                    invalidSourceCodes,
                    validRequest.thumbnailOrdinal(),
                    validRequest.categoryId(),
                    validRequest.tags(),
                    validRequest.visibility());
        }

        private static CreateTemplateRequest createRequestWithInvalidSourceCode(String invalidSourceCode) {
            CreateTemplateRequest validRequest = createValidTemplateRequest();
            List<CreateSourceCodeRequest> invalidSourceCodes = List.of(
                    new CreateSourceCodeRequest("ValidFileName.java", invalidSourceCode, 1)
            );
            return new CreateTemplateRequest(
                    validRequest.title(),
                    validRequest.description(),
                    invalidSourceCodes,
                    validRequest.thumbnailOrdinal(),
                    validRequest.categoryId(),
                    validRequest.tags(),
                    validRequest.visibility());
        }

        private static CreateTemplateRequest createRequestWithNoSourceCodes() {
            CreateTemplateRequest validRequest = createValidTemplateRequest();
            return new CreateTemplateRequest(
                    validRequest.title(),
                    validRequest.description(),
                    List.of(),
                    validRequest.thumbnailOrdinal(),
                    validRequest.categoryId(),
                    validRequest.tags(),
                    validRequest.visibility());
        }

        private static CreateTemplateRequest createRequestWithNullCategoryId() {
            CreateTemplateRequest validRequest = createValidTemplateRequest();
            return new CreateTemplateRequest(
                    validRequest.title(),
                    validRequest.description(),
                    validRequest.sourceCodes(),
                    validRequest.thumbnailOrdinal(),
                    null,
                    validRequest.tags(),
                    validRequest.visibility());
        }

        private static CreateTemplateRequest createRequestWithNullThumbnail() {
            CreateTemplateRequest validRequest = createValidTemplateRequest();
            return new CreateTemplateRequest(
                    validRequest.title(),
                    validRequest.description(),
                    validRequest.sourceCodes(),
                    null,
                    validRequest.categoryId(),
                    validRequest.tags(),
                    validRequest.visibility());
        }

        private static CreateTemplateRequest createTemplateRequestWithNullTags() {
            CreateTemplateRequest validRequest = createValidTemplateRequest();
            return new CreateTemplateRequest(
                    validRequest.title(),
                    validRequest.description(),
                    validRequest.sourceCodes(),
                    validRequest.thumbnailOrdinal(),
                    validRequest.categoryId(),
                    null,
                    validRequest.visibility());
        }

        private static CreateTemplateRequest createRequestWithLongTag() {
            CreateTemplateRequest validRequest = createValidTemplateRequest();
            return new CreateTemplateRequest(
                    validRequest.title(),
                    validRequest.description(),
                    validRequest.sourceCodes(),
                    validRequest.thumbnailOrdinal(),
                    validRequest.categoryId(),
                    List.of("ㄱ".repeat(31)),
                    validRequest.visibility());
        }

        private static CreateTemplateRequest createRequestWithNullVisibility() {
            CreateTemplateRequest validRequest = createValidTemplateRequest();
            return new CreateTemplateRequest(
                    validRequest.title(),
                    validRequest.description(),
                    validRequest.sourceCodes(),
                    validRequest.thumbnailOrdinal(),
                    validRequest.categoryId(),
                    validRequest.tags(),
                    null);
        }

        @ParameterizedTest
        @DisplayName("템플릿 생성 실패: 잘못된 소스 코드 순서 입력 시 400 반환")
        @CsvSource({"0, 1", "1, 3", "2, 1"})
        void createTemplateFailWithWrongSourceCodeOrdinal(int firstIndex, int secondIndex) throws Exception {
            CreateTemplateRequest templateRequest = new CreateTemplateRequest("title", "description",
                    List.of(new CreateSourceCodeRequest("title", "content", firstIndex),
                            new CreateSourceCodeRequest("title", "content", secondIndex)), 1, 1L,
                    List.of("tag1", "tag2"),
                    Visibility.PUBLIC);

            mvc.perform(post("/templates")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(templateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("소스 코드 순서가 잘못되었습니다."));
        }
    }

    @Nested
    class FindAllTemplatesSuccess {

        @Test
        @DisplayName("템플릿 검색 성공")
        void findAllTemplatesSuccess() throws Exception {
            // given
            FindAllTemplateItemResponse findAllTemplateItemResponse = getFindAllTemplateItemResponse();

            when(templateApplicationService.findAllBy(any(), any(), any(), any(), any(), any())).thenReturn(
                    new FindAllTemplatesResponse(1, 1, List.of(findAllTemplateItemResponse)));

            // when & then
            mvc.perform(get("/templates")
                            .param("memberId", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.templates.size()").value(1))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(jsonPath("$.totalElements").value(1));
        }

        @Test
        @DisplayName("템플릿 검색 성공: 로그인한 사용자가 없는 경우도 조회 가능")
        void findAllTemplatesSuccessWhenNoMember() throws Exception {
            // given
            FindAllTemplateItemResponse findAllTemplateItemResponse = getFindAllTemplateItemResponse();

            when(credentialManager.hasCredential(any())).thenReturn(false);
            when(credentialManager.getCredential(any())).thenReturn(null);
            when(templateApplicationService.findAllBy(any(), any(), any(), any(), any())).thenReturn(
                    new FindAllTemplatesResponse(1, 1, List.of(findAllTemplateItemResponse)));

            // when & then
            mvc.perform(get("/templates")
                            .param("memberId", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.templates.size()").value(1))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(jsonPath("$.totalElements").value(1));
        }

        private FindAllTemplateItemResponse getFindAllTemplateItemResponse() {
            Template template = TemplateFixture.get(MemberFixture.getFirstMember(), CategoryFixture.getFirstCategory());
            return FindAllTemplateItemResponse.of(
                    template,
                    List.of(new Tag(1L, "tag1")),
                    SourceCodeFixture.get(template, 1),
                    true);
        }
    }

    @Nested
    @DisplayName("템플릿 단건 조회 테스트")
    class findTemplateTest {

        @Test
        @DisplayName("템플릿 단건 조회 성공")
        void findOneTemplateSuccess() throws Exception {
            // given
            Template template = TemplateFixture.get(MemberFixture.getFirstMember(), CategoryFixture.getFirstCategory());
            FindTemplateResponse findTemplateResponse = FindTemplateResponse.of(
                    template,
                    List.of(SourceCodeFixture.get(template, 1)),
                    List.of(new Tag(1L, "tag1")),
                    true);

            when(templateApplicationService.findById(anyLong(), any())).thenReturn(findTemplateResponse);

            // when & then
            mvc.perform(get("/templates/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.category.id").value(1));
        }

        @Test
        @DisplayName("템플릿 단건 조회 성공: 로그인한 사용자가 없는 경우도 조회 가능")
        void findOneTemplateSuccessWithNoMember() throws Exception {
            // given
            Template template = TemplateFixture.get(MemberFixture.getFirstMember(), CategoryFixture.getFirstCategory());
            FindTemplateResponse findTemplateResponse = FindTemplateResponse.of(
                    template,
                    List.of(SourceCodeFixture.get(template, 1)),
                    List.of(new Tag(1L, "tag1")),
                    true);

            when(credentialManager.hasCredential(any())).thenReturn(false);
            when(credentialManager.getCredential(any())).thenReturn(null);
            when(templateApplicationService.findById(1L)).thenReturn(findTemplateResponse);

            // when & then
            mvc.perform(get("/templates/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.category.id").value(1));
        }
    }

    @Nested
    @DisplayName("템플릿 수정 테스트")
    class updateTemplateTest {

        @Test
        @DisplayName("템플릿 수정 성공")
        void updateTemplateSuccess() throws Exception {
            UpdateTemplateRequest updateTemplateRequest = createValidUpdateTemplateRequest();

            mvc.perform(post("/templates/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateTemplateRequest)))
                    .andExpect(status().isOk());
        }

        private static UpdateTemplateRequest createValidUpdateTemplateRequest() {
            return new UpdateTemplateRequest(
                    "updateTitle",
                    "description",
                    List.of(new CreateSourceCodeRequest("filename3", "content3", 2)),
                    List.of(new UpdateSourceCodeRequest(2L, "updateFilename2", "updateContent2", 1)),
                    List.of(1L),
                    1L,
                    List.of("tag1", "tag3"),
                    Visibility.PUBLIC
            );
        }

        @ParameterizedTest
        @MethodSource("invalidUpdateTemplateData")
        @DisplayName("템플릿 수정 실패: 문자열 잘못된 형식인 경우 400 반환")
        void updateTemplateFailWithInvalidInput(UpdateTemplateRequest request, String expectedError) throws Exception {

            mvc.perform(post("/templates/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value(expectedError))
                    .andExpect(jsonPath("$.errorCode").value("1101"));
        }

        private static Stream<Arguments> invalidUpdateTemplateData() {
            return Stream.of(
                    Arguments.of(createUpdateRequestWithInvalidTitle(""), "템플릿명이 비어 있거나 공백입니다."),
                    Arguments.of(createUpdateRequestWithInvalidTitle("a".repeat(MAX_LENGTH + 1)),
                            "템플릿명은 최대 255자까지 입력 가능합니다."),
                    Arguments.of(createUpdateRequestWithInvalidDescription(null), "템플릿 설명이 null 입니다."),
                    Arguments.of(createUpdateRequestWithInvalidDescription("a".repeat(MAX_CONTENT_LENGTH + 1)),
                            "템플릿 설명은 최대 65,535 Byte까지 입력 가능합니다."),
                    Arguments.of(createUpdateRequestWithInvalidFileName(""), "파일명이 비어 있거나 공백입니다."),
                    Arguments.of(createUpdateRequestWithInvalidFileName("a".repeat(MAX_LENGTH + 1)),
                            "파일명은 최대 255자까지 입력 가능합니다."),
                    Arguments.of(createUpdateRequestWithInvalidSourceCode(""), "소스 코드가 비어 있거나 공백입니다."),
                    Arguments.of(createUpdateRequestWithInvalidSourceCode("a".repeat(MAX_CONTENT_LENGTH + 1)),
                            "소스 코드는 최대 65,535 Byte까지 입력 가능합니다."),
                    Arguments.of(createUpdateRequestWithInvalidSourceCode("ㄱ".repeat(MAX_CONTENT_LENGTH / 3 + 1)),
                            "소스 코드는 최대 65,535 Byte까지 입력 가능합니다."),
                    Arguments.of(createUpdateRequestWithNullCreateSourceCodes(), "추가하는 소스 코드 목록이 null 입니다."),
                    Arguments.of(createUpdateRequestWithNullUpdateSourceCodes(),
                            "삭제, 생성 소스 코드를 제외한 모든 소스 코드 목록이 null 입니다."),
                    Arguments.of(createUpdateRequestWithNullDeleteSourceCodeIds(), "삭제하는 소스 코드 ID 목록이 null 입니다."),
                    Arguments.of(createUpdateRequestWithNullCategoryId(), "카테고리 ID가 null 입니다."),
                    Arguments.of(createUpdateRequestWithNullTags(), "태그 목록이 null 입니다.")
            );
        }

        private static UpdateTemplateRequest createUpdateRequestWithInvalidTitle(String invalidTitle) {
            UpdateTemplateRequest validRequest = createValidUpdateTemplateRequest();
            return new UpdateTemplateRequest(
                    invalidTitle,
                    validRequest.description(),
                    validRequest.createSourceCodes(),
                    validRequest.updateSourceCodes(),
                    validRequest.deleteSourceCodeIds(),
                    validRequest.categoryId(),
                    validRequest.tags(),
                    Visibility.PUBLIC
            );
        }

        private static UpdateTemplateRequest createUpdateRequestWithInvalidDescription(String invalidDescription) {
            UpdateTemplateRequest validRequest = createValidUpdateTemplateRequest();
            return new UpdateTemplateRequest(
                    validRequest.title(),
                    invalidDescription,
                    validRequest.createSourceCodes(),
                    validRequest.updateSourceCodes(),
                    validRequest.deleteSourceCodeIds(),
                    validRequest.categoryId(),
                    validRequest.tags(),
                    Visibility.PUBLIC
            );
        }

        private static UpdateTemplateRequest createUpdateRequestWithInvalidFileName(String invalidFileName) {
            UpdateTemplateRequest validRequest = createValidUpdateTemplateRequest();
            List<CreateSourceCodeRequest> invalidCreateSourceCodes = List.of(
                    new CreateSourceCodeRequest(invalidFileName, "Valid Content", 2)
            );
            return new UpdateTemplateRequest(
                    validRequest.title(),
                    validRequest.description(),
                    invalidCreateSourceCodes,
                    validRequest.updateSourceCodes(),
                    validRequest.deleteSourceCodeIds(),
                    validRequest.categoryId(),
                    validRequest.tags(),
                    Visibility.PUBLIC
            );
        }

        private static UpdateTemplateRequest createUpdateRequestWithInvalidSourceCode(String invalidSourceCode) {
            UpdateTemplateRequest validRequest = createValidUpdateTemplateRequest();
            List<CreateSourceCodeRequest> invalidCreateSourceCodes = List.of(
                    new CreateSourceCodeRequest("validFileName.txt", invalidSourceCode, 2)
            );
            return new UpdateTemplateRequest(
                    validRequest.title(),
                    validRequest.description(),
                    invalidCreateSourceCodes,
                    validRequest.updateSourceCodes(),
                    validRequest.deleteSourceCodeIds(),
                    validRequest.categoryId(),
                    validRequest.tags(),
                    Visibility.PUBLIC
            );
        }

        private static UpdateTemplateRequest createUpdateRequestWithNullCreateSourceCodes() {
            UpdateTemplateRequest validRequest = createValidUpdateTemplateRequest();
            return new UpdateTemplateRequest(
                    validRequest.title(),
                    validRequest.description(),
                    null,
                    validRequest.updateSourceCodes(),
                    validRequest.deleteSourceCodeIds(),
                    validRequest.categoryId(),
                    validRequest.tags(),
                    Visibility.PUBLIC
            );
        }

        private static UpdateTemplateRequest createUpdateRequestWithNullUpdateSourceCodes() {
            UpdateTemplateRequest validRequest = createValidUpdateTemplateRequest();
            return new UpdateTemplateRequest(
                    validRequest.title(),
                    validRequest.description(),
                    validRequest.createSourceCodes(),
                    null,
                    validRequest.deleteSourceCodeIds(),
                    validRequest.categoryId(),
                    validRequest.tags(),
                    Visibility.PUBLIC
            );
        }

        private static UpdateTemplateRequest createUpdateRequestWithNullDeleteSourceCodeIds() {
            UpdateTemplateRequest validRequest = createValidUpdateTemplateRequest();
            return new UpdateTemplateRequest(
                    validRequest.title(),
                    validRequest.description(),
                    validRequest.createSourceCodes(),
                    validRequest.updateSourceCodes(),
                    null,
                    validRequest.categoryId(),
                    validRequest.tags(),
                    Visibility.PUBLIC
            );
        }

        private static UpdateTemplateRequest createUpdateRequestWithNullCategoryId() {
            UpdateTemplateRequest validRequest = createValidUpdateTemplateRequest();
            return new UpdateTemplateRequest(
                    validRequest.title(),
                    validRequest.description(),
                    validRequest.createSourceCodes(),
                    validRequest.updateSourceCodes(),
                    validRequest.deleteSourceCodeIds(),
                    null,
                    validRequest.tags(),
                    Visibility.PUBLIC
            );
        }

        private static UpdateTemplateRequest createUpdateRequestWithNullTags() {
            UpdateTemplateRequest validRequest = createValidUpdateTemplateRequest();
            return new UpdateTemplateRequest(
                    validRequest.title(),
                    validRequest.description(),
                    validRequest.createSourceCodes(),
                    validRequest.updateSourceCodes(),
                    validRequest.deleteSourceCodeIds(),
                    validRequest.categoryId(),
                    null,
                    Visibility.PUBLIC
            );
        }

        @ParameterizedTest
        @DisplayName("템플릿 수정 실패: 잘못된 소스 코드 순서 입력")
        @CsvSource({"1, 2, 1", "3, 2, 1", "0, 2, 1"})
        void updateTemplateFailWithWrongSourceCodeOrdinal(int createOrdinal1, int createOrdinal2, int updateOrdinal)
                throws Exception {
            // given
            UpdateTemplateRequest updateTemplateRequest = getUpdateTemplateRequest(createOrdinal1,
                    createOrdinal2, updateOrdinal);

            // when & then
            mvc.perform(post("/templates/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateTemplateRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("소스 코드 순서가 잘못되었습니다."));
        }

        private static UpdateTemplateRequest getUpdateTemplateRequest(int createOrdinal1, int createOrdinal2,
                int updateOrdinal
        ) {
            List<CreateSourceCodeRequest> createSourceCodes = List.of(
                    new CreateSourceCodeRequest("filename3", "content3", createOrdinal1),
                    new CreateSourceCodeRequest("filename4", "content4", createOrdinal2));
            List<UpdateSourceCodeRequest> updateSourceCodes = List.of(
                    new UpdateSourceCodeRequest(2L, "updateFilename2", "updateContent2", updateOrdinal));

            return new UpdateTemplateRequest(
                    "updateTitle",
                    "description",
                    createSourceCodes,
                    updateSourceCodes,
                    List.of(1L),
                    2L,
                    List.of("tag1", "tag3"),
                    Visibility.PUBLIC
            );
        }
    }

    @Nested
    @DisplayName("템플릿 삭제 테스트")
    class deleteTemplateTest {

        @ParameterizedTest
        @CsvSource(value = {"1", "1,2"})
        @DisplayName("템플릿 삭제 성공")
        void deleteTemplateSuccess(String templateIdsToDelete) throws Exception {
            mvc.perform(delete("/templates/" + templateIdsToDelete)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            verify(templateApplicationService, times(1)).deleteAllByMemberAndTemplateIds(any(), any());
        }
    }

    @Nested
    @DisplayName("좋아요한 템플릿 조회 테스트")
    class FindTemplateByLiked {

        @Test
        @DisplayName("성공: 로그인한 사용자가 있는 경우")
        void findTemplateByLiked() throws Exception {
            Member member = MemberFixture.getFirstMember();
            FindAllTemplateItemResponse findAllTemplateItemResponse = getFindAllTemplateItemResponse(member);

            when(templateApplicationService.findAllByLiked(any(), any()))
                    .thenReturn(new FindAllTemplatesResponse(1, 1, List.of(findAllTemplateItemResponse)));

            mvc.perform(get("/templates/like")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.templates.size()").value(1))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(jsonPath("$.totalElements").value(1));
        }

        private FindAllTemplateItemResponse getFindAllTemplateItemResponse(Member member) {
            Template template = TemplateFixture.get(member, CategoryFixture.getFirstCategory());
            return FindAllTemplateItemResponse.of(
                    template,
                    List.of(new Tag(1L, "tag1")),
                    SourceCodeFixture.get(template, 1),
                    true
            );
        }
    }
}
