package com.nca.controller;

import com.nca.dto.request.NewsCreateRequestDTO;
import com.nca.dto.request.NewsUpdateRequestDTO;
import com.nca.dto.response.NewsResponseDTO;
import com.nca.dto.response.NewsResponseNoCommentsDTO;
import com.nca.entity.User;
import com.nca.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * {@code NewsController} is controller which process all request about {@link com.nca.entity.News}.
 */

@Controller
@RequestMapping("/news")
@Validated
public class NewsController {

    private NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    public NewsController() {
    }

    /**
     * Endpoint for getting {@link NewsResponseNoCommentsDTO} with pagination,
     * without {@code comments}.
     * By {@code searchText} we can find news with info from param inside {@code title} and {@code text} fields.
     * For deeper logic go {@link NewsService}.
     *
     * @param page
     * @param size
     * @param searchText notRequired
     *
     * @return {@code ResponseEntity} with pageable {@link NewsResponseNoCommentsDTO} as response body.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<NewsResponseNoCommentsDTO>> getList(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                   @RequestParam(value = "size", defaultValue = "20") int size,
                                                                   @RequestParam(value = "searchText", required = false) String searchText) {

        if (searchText != null) {
            return new ResponseEntity<>(newsService.getNewsList(page - 1, size, searchText), HttpStatus.OK);
        }
        return new ResponseEntity<>(newsService.getNewsList(page - 1, size), HttpStatus.OK);
    }

    /**
     * Endpoint for getting {@link NewsResponseDTO}
     * with pagination for {@code comments} field.
     * For deeper logic go {@link NewsService}.
     *
     * @param page
     * @param size
     *
     * @return {@code ResponseEntity} with entity mapped to {@link NewsResponseDTO} as response body.
     */
    @RequestMapping(value = "/{newsId}", method = RequestMethod.GET)
    public ResponseEntity<NewsResponseDTO> get(@PathVariable(value = "newsId") Long newsId,
                                               @RequestParam(value = "page", defaultValue = "1") int page,
                                               @RequestParam(value = "size", defaultValue = "20") int size) {

        return new ResponseEntity<>(newsService.getNews(newsId, page - 1, size), HttpStatus.OK);
    }

    /**
     * Endpoint for creating {@link com.nca.entity.News}
     * with provided request body as {@link NewsCreateRequestDTO}.
     * For deeper logic go {@link NewsService}.
     *
     * @param newsCreateRequest
     *
     * @return {@code ResponseEntity} with created entity mapped to {@link NewsResponseNoCommentsDTO} as response body.
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_JOURNALIST')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<NewsResponseNoCommentsDTO> create(@RequestBody @Valid NewsCreateRequestDTO newsCreateRequest) {

        return new ResponseEntity<>(newsService.save(newsCreateRequest), HttpStatus.CREATED);
    }

    /**
     * Endpoint for updating {@link com.nca.entity.News}
     * with provided request body as {@link NewsUpdateRequestDTO}.
     * For deeper logic go {@link NewsService}.
     *
     * @param newsId
     * @param newsUpdateRequest
     * @return {@code ResponseEntity} with updated entity mapped to {@link NewsResponseNoCommentsDTO} as response body.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'JOURNALIST')")
    @RequestMapping(value = "/{newsId}", method = RequestMethod.PATCH)
    public ResponseEntity<NewsResponseNoCommentsDTO> update(@AuthenticationPrincipal User user,
                                                            @PathVariable(value = "newsId") Long newsId,
                                                            @RequestBody @Valid NewsUpdateRequestDTO newsUpdateRequest) {

        return new ResponseEntity<>(newsService.update(newsUpdateRequest, newsId, user), HttpStatus.OK);
    }

    /**
     * Endpoint for deleting {@link com.nca.entity.News}.
     * For deeper logic go {@link NewsService}.
     *
     * @param newsId
     * @return {@code ResponseEntity} with empty body
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'JOURNALIST')")
    @RequestMapping(value = "/{newsId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user,
                                    @PathVariable(value = "newsId") Long newsId) {

        newsService.delete(newsId, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
