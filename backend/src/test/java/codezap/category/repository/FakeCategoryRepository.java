package codezap.category.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;

import codezap.category.domain.Category;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;

public class FakeCategoryRepository implements CategoryRepository {

    private final AtomicLong idCounter = new AtomicLong(1);

    private final List<Category> categories;

    public FakeCategoryRepository() {
        this.categories = new ArrayList<>();
    }

    public FakeCategoryRepository(List<Category> categories) {
        this.categories = new ArrayList<>(categories);
        idCounter.set(1 + categories.size());
    }

    @Override
    public Category fetchById(Long id) {
        return categories.stream()
                .filter(category -> Objects.equals(category.getId(), id))
                .findFirst()
                .orElseThrow(() -> new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 카테고리가 존재하지 않습니다."));
    }

    @Override
    public List<Category> findAllByMemberIdOrderById(Long memberId) {
        return categories.stream()
                .filter(category -> Objects.equals(category.getMember().getId(), memberId))
                .sorted(Comparator.comparing(Category::getId))
                .toList();
    }

    @Override
    public List<Category> findAll() {
        return categories;
    }

    @Override
    public boolean existsById(Long id) {
        return categories.stream().anyMatch(category -> Objects.equals(category.getId(), id));
    }

    @Override
    public boolean existsByNameAndMember(String categoryName, Member member) {
        return categories.stream()
                .anyMatch(category ->
                        Objects.equals(category.getName(), categoryName) && Objects.equals(category.getMember(), member)
                );
    }

    @Override
    public Category save(Category entity) {
        var saved = new Category(
                getOrGenerateId(entity),
                entity.getMember(),
                entity.getName(),
                entity.getIsDefault()
        );
        categories.removeIf(category -> Objects.equals(category.getId(), entity.getId()));
        categories.add(saved);
        return saved;
    }

    private long getOrGenerateId(Category entity) {
        if (existsById(entity.getId())) {
            return entity.getId();
        }
        return idCounter.getAndIncrement();
    }

    @Override
    public void deleteById(Long id) {
        categories.removeIf(category -> Objects.equals(category.getId(), id));
    }
}
