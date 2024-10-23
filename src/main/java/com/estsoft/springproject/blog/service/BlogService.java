package com.estsoft.springproject.blog.service;

import com.estsoft.springproject.blog.domain.dto.AddArticleRequest;
import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.dto.ArticleResponse;
import com.estsoft.springproject.blog.domain.dto.UpdateArticleRequest;
import com.estsoft.springproject.blog.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlogService {

    private final BlogRepository repository;

    public BlogService(BlogRepository repository) {
        this.repository = repository;
    }

    // blog 게시글 저장
    public Article saveArticle(AddArticleRequest request) {
        return repository.save(request.toEntity());
    }

    // blog 게시글 조회
    public List<Article> findAll() {
        return repository.findAll();
    }

    // blog 게시글 단건 조회 id(PK) 1건
    public Article findBy(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found id: " + id));
    }

    // blog 게시글 삭제 (id)
    public void deleteBy(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public Article update(Long id, UpdateArticleRequest request) {
        Article article = findBy(id); // 수정하고 싶은 article 객체 가져오기
        article.update(request.getTitle(), request.getContent());
        return article;
    }

//    @Transactional
    public void saveAll(List<AddArticleRequest> requests) {
        List<Article> entities = requests.stream()
                .map(AddArticleRequest::toEntity).toList();
        repository.saveAll(entities);
    }
}
