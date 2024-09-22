package codezap.like.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.DatabaseIsolation;
import codezap.global.ServiceTest;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.template.domain.Template;

@SpringBootTest
@DatabaseIsolation
class LikesServiceTest extends ServiceTest {

    @Autowired
    private LikesService likesService;

    @Nested
    @DisplayName("좋아요 기능 테스트")
    class LikesTest {

        @Test
        @DisplayName("성공")
        void success() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

            likesService.like(MemberDto.from(member), template.getId());

            assertThat(likesRepository.existsByTemplateAndMember(template, member)).isTrue();
        }

        @Test
        @DisplayName("성공: 동일한 사람이 동일한 템플릿에 여러번 좋아요를 해도 Likes 가 한번만 생성된다.")
        @Disabled
        void multipleLikes() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Template template = templateRepository.save(TemplateFixture.get(
                    member,
                    categoryRepository.save(CategoryFixture.getFirstCategory())
            ));

            likesService.like(MemberDto.from(member), template.getId());
            likesService.like(MemberDto.from(member), template.getId());

            //todo 현재 확인하기 어려움. 추후 메서드 추가에 따라 테스트 코드 변경하기
        }
    }
}
