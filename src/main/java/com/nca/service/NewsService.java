package com.nca.service;

import com.nca.dto.response.CommentsResponseDTO;
import com.nca.dto.response.NewsResponseDTO;
import com.nca.dto.response.NewsResponseNoCommentsDTO;
import com.nca.entity.User;
import com.nca.entity.UserRole;
import com.nca.exception.EntityNotFoundException;
import com.nca.exception.ChangeResourceAccessDeniedException;
import com.nca.repository.CommentsRepository;
import com.nca.repository.NewsRepository;
import com.nca.dto.request.NewsCreateRequestDTO;
import com.nca.dto.request.NewsUpdateRequestDTO;
import com.nca.entity.News;
import com.nca.mapper.NewsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.nca.exception.Message.CHANGE_RESOURCE_ACCESS_DENIED;
import static com.nca.exception.Message.ENTITY_NOT_FOUND;

/**
 * {@code NewsService} for providing business logic with {@link News}.
 */

@Slf4j
@Service
@Transactional
public class NewsService {

    private NewsRepository newsRepository;
    private CommentsRepository commentsRepository;
    private NewsMapper newsMapper;

    @Autowired
    public NewsService(NewsRepository newsRepository,
                       CommentsRepository commentsRepository,
                       NewsMapper newsMapper) {
        this.newsRepository = newsRepository;
        this.commentsRepository = commentsRepository;
        this.newsMapper = newsMapper;
    }

    public NewsService() {
    }

    /**
     * {@code save} for providing save {@link News} business logic with.
     *
     * @param newsCreateRequest
     * @return {@link NewsResponseNoCommentsDTO} of saved entity.
     */
    public NewsResponseNoCommentsDTO save(NewsCreateRequestDTO newsCreateRequest) {
        News news = newsMapper.toEntity(newsCreateRequest);

        News saved = newsRepository.save(news);
        log.trace("Saved news: {}", saved);

        return newsMapper.toDtoWithoutComments(saved);
    }

    /**
     * {@code update} for providing update {@link News} business logic.
     *
     * @param newsUpdateRequest
     * @param id
     * @return {@link NewsResponseNoCommentsDTO} of updated entity.
     * @throws com.nca.exception.EntityNotFoundException if there isn't news in the system with provided id.
     */
    public NewsResponseNoCommentsDTO update(NewsUpdateRequestDTO newsUpdateRequest, Long id, User user) {
        News news = getNewsById(id);

        checkAccess(user, news);

        newsMapper.updateFromDto(newsUpdateRequest, news);

        News updated = newsRepository.save(news);
        log.trace("Updated news: {}", updated);

        return newsMapper.toDtoWithoutComments(updated);
    }

    /**
     * {@code getNews} for providing receiving {@link News} business logic.
     *
     * @param id
     * @param page
     * @param size
     * @return {@link NewsResponseDTO} of received entity with pagination for comments.
     * @throws com.nca.exception.EntityNotFoundException if there isn't news in the system with provided id.
     */
    @Transactional(readOnly = true)
    public NewsResponseDTO getNews(Long id, int page, int size) {
        News news = getNewsById(id);

        Page<CommentsResponseDTO> allByNewsId =
                commentsRepository.findAllDtoByNewsId(id, getPageRequest(page, size));
        NewsResponseDTO newsResponseDTO = newsMapper.toDtoWithComments(news);
        newsResponseDTO.setComments(allByNewsId);

        return newsResponseDTO;
    }

    /**
     * {@code getNewsList} for providing receiving {@link News} business logic.
     * Receiving values only with coincidences on {@code searchText} in {@code title} or {@code text}
     *
     * @param page
     * @param size
     * @param searchText
     * @return {@code Page} of {@link NewsResponseNoCommentsDTO} of received entity.
     */
    @Transactional(readOnly = true)
    public Page<NewsResponseNoCommentsDTO> getNewsList(int page, int size, String searchText) {
        return newsRepository.findAllDto(searchText, getPageRequest(page, size));
    }

    /**
     * {@code getNewsList} for providing receiving {@link News} business logic.
     *
     * @param page
     * @param size
     * @return {@code Page} of {@link NewsResponseNoCommentsDTO} of received entity.
     */
    @Transactional(readOnly = true)
    public Page<NewsResponseNoCommentsDTO> getNewsList(int page, int size) {
        return newsRepository.findAllDto(getPageRequest(page, size));
    }

    /**
     * {@code delete} for providing delete {@link News} business logic.
     *
     * @param id
     * @throws com.nca.exception.EntityNotFoundException if there isn't news in the system with provided id.
     */
    public void delete(Long id, User user) {
        News news = getNewsById(id);

        checkAccess(user, news);

        newsRepository.delete(news);
        log.trace("Deleted news: {}", news);
    }

    /**
     * {@code getPageRequest} creating {@code PageRequest}.
     *
     * @param page
     * @param size
     * @return {@code PageRequest} with sorting by creation date.
     */
    private PageRequest getPageRequest(int page, int size) {
        return new PageRequest(page, size, new Sort(Sort.Direction.DESC, "creationDate"));
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

    private void checkAccess(User user, News news) {
        if (user.getUserRole().equals(UserRole.ROLE_JOURNALIST)) {

            if (!user.getId().equals(news.getInsertedBy().getId())) {

                throw new ChangeResourceAccessDeniedException(
                        String.format(CHANGE_RESOURCE_ACCESS_DENIED.getMessage(),
                                News.class.getSimpleName(),
                                news.getId()));
            }
        }
    }
}
