package com.nca.repository;

import com.nca.dto.response.CommentsResponseDTO;
import com.nca.entity.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@code CommentsRepository} for requests to database column which bounded with {@link Comments}
 * and do necessary persistence logic there.
 */

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

    /**
     * {@code findById} returning {@code Optional} value of {@link Comments}.
     *
     * @param id
     */
    Optional<Comments> findById(Long id);

    /**
     * {@code findAllDtoByNewsId} returning {@code Page} value with {@link CommentsResponseDTO}.
     *
     * @param newsId
     * @param pageable
     */
    @Query(value = "select new com.nca.dto.response.CommentsResponseDTO(c.id, c.text, c.news.id) FROM Comments c where c.news.id = :newsId")
    Page<CommentsResponseDTO> findAllDtoByNewsId(@Param(value = "newsId") Long newsId, Pageable pageable);
}
