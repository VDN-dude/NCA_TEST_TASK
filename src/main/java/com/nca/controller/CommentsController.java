package com.nca.controller;

import com.nca.dto.request.CommentsCreateRequestDTO;
import com.nca.dto.request.CommentsUpdateRequestDTO;
import com.nca.dto.response.CommentsResponseDTO;
import com.nca.entity.User;
import com.nca.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * {@code CommentsController} is controller which process all request about comments.
 * Inside uri for all endpoints there should be {@code newsId}, because of you cannot have comment for nothing.
 */

@Controller
@RequestMapping("/news/{newsId}/comments")
public class CommentsController {

    private CommentsService commentsService;

    @Autowired
    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    public CommentsController() {
    }

    /**
     * Endpoint for receiving {@link CommentsResponseDTO} with provided id.
     * For deeper logic go {@link CommentsService}.
     *
     * @param newsId
     * @param commentId
     *
     * @return {@code ResponseEntity} with {@link CommentsResponseDTO} as response body.
     */
    @RequestMapping(value = "/{commentId}", method = RequestMethod.GET)
    public ResponseEntity<CommentsResponseDTO> get(@PathVariable(value = "newsId") Long newsId,
                                                   @PathVariable(value = "commentId") Long commentId) {

        return new ResponseEntity<>(commentsService.getComment(commentId, newsId), HttpStatus.OK);
    }

    /**
     * Endpoint for creating {@link com.nca.entity.Comments}
     * with provided request body as {@link CommentsCreateRequestDTO}.
     * For deeper logic go {@link CommentsService}.
     *
     * @param newsId
     * @param commentsCreateRequest
     *
     * @return {@code ResponseEntity} with created entity mapped to {@link CommentsResponseDTO} as response body.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBER')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CommentsResponseDTO> create(@PathVariable(value = "newsId") Long newsId,
                                                      @RequestBody @Valid CommentsCreateRequestDTO commentsCreateRequest) {

        return new ResponseEntity<>(commentsService.save(commentsCreateRequest, newsId), HttpStatus.CREATED);
    }

    /**
     * Endpoint for updating {@link com.nca.entity.Comments}
     * with provided request body as {@link CommentsUpdateRequestDTO}.
     * For deeper logic go {@link CommentsService}.
     *
     * @param newsId
     * @param commentsUpdateRequest
     *
     * @return {@code ResponseEntity} with updated entity mapped to {@link CommentsResponseDTO} as response body.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBER')")
    @RequestMapping(value = "/{commentId}", method = RequestMethod.PATCH)
    public ResponseEntity<CommentsResponseDTO> update(@AuthenticationPrincipal User user,
                                                      @PathVariable(value = "newsId") Long newsId,
                                                      @PathVariable(value = "commentId") Long commentId,
                                                      @RequestBody @Valid CommentsUpdateRequestDTO commentsUpdateRequest) {

        return new ResponseEntity<>(commentsService.update(commentsUpdateRequest, commentId, newsId, user),
                HttpStatus.OK);
    }

    /**
     * Endpoint for deleting {@link com.nca.entity.Comments}.
     * For deeper logic go {@link CommentsService}.
     *
     * @param newsId
     * @param commentId
     *
     * @return {@code ResponseEntity} with empty body.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBER')")
    @RequestMapping(value = "/{commentId}", method = RequestMethod.DELETE)
    public ResponseEntity<CommentsResponseDTO> delete(@AuthenticationPrincipal User user,
                                                      @PathVariable(value = "newsId") Long newsId,
                                                      @PathVariable(value = "commentId") Long commentId) {

        commentsService.delete(commentId, newsId, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
