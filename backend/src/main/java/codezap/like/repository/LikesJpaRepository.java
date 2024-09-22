package codezap.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codezap.like.domain.Likes;

@Repository
public interface LikesJpaRepository extends LikesRepository, JpaRepository<Likes, Long> {
}
