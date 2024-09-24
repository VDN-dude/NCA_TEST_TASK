package com.nca.repository;

import com.nca.dto.response.NewsResponseNoCommentsDTO;
import com.nca.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@code NewsRepository} for requests to database column which bounded with {@link News}
 * and do necessary persistence logic there.
 */

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * {@code findById} returning {@code Optional} value of {@link News}.
     *
     * @param id
     */
    Optional<News> findById(Long id);

    /**
     * {@code findAllDto} returning {@code Page} value with {@link NewsResponseNoCommentsDTO}.
     *
     * @param pageable
     */
    @Query(value = "select new com.nca.dto.response.NewsResponseNoCommentsDTO(n.id, n.text, n.title) FROM News n")
    Page<NewsResponseNoCommentsDTO> findAllDto(Pageable pageable);

    /**
     * {@code findAllDto} returning {@code Page} value with {@link NewsResponseNoCommentsDTO}.
     * Searching {@link News} only with coincidences on {@code searchText} in {@code title} or {@code text}.
     *
     * @param searchText
     * @param pageable
     */
    @Query(value = "select new com.nca.dto.response.NewsResponseNoCommentsDTO" +
            "(n.id, n.title, n.text) FROM News n " +
            "where LOWER(n.title) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(n.text) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    Page<NewsResponseNoCommentsDTO> findAllDto(@Param(value = "searchText") String searchText, Pageable pageable);
}
