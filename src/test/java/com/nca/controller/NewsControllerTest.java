package com.nca.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nca.dto.request.NewsCreateRequestDTO;
import com.nca.dto.request.NewsUpdateRequestDTO;
import com.nca.dto.response.CommentsResponseDTO;
import com.nca.dto.response.ExceptionResponseDTO;
import com.nca.dto.response.NewsResponseDTO;
import com.nca.dto.response.NewsResponseNoCommentsDTO;
import com.nca.entity.News;
import com.nca.repository.NewsRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;

import static com.nca.exception.Message.ENTITY_NOT_FOUND;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:context-test.xml"})
public class NewsControllerTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private NewsRepository newsRepository;
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    private NewsCreateRequestDTO newsCreateRequestDTO;
    private NewsUpdateRequestDTO newsUpdateRequestDTO;

    @Before
    public void setUpModels() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();

        newsCreateRequestDTO = NewsCreateRequestDTO.builder()
                .title("News Title")
                .text("News text")
                .build();

        newsUpdateRequestDTO = NewsUpdateRequestDTO.builder()
                .title("Updated News Title")
                .text("Updated News text")
                .build();
    }

    @Test
    public void shouldReturn_PageableNewsResponse_WithoutComments() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/news")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        List<NewsResponseNoCommentsDTO> contentFromPage =
                getContentFromPage(mvcResult.getResponse(), null, NewsResponseNoCommentsDTO.class);
        assertEquals(10, contentFromPage.size());

        mvcResult = mockMvc.perform(get("/news")
                        .param("page", "2")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        contentFromPage = getContentFromPage(mvcResult.getResponse(), null, NewsResponseNoCommentsDTO.class);
        assertEquals(10, contentFromPage.size());

        mvcResult = mockMvc.perform(get("/news")
                        .param("page", "1")
                        .param("size", "20")
                        .param("searchText", contentFromPage.get(0).getText()))
                .andExpect(status().isOk())
                .andReturn();

        contentFromPage = getContentFromPage(mvcResult.getResponse(), null, NewsResponseNoCommentsDTO.class);
        assertEquals(1, contentFromPage.size());
    }

    @Test
    public void shouldReturn_NewsWith_PageableCommentsResponse() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/news/1")
                        .param("page", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andReturn();

        List<CommentsResponseDTO> contentFromPage =
                    getContentFromPage(mvcResult.getResponse(), "comments", CommentsResponseDTO.class);
        assertEquals(5, contentFromPage.size());

        mvcResult = mockMvc.perform(get("/news/1")
                        .param("page", "2")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andReturn();

        contentFromPage = getContentFromPage(mvcResult.getResponse(), "comments", CommentsResponseDTO.class);
        assertEquals(5, contentFromPage.size());

        mvcResult = mockMvc.perform(get("/news/1")
                        .param("page", "1")
                        .param("size", "10")
                        .param("searchText", contentFromPage.get(0).getText()))
                .andExpect(status().isOk())
                .andReturn();

        contentFromPage = getContentFromPage(mvcResult.getResponse(), "comments", CommentsResponseDTO.class);
        assertEquals(10, contentFromPage.size());
    }


    @Test
    public void shouldCreate_News() throws Exception {
        long initialCount = newsRepository.count();

        MvcResult mvcResult = mockMvc.perform(post("/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newsCreateRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        NewsResponseDTO newsResponseDTO =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), NewsResponseDTO.class);

        long updatedCount = newsRepository.count();

        assertEquals(newsCreateRequestDTO.getTitle(), newsResponseDTO.getTitle());
        assertEquals(newsCreateRequestDTO.getText(), newsResponseDTO.getText());
        assertEquals(initialCount + 1, updatedCount);
    }

    @Test
    public void shouldUpdate_News() throws Exception {
        long initialCount = newsRepository.count();

        MvcResult mvcResult = mockMvc.perform(patch("/news/" + initialCount)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newsUpdateRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();

        NewsResponseDTO newsResponseDTO =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), NewsResponseDTO.class);

        assertEquals(newsUpdateRequestDTO.getTitle(), newsResponseDTO.getTitle());
        assertEquals(newsUpdateRequestDTO.getText(), newsResponseDTO.getText());
    }

    @Test
    public void shouldNotUpdate_News_BecauseOf_ItNotExists() throws Exception {
        long initialCount = newsRepository.count();

        MvcResult mvcResult = mockMvc.perform(patch("/news/" + initialCount + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newsUpdateRequestDTO)))
                .andExpect(status().isNotFound())
                .andReturn();

        ExceptionResponseDTO exceptionResponseDTO =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ExceptionResponseDTO.class);


        assertEquals(HttpStatus.NOT_FOUND.value(), exceptionResponseDTO.getStatus());
        assertEquals(String.format(ENTITY_NOT_FOUND.getMessage(), News.class.getSimpleName()),
                exceptionResponseDTO.getMessage());
    }

    @Test
    public void shouldDelete_News() throws Exception {
        long initialCount = newsRepository.count();

        mockMvc.perform(delete("/news/" + initialCount))
                .andExpect(status().isNoContent())
                .andReturn();

        long updatedCount = newsRepository.count();

        assertEquals(initialCount - 1, updatedCount);
    }

    @Test
    public void shouldNotDelete_News_BecauseOf_ItNotExists() throws Exception {
        long initialCount = newsRepository.count();

        mockMvc.perform(delete("/news/" + initialCount + 1))
                .andExpect(status().isNotFound())
                .andReturn();

        long updatedCount = newsRepository.count();

        assertEquals(initialCount, updatedCount);
    }

    private <T> List<T> getContentFromPage(MockHttpServletResponse response, String pageObjectName, Class<T> t) throws IOException {
        String contentAsString = response.getContentAsString();
        JsonNode jsonNode = objectMapper.readValue(contentAsString, JsonNode.class);
        JsonNode content;
        if (pageObjectName != null){
            content = jsonNode.get(pageObjectName).get("content");
        } else {
            content = jsonNode.get("content");
        }
        return objectMapper.readValue(content.toString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, t));
    }
}