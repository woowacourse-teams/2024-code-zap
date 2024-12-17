package codezap.category.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import codezap.category.domain.Category;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryQueryDslRepository categoryQueryDslRepository;

    public Category save(Category category) {
        return categoryJpaRepository.save(category);
    }

    public Category fetchById(Long id) {
        return categoryJpaRepository.findById(id).orElseThrow(
                () -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND, "식별자 " + id + "에 해당하는 카테고리가 존재하지 않습니다."));
    }

    public List<Category> findAllByMemberIdOrderById(Long memberId) {
        return categoryJpaRepository.findAllByMemberIdOrderById(memberId);
    }

    public List<Category> findAll() {
        return categoryJpaRepository.findAll();
    }

    public boolean existsByNameAndMember(String categoryName, Member member) {
        return categoryJpaRepository.existsByNameAndMember(categoryName, member);
    }

    public void deleteById(Long id) {
        categoryJpaRepository.deleteById(id);
    }

    public long countByMember(Member member) {
        return categoryJpaRepository.countByMember(member);
    }

    public void shiftOrdinal(Member member, Long ordinal) {
        categoryQueryDslRepository.shiftOrdinal(member, ordinal);
    }
}
