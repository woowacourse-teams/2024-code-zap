package codezap.tag.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.global.exception.CodeZapException;
import codezap.global.repository.JpaRepositoryTest;
import codezap.tag.domain.Tag;

@JpaRepositoryTest
class TagJpaRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Nested
    @DisplayName("id로 태그 조회")
    class fetchById {
        @Test
        @DisplayName("id로 태그 조회 성공 : id로 태그를 알아낼 수 있다.")
        void fetchByIdSuccess() {
            Tag tag = tagRepository.save(new Tag("tag"));

            Tag actual = tagRepository.fetchById(tag.getId());

            assertThat(actual).isEqualTo(tag);
        }

        @Test
        @DisplayName("id로 태그 조회 실패 : 존재하지 않는 id인 경우 에러가 발생한다.")
        void fetchByIdFailByNotExistsId() {
            long notExistId = 100;

            assertThatThrownBy(() -> tagRepository.fetchById(notExistId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notExistId + "에 해당하는 태그가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("태그명으로 태그 조회(fetch)")
    class fetchByName {
        @Test
        @DisplayName("태그명으로 태그 조회 성공 : 태그명으로 태그를 조회할 수 있다.")
        void fetchByNameSuccess() {
            Tag tag = tagRepository.save(new Tag("tag"));

            Tag actual = tagRepository.fetchByName(tag.getName());

            assertThat(actual).isEqualTo(tag);
        }

        @Test
        @DisplayName("태그명으로 태그 조회 실패 : 존재하지 않는 태그명인 경우 에러가 발생한다.")
        void fetchByNameFailByNotExistsId() {
            String notExistTagName = "태그";

            assertThatThrownBy(() -> tagRepository.fetchByName(notExistTagName))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("이름이 " + notExistTagName + "인 태그는 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("태그명으로 태그 조회(find)")
    class findByName {
        @Test
        @DisplayName("태그명으로 태그 조회 성공 : 태그명으로 태그를 알아낼 수 있다.")
        void findByNameSuccess() {
            Tag tag = tagRepository.save(new Tag("태그"));

            Optional<Tag> actual = tagRepository.findByName(tag.getName());

            assertThat(actual).hasValue(tag);
        }

        @Test
        @DisplayName("태그명으로 태그 조회 실패 : 존재하지 않는 태그명인 경우 optional 값이 반환된다.")
        void findByNameFailByNotExistsId() {
            Optional<Tag> actual = tagRepository.findByName("태그");

            assertThat(actual).isEmpty();
        }
    }

    @Test
    @DisplayName("이름 목록 조회 성공 : 이름 목록에 포함된 모든 태그를 반환한다.")
    void findNameByNamesIn() {
        tagRepository.save(new Tag("태그1"));
        tagRepository.save(new Tag("태그2"));
        tagRepository.save(new Tag("태그3"));

        List<String> actual = tagRepository.findNameByNamesIn(List.of("태그1", "태그3", "태그4"));

        assertThat(actual).hasSize(2)
                .containsExactly("태그1", "태그3");
    }

    @Nested
    @DisplayName("태그명 존재 여부")
    class existsByName {

        @Test
        @DisplayName("태그명 존재 여부 성공 : 해당 아이디가 존재하면 true를 반환한다.")
        void existsByNameReturnTrue() {
            Tag tag = tagRepository.save(new Tag("태그"));

            boolean actual = tagRepository.existsByName(tag.getName());

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("태그명 존재 여부 성공 : 해당 아이디가 존재하지 않으면 false를 반환한다.")
        void existsByNameReturnFalse() {
            boolean actual = tagRepository.existsByName("태그");

            assertThat(actual).isFalse();
        }
    }
}
