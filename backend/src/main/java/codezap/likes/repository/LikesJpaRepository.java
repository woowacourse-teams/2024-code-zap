package codezap.likes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codezap.likes.domain.Likes;

@Repository
public interface LikesJpaRepository extends LikesRepository, JpaRepository<Likes, Long> {
}
