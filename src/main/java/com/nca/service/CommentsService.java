package com.nca.service;

import com.nca.dto.request.CommentsCreateRequestDTO;
import com.nca.dto.request.CommentsUpdateRequestDTO;
import com.nca.dto.response.CommentsResponseDTO;
import com.nca.entity.Comments;
import com.nca.entity.News;
import com.nca.exception.CommentsNotBelongToNewsException;
import com.nca.exception.EntityNotFoundException;
import com.nca.mapper.CommentsMapper;
import com.nca.repository.CommentsRepository;
import com.nca.repository.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.nca.exception.Message.COMMENTS_NOT_BELONG_TO_NEWS;
import static com.nca.exception.Message.ENTITY_NOT_FOUND;

/**
 * {@code CommentsService} for providing business logic with {@link Comments}.
 */

@Slf4j
@Service
@Transactional
public class CommentsService {

    private CommentsRepository commentsRepository;
    private NewsRepository newsRepository;
    private CommentsMapper commentsMapper;

    @Autowired
    public CommentsService(CommentsRepository commentsRepository,
                           NewsRepository newsRepository,
                           CommentsMapper commentsMapper) {
        this.commentsRepository = commentsRepository;
        this.newsRepository = newsRepository;
        this.commentsMapper = commentsMapper;
    }

    public CommentsService() {
    }

    /**
     * {@code save} for providing save {@link Comments} business logic with.
     *
     * @param commentsCreateRequest
     * @param newsId
     * @return {@link CommentsResponseDTO} of saved entity.
     * @throws com.nca.exception.EntityNotFoundException if there isn't news in the system with provided id.
     */
    public CommentsResponseDTO save(CommentsCreateRequestDTO commentsCreateRequest, Long newsId) {
        News news = getNewsById(newsId);

        Comments comment = commentsMapper.toEntity(commentsCreateRequest);
        comment.setNews(news);

        Comments saved = commentsRepository.save(comment);
        log.trace("Saved comments: {}", saved);

        return commentsMapper.toDto(saved);
    }

    /**
     * {@code update} for providing update {@link Comments} business logic.
     *
     * @param commentsUpdateRequest
     * @param commentId
     * @param newsId
     * @return {@link CommentsResponseDTO} of updated entity.
     * @throws com.nca.exception.EntityNotFoundException          if there isn't news or comment in the system with provided id.
     * @throws com.nca.exception.CommentsNotBelongToNewsException if news with provided {@code newsId} hasn't the comment with provided {@code commentId}.
     */
    public CommentsResponseDTO update(CommentsUpdateRequestDTO commentsUpdateRequest, Long commentId, Long newsId) {
        News news = getNewsById(newsId);
        Comments comment = getCommentById(commentId);

        isCommentBeLongToNews(comment, news);

        commentsMapper.updateFromDto(commentsUpdateRequest, comment);
        Comments updated = commentsRepository.save(comment);
        log.trace("Updated comments: {}", updated);

        return commentsMapper.toDto(updated);
    }

    /**
     * {@code getComment} for providing receiving {@link Comments} business logic.
     *
     * @param commentId
     * @param newsId
     * @return {@link CommentsResponseDTO} of received entity.
     * @throws com.nca.exception.EntityNotFoundException          if there isn't news or comment in the system with provided id.
     * @throws com.nca.exception.CommentsNotBelongToNewsException if news with provided {@code newsId} hasn't the comment with provided {@code commentId}.
     */
    @Transactional(readOnly = true)
    public CommentsResponseDTO getComment(Long commentId, Long newsId) {
        News news = getNewsById(newsId);
        Comments comment = getCommentById(commentId);

        isCommentBeLongToNews(comment, news);

        return commentsMapper.toDto(comment);
    }

    /**
     * {@code delete} for providing delete {@link Comments} business logic.
     *
     * @param commentId
     * @param newsId
     * @throws com.nca.exception.EntityNotFoundException          if there isn't news or comment in the system with provided id.
     * @throws com.nca.exception.CommentsNotBelongToNewsException if news with provided {@code newsId} hasn't the comment with provided {@code commentId}.
     */
    public void delete(Long commentId, Long newsId) {
        News news = getNewsById(newsId);
        Comments comment = getCommentById(commentId);

        isCommentBeLongToNews(comment, news);

        news.getComments().remove(comment);
        comment.setNews(null);
        commentsRepository.delete(comment);

        log.trace("Deleted comments: {}", comment);
    }

    /**
     * {@code isCommentBeLongToNews} for providing checking business logic
     * of belonging provided {@link Comments} to provided {@link News}.
     *
     * @param comments
     * @param news
     * @throws com.nca.exception.CommentsNotBelongToNewsException if news with provided {@code newsId} hasn't the comment with provided {@code commentId}.
     */
    private void isCommentBeLongToNews(Comments comments, News news) {
        if (!comments.getNews().getId().equals(news.getId())) {
            throw new CommentsNotBelongToNewsException(
                    String.format(COMMENTS_NOT_BELONG_TO_NEWS.getMessage(), comments.getId(), news.getId()));
        }
    }

    /**
     * {@code getCommentById} for providing receiving {@link Comments} logic from database by provided id.
     *
     * @param id
     * @throws com.nca.exception.EntityNotFoundException if there isn't comment in the system with provided id.
     */
    private Comments getCommentById(Long id) {
        return commentsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND.getMessage(), Comments.class.getSimpleName())));
    }

    /**
     * {@code getNewsById} for providing receiving {@link News} logic from database by provided id.
     *
     * @param id
     * @throws com.nca.exception.EntityNotFoundException if there isn't news in the system with provided id.
     */
    private News getNewsById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ENTITY_NOT_FOUND.getMessage(), News.class.getSimpleName())));
    }
}
