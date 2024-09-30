package com.nca.service;

import com.nca.dto.request.NewsCreateRequestDTO;
import com.nca.dto.request.NewsUpdateRequestDTO;
import com.nca.dto.response.CommentsResponseDTO;
import com.nca.dto.response.NewsResponseDTO;
import com.nca.dto.response.NewsResponseNoCommentsDTO;
import com.nca.entity.Comments;
import com.nca.entity.News;
import com.nca.entity.User;
import com.nca.entity.UserRole;
import com.nca.exception.EntityNotFoundException;
import com.nca.mapper.NewsMapper;
import com.nca.repository.CommentsRepository;
import com.nca.repository.NewsRepository;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NewsServiceTest {

    private CommentsRepository commentsRepository = mock(CommentsRepository.class);
    private NewsRepository newsRepository = mock(NewsRepository.class);
    private NewsMapper newsMapper = mock(NewsMapper.class);

    private NewsService newsService =
            new NewsService(newsRepository,
                    commentsRepository,
                    newsMapper);

    private User user;
    private News news;
    private News secondNews;
    private Comments comments;
    private NewsCreateRequestDTO newsCreateRequestDTO;
    private NewsUpdateRequestDTO newsUpdateRequestDTO;
    private CommentsResponseDTO commentsResponseDTO;
    private NewsResponseNoCommentsDTO newsResponseNoCommentsDTO;
    private NewsResponseNoCommentsDTO secondNewsResponseNoCommentsDTO;
    private Page<CommentsResponseDTO> commentsResponseDTOPage;
    private Page<NewsResponseNoCommentsDTO> newsResponseDTOPage;
    private List<NewsResponseNoCommentsDTO> newsList;

    @Before
    public void setUp() {
        user = User.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("Name")
                .surname("Surname")
                .parentName("Parent name")
                .password("Password")
                .username("Username")
                .userRole(UserRole.ROLE_ADMIN)
                .build();
        news = News.builder()
                .id(1L)
                .title("News title")
                .text("News text")
                .comments(new ArrayList<>())
                .build();
        secondNews = News.builder()
                .id(2L)
                .title("Second News title")
                .text("Second News text")
                .comments(new ArrayList<>())
                .build();
        commentsResponseDTO = CommentsResponseDTO.builder()
                .id(1L)
                .text("Comment text")
                .newsId(news.getId())
                .build();
        news.getComments().add(comments);
        newsCreateRequestDTO = NewsCreateRequestDTO.builder()
                .title("Creation title")
                .text("Creation text")
                .build();
        newsUpdateRequestDTO = NewsUpdateRequestDTO.builder()
                .title("Update title")
                .text("Updated text")
                .build();
        newsResponseNoCommentsDTO = NewsResponseNoCommentsDTO.builder()
                .id(news.getId())
                .title(news.getTitle())
                .text(news.getText())
                .build();
        secondNewsResponseNoCommentsDTO = NewsResponseNoCommentsDTO.builder()
                .id(secondNews.getId())
                .title(secondNews.getTitle())
                .text(secondNews.getText())
                .build();

        newsList = new ArrayList<>();
        newsList.add(newsResponseNoCommentsDTO);
        newsList.add(secondNewsResponseNoCommentsDTO);

        newsResponseDTOPage = new PageImpl<>(newsList);

        List<CommentsResponseDTO> commentsList = new ArrayList<>();
        commentsList.add(commentsResponseDTO);

        commentsResponseDTOPage = new PageImpl<>(commentsList);
    }

    @Test
    public void shouldSave_News() {
        News newNews = new News();
        when(newsMapper.toEntity(newsCreateRequestDTO))
                .then(mapper -> {
                    newNews.setTitle(newsCreateRequestDTO.getTitle());
                    newNews.setText(newsCreateRequestDTO.getText());
                    return newNews;
                });
        when(newsRepository.save(newNews))
                .then(mapper -> {
                    newNews.setId(1L);
                    newNews.setCreationDate(LocalDateTime.now());
                    return newNews;
                });
        when(newsMapper.toDtoWithoutComments(newNews))
                .then(mapper -> {
                    NewsResponseNoCommentsDTO newsResponseNoCommentsDTO =
                            new NewsResponseNoCommentsDTO();
                    newsResponseNoCommentsDTO.setId(newNews.getId());
                    newsResponseNoCommentsDTO.setText(newNews.getText());
                    newsResponseNoCommentsDTO.setTitle(newNews.getTitle());
                    return newsResponseNoCommentsDTO;
                });

        NewsResponseNoCommentsDTO saved = newsService.save(newsCreateRequestDTO);

        assertEquals(1L, saved.getId());
        assertEquals(newsCreateRequestDTO.getText(), saved.getText());
        assertEquals(newsCreateRequestDTO.getTitle(), saved.getTitle());
    }

    @Test
    public void shouldUpdate_News() {
        when(newsRepository.findById(news.getId()))
                .thenReturn(Optional.of(news));
        when(newsMapper.updateFromDto(newsUpdateRequestDTO, news))
                .then(mapper -> {
                    news.setTitle(newsUpdateRequestDTO.getTitle());
                    news.setText(newsUpdateRequestDTO.getText());
                    return news;
                });
        when(newsRepository.save(news))
                .then(mapper -> {
                    news.setLastEditDate(LocalDateTime.now());
                    return news;
                });
        when(newsMapper.toDtoWithoutComments(news))
                .then(mapper -> {
                    NewsResponseNoCommentsDTO newsResponseNoCommentsDTO =
                            new NewsResponseNoCommentsDTO();
                    newsResponseNoCommentsDTO.setId(news.getId());
                    newsResponseNoCommentsDTO.setText(news.getText());
                    newsResponseNoCommentsDTO.setTitle(news.getTitle());
                    return newsResponseNoCommentsDTO;
                });

        NewsResponseNoCommentsDTO updated =
                newsService.update(newsUpdateRequestDTO, news.getId(), user);

        assertEquals(newsUpdateRequestDTO.getText(), updated.getText());
        assertEquals(newsUpdateRequestDTO.getTitle(), updated.getTitle());
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldNotUpdate_News_BecauseOf_NotFound() {
        when(newsRepository.findById(Long.MAX_VALUE))
                .thenReturn(Optional.empty());

        newsService.update(newsUpdateRequestDTO, Long.MAX_VALUE, user);
    }

    @Test
    public void shouldReceive_News() {
        PageRequest pageRequest =
                new PageRequest(0, 20, new Sort(Sort.Direction.DESC, "creationDate"));
        when(newsRepository.findById(news.getId()))
                .thenReturn(Optional.of(news));
        when(commentsRepository.findAllDtoByNewsId(news.getId(), pageRequest))
                .thenReturn(commentsResponseDTOPage);
        when(newsMapper.toDtoWithComments(news))
                .then(mapper -> {
                    NewsResponseDTO newsResponseDTO =
                            new NewsResponseDTO();
                    newsResponseDTO.setId(news.getId());
                    newsResponseDTO.setText(news.getText());
                    newsResponseDTO.setTitle(news.getTitle());
                    return newsResponseDTO;
                });

        NewsResponseDTO received = newsService.getNews(news.getId(), 0, 20);

        assertEquals(news.getText(), received.getText());
        assertEquals(news.getTitle(), received.getTitle());
        assertEquals(commentsResponseDTOPage, received.getComments());
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldNotReceive_News_BecauseOf_NotFound() {
        when(newsRepository.findById(Long.MAX_VALUE))
                .thenReturn(Optional.empty());

        newsService.getNews(Long.MAX_VALUE, 0, 20);
    }

    @Test
    public void shouldReceive_NewsList() {
        PageRequest pageRequest =
                new PageRequest(0, 20, new Sort(Sort.Direction.DESC, "creationDate"));
        when(newsRepository.findAllDto(pageRequest))
                .thenReturn(newsResponseDTOPage);

        Page<NewsResponseNoCommentsDTO> newsList =
                newsService.getNewsList(0, 20);

        assertEquals(newsResponseDTOPage, newsList);

        when(newsRepository.findAllDto(news.getText(), pageRequest))
                .thenReturn(newsResponseDTOPage);
        this.newsList.removeIf(dto -> !dto.getText().contains(news.getText()) || !dto.getTitle().contains(news.getTitle()));
        newsResponseDTOPage = new PageImpl<>(this.newsList);

        newsList = newsService.getNewsList(0, 20);

        assertEquals(newsResponseDTOPage, newsList);
    }

}