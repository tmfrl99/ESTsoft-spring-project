package com.estsoft.springproject.blog.controller;

import com.estsoft.springproject.blog.domain.dto.AddArticleRequest;
import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.dto.UpdateArticleRequest;
import com.estsoft.springproject.blog.repository.BlogRepository;
import com.estsoft.springproject.blog.service.BlogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class BlogControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BlogRepository repository;

    @Autowired
    private BlogService blogService;

    @BeforeEach
    public void SetUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        repository.deleteAll();
    }

    // POST /articles API 테스트
    @Test
    public void addArticle() throws Exception {
        // given: article 저장
//        Article article = new Article("제목", "내용");
        AddArticleRequest request = new AddArticleRequest("제목", "내용");

        // 직렬화 (object -> Json)
        String json = objectMapper.writeValueAsString(request);

        // when: POST /article API 호출
        ResultActions resultActions = mockMvc.perform(post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // then: 호출 결과 검증
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("title").value(request.getTitle()))
                .andExpect(jsonPath("content").value(request.getContent()));

        List<Article> articleList = repository.findAll();
        assertThat(articleList.size()).isEqualTo(1);
    }

    // 블로그 게시글 조회 API
    @Test
    public void findAll() throws Exception {
        // given: 조회 API에 필요한 값 세팅
        Article article = repository.save(new Article("title", "content"));

        // when: 조회 API
        ResultActions resultActions = mockMvc.perform(get("/articles")
                .accept(MediaType.APPLICATION_JSON));

        // then: API 호출 결과 검증
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(article.getTitle()))
                .andExpect(jsonPath("$[0].content").value(article.getContent()));
    }

    // 블로그 단건 조회 API 테스트: data insert (id=1), GET /articles/1
    @Test
    public void findOne() throws Exception {
        // given: 조회 API에 필요한 값 세팅
        Article article = repository.save(new Article("blog title", "blog content"));
        Long id = article.getId();

        // when: API 호출
        ResultActions resultActions = mockMvc.perform(get("/articles/{id}", id)
                .accept(MediaType.APPLICATION_JSON));

        // then: API 호출 결과 검증
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value(article.getTitle()))
                .andExpect(jsonPath("$.content").value(article.getContent()));
    }

    // 단건 조회 API - id에 해당하는 자원이 없을 경우 (4xx) 예외처리 검증
    @Test
    public void findOneException() throws Exception {
        // when: API 호출
        ResultActions resultActions = mockMvc.perform(get("/articles/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON));

        // then: Exception 검증, resultActions STATUS CODE 검증
        resultActions.andExpect(status().isBadRequest());

        assertThrows(IllegalArgumentException.class, () -> blogService.findBy(1L));
    }

    // 블로그 삭제 API 테스트
    // 글 정보 insert, 삭제 API 호출, STATUS CODE 검증, repository.findAll()
    @Test
    public void deleteOne() throws Exception {
        // given: 삭제 API에 필요한 값 세팅
        Article article = repository.save(new Article("blog title", "blog content"));
        Long id = article.getId();

        // when: 삭제 API 호출
        ResultActions resultActions = mockMvc.perform(delete("/articles/{id}", id));

        // then: API 호출 결과 검증
        resultActions.andExpect(status().isOk());
        List<Article> articleList = repository.findAll();
        assertThat(articleList).isEmpty();
    }

    // PUT /articles/{id} body(json content) 요청
    @Test
    public void updateArticle() throws Exception {
        Article article = repository.save(new Article("blog title", "blog content"));
        Long id = article.getId();

        // 수정 데이터(object) -> json
        UpdateArticleRequest request = new UpdateArticleRequest("변경 제목", "변경 내용");
        String updateJasonContent = objectMapper.writeValueAsString(request);

        ResultActions resultActions = mockMvc.perform(put("/articles/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJasonContent)
        );

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(request.getTitle()))
                .andExpect(jsonPath("$.content").value(request.getContent()));
    }

    // 수정 API 호출 시 예외 발생했을 경우(수정하려는 id 존재하지 않음) = status code 검증, Exception 검증
    @Test
    public void updateArticleException() throws Exception {
        // given :id, requestBody
        Long notExistId = 10L;
        UpdateArticleRequest request = new UpdateArticleRequest("title", "content");
        String requestBody = objectMapper.writeValueAsString(request);

        ResultActions resultActions = mockMvc.perform(put("/articles/{id}", notExistId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        resultActions.andExpect(status().isBadRequest());
        assertThrows(IllegalArgumentException.class, () -> blogService.update(notExistId, request));
        assertThrows(IllegalArgumentException.class, () -> blogService.findBy(notExistId));
    }
}