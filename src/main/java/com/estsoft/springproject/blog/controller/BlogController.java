package com.estsoft.springproject.blog.controller;

import com.estsoft.springproject.blog.domain.dto.AddArticleRequest;
import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.dto.ArticleResponse;
import com.estsoft.springproject.blog.domain.dto.UpdateArticleRequest;
import com.estsoft.springproject.blog.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "블로그 저장/수정/삭제/조회", description = "API 설명을 이곳에 작성하면 됩니다")
public class BlogController {
    private final BlogService service;

    public BlogController(BlogService service) {
        this.service = service;
    }

    // RequestMapping (특정 url   POST /articles)
    // @RequestMapping(method = RequestMethod.POST)
    @Operation(summary = "블로그 글 저장하기", description = "블로그 글 작성하는 화면")
    @PostMapping("/articles")
    public ResponseEntity<ArticleResponse> writeArticle(@RequestBody AddArticleRequest request) {
        Article article = service.saveArticle(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(article.convert());
    }

    // Request Mapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "100", description = "요청에 성공했습니다.", content = @Content(mediaType = "application/json"))
    })
    @Operation(summary = "블로그 전체 목록 보기", description = "블로그 메인 화면에서 보여주는 전체 목록")
    @GetMapping("/articles")
    public ResponseEntity<List<ArticleResponse>> findAll() {
        List<ArticleResponse> articleResponseList = service.findAll().stream()
                .map(Article::convert)
                .toList();
        return ResponseEntity.ok(articleResponseList);
    }

    // 단건 조회 API (Request mapping) 만들기
    @Parameter(name = "id", description = "블로그 글 ID", example = "1")
    @Operation(summary = "블로그 글 세부 정보 보기", description = "블로그 글의 세부 정보를 보여주는 화면")
    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> findById(@PathVariable Long id) {
        Article article = service.findBy(id); // Exception(5xx Server Error) -> 4xx Status Code (Client Error)
        // Article -> ArticleResponse
        return ResponseEntity.ok(article.convert());
    }

    // DELETE /articles/{id}
//    @RequestMapping(method = RequestMethod.DELETE, value = "/articles/{id}")
    @Parameter(name = "id", description = "블로그 글 ID", example = "2")
    @Operation(summary = "블로그 글 삭제하기", description = "블로그 세부 정보 화면에서 글 삭제")
    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteBy(id);
        return ResponseEntity.ok().build();
    }

    // PUT /articles/{id} 수정 API body
    @Parameter(name = "id", description = "블로그 글 ID", example = "3")
    @Operation(summary = "블로그 글 수정하기", description = "블로그 세부 정보 화면에서 글 수정")
    @PutMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> updateById(@PathVariable Long id,
                                              @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = service.update(id, request);
        return ResponseEntity.ok(updatedArticle.convert());
    }

  /*  @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handelIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }*/
}
