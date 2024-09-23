package codezap.likes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codezap.likes.domain.Likes;

public interface LikesJpaRepository extends LikesRepository, JpaRepository<Likes, Long> {
}
