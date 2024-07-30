package codezap.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.category.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
