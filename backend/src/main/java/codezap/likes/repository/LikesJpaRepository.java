package codezap.likes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import codezap.likes.domain.Likes;

public interface LikesJpaRepository extends LikesRepository, JpaRepository<Likes, Long> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Likes l WHERE l.template.id in :templateIds")
    void deleteByTemplateIds(List<Long> templateIds);
}
