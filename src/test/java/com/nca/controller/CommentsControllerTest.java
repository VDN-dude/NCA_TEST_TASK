package com.nca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nca.dto.request.CommentsCreateRequestDTO;
import com.nca.dto.request.CommentsUpdateRequestDTO;
import com.nca.dto.response.CommentsResponseDTO;
import com.nca.dto.response.ExceptionResponseDTO;
import com.nca.entity.Comments;
import com.nca.entity.News;
import com.nca.repository.CommentsRepository;
import com.nca.repository.NewsRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.nca.exception.Message.COMMENTS_NOT_BELONG_TO_NEWS;
import static com.nca.exception.Message.ENTITY_NOT_FOUND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:context-test.xml"})
public class CommentsControllerTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private NewsRepository newsRepository;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    private CommentsCreateRequestDTO commentsCreateRequestDTO;
    private CommentsUpdateRequestDTO commentsUpdateRequestDTO;

    @Before
    public void setUpModels() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();

        commentsCreateRequestDTO = CommentsCreateRequestDTO.builder()
                .text("Comment text")
                .build();

        commentsUpdateRequestDTO = CommentsUpdateRequestDTO.builder()
                .text("Updated Comment text")
                .build();
    }

    @Test
    public void shouldReturn_Comment() throws Exception {
        long count = newsRepository.count();
        CommentsResponseDTO expectedResponse =
                commentsRepository.findAllDtoByNewsId(Long.valueOf(count), null)
                        .getContent().get(0);

        MvcResult mvcResult = mockMvc
                .perform(get("/news/" + count + "/comments/" + expectedResponse.getId()))
                .andExpect(status().isOk())
                .andReturn();

        CommentsResponseDTO actualResponse = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CommentsResponseDTO.class);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldNotReturn_Comment_BecauseOf_NewsNotExists() throws Exception {
        long newsCount = newsRepository.count();
        long commentsCount = commentsRepository.count();

        MvcResult mvcResult = mockMvc
                .perform(get("/news/" + (newsCount + 1) + "/comments/" + commentsCount))
                .andExpect(status().isNotFound())
                .andReturn();

        ExceptionResponseDTO exceptionResponseDTO =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ExceptionResponseDTO.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), exceptionResponseDTO.getStatus());
        assertEquals(String.format(ENTITY_NOT_FOUND.getMessage(), News.class.getSimpleName()),
                exceptionResponseDTO.getMessage());
    }

    @Test
    public void shouldNotReturn_Comment_BecauseOf_ItNotExists() throws Exception {
        long newsCount = newsRepository.count();
        long commentsCount = commentsRepository.count();

        MvcResult mvcResult = mockMvc
                .perform(get("/news/" + newsCount + "/comments/" + commentsCount + 1))
                .andExpect(status().isNotFound())
                .andReturn();

        ExceptionResponseDTO exceptionResponseDTO =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ExceptionResponseDTO.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), exceptionResponseDTO.getStatus());
        assertEquals(String.format(ENTITY_NOT_FOUND.getMessage(), Comments.class.getSimpleName()),
                exceptionResponseDTO.getMessage());
    }

    @Test
    public void shouldNotReturn_Comment_BecauseOf_ItNotBelongToNews() throws Exception {
        long newsCount = newsRepository.count();
        CommentsResponseDTO notBelongedComment =
                commentsRepository.findAllDtoByNewsId(Long.valueOf(newsCount - 1), null)
                        .getContent().get(0);

        MvcResult mvcResult = mockMvc
                .perform(get("/news/" + newsCount + "/comments/" + notBelongedComment.getId()))
                .andExpect(status().isForbidden())
                .andReturn();

        ExceptionResponseDTO exceptionResponseDTO =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ExceptionResponseDTO.class);

        assertEquals(HttpStatus.FORBIDDEN.value(), exceptionResponseDTO.getStatus());
        assertEquals(String.format(COMMENTS_NOT_BELONG_TO_NEWS.getMessage(), notBelongedComment.getId(), newsCount),
                exceptionResponseDTO.getMessage());
    }

    @Test
    public void shouldCreate_Comment() throws Exception {
        long newsCount = newsRepository.count();

        MvcResult mvcResult = mockMvc
                .perform(post("/news/" + newsCount + "/comments")
                        .content(objectMapper.writeValueAsString(commentsCreateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CommentsResponseDTO commentsResponseDTO = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CommentsResponseDTO.class);

        assertEquals(commentsCreateRequestDTO.getText(), commentsResponseDTO.getText());
        assertEquals(newsCount, commentsResponseDTO.getNewsId());
    }

    @Test
    public void shouldNotCreate_Comment_BecauseOf_NewsNotExists() throws Exception {
        long newsCount = newsRepository.count();

        MvcResult mvcResult = mockMvc
                .perform(post("/news/" + (newsCount + 1) + "/comments")
                        .content(objectMapper.writeValueAsString(commentsCreateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ExceptionResponseDTO exceptionResponseDTO =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ExceptionResponseDTO.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), exceptionResponseDTO.getStatus());
        assertEquals(String.format(ENTITY_NOT_FOUND.getMessage(), News.class.getSimpleName()),
                exceptionResponseDTO.getMessage());
    }

    @Test
    public void shouldUpdate_Comment() throws Exception {
        long newsCount = newsRepository.count();
        CommentsResponseDTO commentToUpdate =
                commentsRepository.findAllDtoByNewsId(Long.valueOf(newsCount), null)
                        .getContent().get(0);

        MvcResult mvcResult = mockMvc
                .perform(patch("/news/" + newsCount + "/comments/" + commentToUpdate.getId())
                        .content(objectMapper.writeValueAsString(commentsUpdateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CommentsResponseDTO commentsResponseDTO = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), CommentsResponseDTO.class);

        assertNotEquals(commentToUpdate.getText(), commentsResponseDTO.getText());
        assertEquals(commentsUpdateRequestDTO.getText(), commentsResponseDTO.getText());
    }

    @Test
    public void shouldNotUpdate_Comment_BecauseOf_ItNoExists() throws Exception {
        long newsCount = newsRepository.count();
        long commentsCount = commentsRepository.count();

        MvcResult mvcResult = mockMvc
                .perform(patch("/news/" + newsCount + "/comments/" + (commentsCount + 1))
                        .content(objectMapper.writeValueAsString(commentsUpdateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ExceptionResponseDTO exceptionResponseDTO =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ExceptionResponseDTO.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), exceptionResponseDTO.getStatus());
        assertEquals(String.format(ENTITY_NOT_FOUND.getMessage(), Comments.class.getSimpleName()),
                exceptionResponseDTO.getMessage());
    }

    @Test
    public void shouldNotUpdate_Comment_BecauseOf_NewsNoExists() throws Exception {
        long newsCount = newsRepository.count();
        long commentsCount = commentsRepository.count();

        MvcResult mvcResult = mockMvc
                .perform(patch("/news/" + (newsCount + 1) + "/comments/" + commentsCount)
                        .content(objectMapper.writeValueAsString(commentsUpdateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ExceptionResponseDTO exceptionResponseDTO =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ExceptionResponseDTO.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), exceptionResponseDTO.getStatus());
        assertEquals(String.format(ENTITY_NOT_FOUND.getMessage(), News.class.getSimpleName()),
                exceptionResponseDTO.getMessage());
    }

    @Test
    public void shouldNotUpdate_Comment_BecauseOf_ItNotBelongToNews() throws Exception {
        long newsCount = newsRepository.count();
        CommentsResponseDTO notBelongedComment =
                commentsRepository.findAllDtoByNewsId(Long.valueOf(newsCount - 1), null)
                        .getContent().get(0);

        MvcResult mvcResult = mockMvc
                .perform(patch("/news/" + newsCount + "/comments/" + notBelongedComment.getId())
                        .content(objectMapper.writeValueAsString(commentsUpdateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        ExceptionResponseDTO exceptionResponseDTO =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ExceptionResponseDTO.class);

        assertEquals(HttpStatus.FORBIDDEN.value(), exceptionResponseDTO.getStatus());
        assertEquals(String.format(COMMENTS_NOT_BELONG_TO_NEWS.getMessage(), notBelongedComment.getId(), newsCount),
                exceptionResponseDTO.getMessage());
    }

    @Test
    public void shouldDelete_Comment() throws Exception {
        long newsCount = newsRepository.count();
        long commentsCount = commentsRepository.count();
        CommentsResponseDTO commentToDelete =
                commentsRepository.findAllDtoByNewsId(Long.valueOf(newsCount), null)
                        .getContent().get(0);

        mockMvc.perform(delete("/news/" + newsCount + "/comments/" + commentToDelete.getId()))
                .andExpect(status().isNoContent())
                .andReturn();

        long afterDeleteCommentsCount = commentsRepository.count();

        assertEquals(commentsCount - 1, afterDeleteCommentsCount);
    }

    @Test
    public void shouldNotDelete_Comment_BecauseOf_ItNoExists() throws Exception {
        long newsCount = newsRepository.count();
        long commentsCount = commentsRepository.count();

        MvcResult mvcResult = mockMvc
                .perform(delete("/news/" + newsCount + "/comments/" + (commentsCount + 1))
                        .content(objectMapper.writeValueAsString(commentsUpdateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ExceptionResponseDTO exceptionResponseDTO =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ExceptionResponseDTO.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), exceptionResponseDTO.getStatus());
        assertEquals(String.format(ENTITY_NOT_FOUND.getMessage(), Comments.class.getSimpleName()),
                exceptionResponseDTO.getMessage());
    }

    @Test
    public void shouldNotDelete_Comment_BecauseOf_NewsNoExists() throws Exception {
        long newsCount = newsRepository.count();
        long commentsCount = commentsRepository.count();

        MvcResult mvcResult = mockMvc
                .perform(delete("/news/" + (newsCount + 1) + "/comments/" + commentsCount)
                        .content(objectMapper.writeValueAsString(commentsUpdateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ExceptionResponseDTO exceptionResponseDTO =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ExceptionResponseDTO.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), exceptionResponseDTO.getStatus());
        assertEquals(String.format(ENTITY_NOT_FOUND.getMessage(), News.class.getSimpleName()),
                exceptionResponseDTO.getMessage());
    }

    @Test
    public void shouldNotDelete_Comment_BecauseOf_ItNotBelongToNews() throws Exception {
        long newsCount = newsRepository.count();
        CommentsResponseDTO notBelongedComment =
                commentsRepository.findAllDtoByNewsId(Long.valueOf(newsCount - 1), null)
                        .getContent().get(0);

        MvcResult mvcResult = mockMvc
                .perform(delete("/news/" + newsCount + "/comments/" + notBelongedComment.getId())
                        .content(objectMapper.writeValueAsString(commentsUpdateRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        ExceptionResponseDTO exceptionResponseDTO =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ExceptionResponseDTO.class);

        assertEquals(HttpStatus.FORBIDDEN.value(), exceptionResponseDTO.getStatus());
        assertEquals(String.format(COMMENTS_NOT_BELONG_TO_NEWS.getMessage(), notBelongedComment.getId(), newsCount),
                exceptionResponseDTO.getMessage());
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            configurableApplicationContext.getEnvironment().getPropertySources();
            System.setProperty("spring.profiles.active", "test");
            configurableApplicationContext.refresh();
        }
    }

}