package codezap.template.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SourceCodeTest {

    @Test
    @DisplayName("성공: 썸네일(5줄) 추출 잘 되는지 확인")
    void getThumbnailContent() {
        // given
        SourceCode sourceCode = new SourceCode(
                "file",
                "1\n2\n3\n4\n5\n6\n7\n8\n9\n"
        );

        // when
        String thumbnail = sourceCode.getThumbnailContent();

        // then
        assertThat(thumbnail).isEqualTo("1\n2\n3\n4\n5");
    }
}
