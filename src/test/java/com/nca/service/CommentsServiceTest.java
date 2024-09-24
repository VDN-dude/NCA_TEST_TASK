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
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommentsServiceTest {

    private CommentsRepository commentsRepository = mock(CommentsRepository.class);
    private NewsRepository newsRepository = mock(NewsRepository.class);
    private CommentsMapper commentsMapper = mock(CommentsMapper.class);

    private CommentsService commentsService =
            new CommentsService(commentsRepository, newsRepository, commentsMapper);

    private News news;
    private News secondNews;
    private Comments comments;
    private CommentsCreateRequestDTO commentsCreateRequestDTO;
    private CommentsUpdateRequestDTO commentsUpdateRequestDTO;

    @Before
    public void setUp() {
        news = News.builder()
                .id(1L)
                .title("News title")
                .title("News text")
                .comments(new ArrayList<>())
                .build();
        secondNews = News.builder()
                .id(2L)
                .title("Second News title")
                .title("Second News text")
                .comments(new ArrayList<>())
                .build();
        comments = Comments.builder()
                .id(1L)
                .text("Coments text")
                .news(news)
                .build();
        news.getComments().add(comments);
        commentsCreateRequestDTO = CommentsCreateRequestDTO.builder()
                .text("Creation text")
                .build();
        commentsUpdateRequestDTO = CommentsUpdateRequestDTO.builder()
                .text("Updated comments text")
                .build();
    }

    @Test
    public void shouldSave_Comment() {
        Comments newComments = new Comments();
        when(newsRepository.findById(news.getId()))
                .thenReturn(Optional.of(news));
        when(commentsMapper.toEntity(commentsCreateRequestDTO))
                .then(mapper -> {
                    newComments.setText(commentsCreateRequestDTO.getText());
                    return newComments;
                });
        when(commentsRepository.save(newComments))
                .then(mapper -> {
                    newComments.setId(1L);
                    newComments.setCreationDate(LocalDateTime.now());
                    return newComments;
                });
        when(commentsMapper.toDto(newComments))
                .then(mapper -> {
                    CommentsResponseDTO commentsResponseDTO = new CommentsResponseDTO();
                    commentsResponseDTO.setId(newComments.getId());
                    commentsResponseDTO.setText(newComments.getText());
                    commentsResponseDTO.setNewsId(newComments.getNews().getId());
                    return commentsResponseDTO;
                });

        CommentsResponseDTO saved = commentsService.save(commentsCreateRequestDTO, news.getId());

        assertEquals(1L, saved.getId());
        assertEquals(commentsCreateRequestDTO.getText(), saved.getText());
        assertEquals(news.getId().longValue(), saved.getNewsId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldNotSave_Comment_BecauseOf_NewsNotFound() {
        when(newsRepository.findById(Long.MAX_VALUE))
                .thenReturn(Optional.empty());

        commentsService.save(commentsCreateRequestDTO, Long.MAX_VALUE);
    }

    @Test
    public void shouldUpdate_Comment() {
        when(newsRepository.findById(news.getId()))
                .thenReturn(Optional.of(news));
        when(commentsRepository.findById(comments.getId()))
                .thenReturn(Optional.of(comments));
        when(commentsMapper.updateFromDto(commentsUpdateRequestDTO, comments))
                .then(mapper -> {
                    comments.setText(commentsUpdateRequestDTO.getText());
                    return comments;
                });
        when(commentsRepository.save(comments))
                .thenReturn(comments);
        when(commentsMapper.toDto(comments))
                .then(mapper -> {
                    CommentsResponseDTO commentsResponseDTO = new CommentsResponseDTO();
                    commentsResponseDTO.setId(comments.getId());
                    commentsResponseDTO.setText(comments.getText());
                    commentsResponseDTO.setNewsId(comments.getNews().getId());
                    return commentsResponseDTO;
                });

        CommentsResponseDTO updated = commentsService.update(commentsUpdateRequestDTO, comments.getId(), news.getId());

        assertEquals(commentsUpdateRequestDTO.getText(), updated.getText());
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldNotUpdate_Comment_BecauseOf_NewsNotFound() {
        when(newsRepository.findById(Long.MAX_VALUE))
                .thenReturn(Optional.empty());

        commentsService.update(commentsUpdateRequestDTO, comments.getId(), Long.MAX_VALUE);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldNotUpdate_Comment_BecauseOf_ItNotFound() {
        when(newsRepository.findById(news.getId()))
                .thenReturn(Optional.of(news));
        when(commentsRepository.findById(Long.MAX_VALUE))
                .thenReturn(Optional.empty());

        commentsService.update(commentsUpdateRequestDTO, Long.MAX_VALUE, news.getId());
    }

    @Test
    public void shouldReceive_Comment() {
        when(newsRepository.findById(news.getId()))
                .thenReturn(Optional.of(news));
        when(commentsRepository.findById(comments.getId()))
                .thenReturn(Optional.of(comments));
        when(commentsMapper.toDto(comments))
                .then(mapper -> {
                    CommentsResponseDTO commentsResponseDTO = new CommentsResponseDTO();
                    commentsResponseDTO.setId(comments.getId());
                    commentsResponseDTO.setText(comments.getText());
                    commentsResponseDTO.setNewsId(comments.getNews().getId());
                    return commentsResponseDTO;
                });

        CommentsResponseDTO received = commentsService.getComment(comments.getId(), news.getId());

        assertEquals(comments.getText(), received.getText());
        assertEquals(comments.getId().longValue(), received.getId());
        assertEquals(comments.getNews().getId().longValue(), received.getNewsId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldNotReceive_Comment_BecauseOf_NewsNotFound() {
        when(newsRepository.findById(Long.MAX_VALUE))
                .thenReturn(Optional.empty());

        commentsService.getComment(comments.getId(), Long.MAX_VALUE);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldNotReceive_Comment_BecauseOf_ItNotFound() {
        when(newsRepository.findById(news.getId()))
                .thenReturn(Optional.of(news));
        when(commentsRepository.findById(Long.MAX_VALUE))
                .thenReturn(Optional.empty());

        commentsService.getComment(Long.MAX_VALUE, news.getId());
    }

    @Test(expected = CommentsNotBelongToNewsException.class)
    public void shouldNotReceive_Comment_BecauseOf_ItNoBelongToNews() {
        comments.setNews(secondNews);
        when(newsRepository.findById(news.getId()))
                .thenReturn(Optional.of(news));
        when(commentsRepository.findById(comments.getId()))
                .thenReturn(Optional.of(comments));

        commentsService.getComment(comments.getId(), news.getId());
    }

    @Test
    public void shouldDelete_Comment() {
        when(newsRepository.findById(news.getId()))
                .thenReturn(Optional.of(news));
        when(commentsRepository.findById(comments.getId()))
                .thenReturn(Optional.of(comments));

        commentsService.delete(comments.getId(), news.getId());

        assertNull(comments.getNews());
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldNotDelete_Comment_BecauseOf_NewsNotFound() {
        when(newsRepository.findById(Long.MAX_VALUE))
                .thenReturn(Optional.empty());

        commentsService.delete(comments.getId(), Long.MAX_VALUE);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldNotDelete_Comment_BecauseOf_ItNotFound() {
        when(newsRepository.findById(news.getId()))
                .thenReturn(Optional.of(news));
        when(commentsRepository.findById(Long.MAX_VALUE))
                .thenReturn(Optional.empty());

        commentsService.delete(Long.MAX_VALUE, news.getId());
    }

    @Test(expected = CommentsNotBelongToNewsException.class)
    public void shouldNotDelete_Comment_BecauseOf_ItNoBelongToNews() {
        comments.setNews(secondNews);
        when(newsRepository.findById(news.getId()))
                .thenReturn(Optional.of(news));
        when(commentsRepository.findById(comments.getId()))
                .thenReturn(Optional.of(comments));

        commentsService.delete(comments.getId(), news.getId());
    }
}