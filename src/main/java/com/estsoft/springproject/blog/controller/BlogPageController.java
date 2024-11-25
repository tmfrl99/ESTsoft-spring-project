package com.estsoft.springproject.blog.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.dto.ArticleViewResponse;
import com.estsoft.springproject.blog.service.BlogService;
import com.estsoft.springproject.user.domain.Users;

@Controller
public class BlogPageController {
	private final BlogService blogService;

	public BlogPageController(BlogService blogService) {
		this.blogService = blogService;
	}

	@GetMapping("/articles")
	public String showArticle(Model model) {
		List<Article> articleList = blogService.findAll();

		model.addAttribute("articles", articleList);
		return "articleList"; // articleList.html
	}

	@GetMapping("/articles/{id}")
	public String showDetails(@PathVariable Long id, Model model, @AuthenticationPrincipal Users users) {
		Article article = blogService.findBy(id);
		model.addAttribute("article", new ArticleViewResponse(article));

		return "article";
	}

	// GET /new-articles?id=1
	// GET /new-articles
	@GetMapping("/new-article")
	public String addArticle(@RequestParam(required = false) Long id, Model model) {
		if (id == null) {   // 새로운 게시글 생성
			model.addAttribute("article", new ArticleViewResponse());
		} else {    // 게시글 수정: 기존 게시글 불러오기
			Article article = blogService.findBy(id);
			model.addAttribute("article", new ArticleViewResponse(article));
		}
		return "newArticle";    // newArticle.html
	}
}
