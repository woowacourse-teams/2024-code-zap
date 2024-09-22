package codezap.category.service.facade;

import org.springframework.stereotype.Service;

import codezap.category.service.CategoryTemplateService;
import codezap.member.domain.Member;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberCategoryTemplateApplicationService {

    private final CategoryTemplateService categoryTemplateService;

    public void deleteById(Member member, Long id) {
        categoryTemplateService.deleteById(member, id);
    }
}
