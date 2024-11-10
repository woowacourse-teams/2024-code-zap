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
import codezap.global.repository.RepositoryTest;
import codezap.tag.domain.Tag;

@RepositoryTest
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Nested
    @DisplayName("id로 태그 조회")
    class FetchById {

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
    @DisplayName("태그명으로 태그 조회(find)")
    class FindByName {

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

    @Nested
    @DisplayName("태그명 리스트로 태그 조회")
    class FindAllByNames {

        @Test
        @DisplayName("태그명 리스트로 태그 조회 성공")
        void findAllByNamesSuccess() {
            Tag lowerCaseTag1 = tagRepository.save(new Tag("java"));
            Tag lowerCaseTag2 = tagRepository.save(new Tag("javascript"));
            Tag lowerCaseTag3 = tagRepository.save(new Tag("typescript"));
            Tag upperCaseTag1 = tagRepository.save(new Tag("Java"));
            Tag upperCaseTag2 = tagRepository.save(new Tag("Javascript"));
            Tag upperCaseTag3 = tagRepository.save(new Tag("Typescript"));
            tagRepository.saveAll(List.of(
                    lowerCaseTag1,
                    lowerCaseTag2,
                    lowerCaseTag3,
                    upperCaseTag1,
                    upperCaseTag2,
                    upperCaseTag3));

            var names = List.of(lowerCaseTag1.getName(), lowerCaseTag3.getName());
            var actual = tagRepository.findAllByNames(names);

            assertThat(actual)
                    .containsExactlyInAnyOrder(lowerCaseTag1, lowerCaseTag3)
                    .doesNotContain(lowerCaseTag2, upperCaseTag1, upperCaseTag2, upperCaseTag3);
        }
    }
}
