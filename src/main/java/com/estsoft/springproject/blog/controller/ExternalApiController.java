package com.estsoft.springproject.blog.controller;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.dto.AddArticleRequest;
import com.estsoft.springproject.blog.domain.dto.ArticleViewResponse;
import com.estsoft.springproject.blog.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Slf4j
@RestController
public class ExternalApiController {
    private final BlogService blogService;

    public ExternalApiController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/api/external")
    public String callApi() {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://jsonplaceholder.typicode.com/posts";
        // 외부 API 호출, 역직렬화 (restTemplate)
        ResponseEntity<List<Content>> resultList =
                restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        List<Content> contents = resultList.getBody();

        if (contents != null) {
            List<AddArticleRequest> requests = contents.stream()
                    .map(content -> new AddArticleRequest(content.getTitle(), content.getBody()))
                    .toList();

            blogService.saveAll(requests);
            return "OK";

        } else {
            log.info("code: {}", resultList.getStatusCode());
            log.info("{}", resultList.getBody());
            return "ERROR";
        }
    }
}
