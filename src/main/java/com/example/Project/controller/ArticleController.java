package com.example.Project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.example.Project.service.ArticleService;
import com.example.Project.service.FileService;
import com.example.Project.util.Util;
import com.example.Project.vo.Article;
import com.example.Project.vo.Rq;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ArticleController {

	private ArticleService articleService;
	private FileService fileService;
	private Rq rq;

	public ArticleController(ArticleService articleService, FileService fileService, Rq rq) {
		this.articleService = articleService;
		this.fileService = fileService;
		this.rq = rq;
	}

	@GetMapping("/user/article/write")
	public String write(Model model, int boardId) {
		model.addAttribute("boardId", boardId);
		return "/user/article/write";
	}

	@GetMapping("/user/article/doWrite")
	@ResponseBody
	public String doWrite(int boardId, String title, String body, String fileUpload, String imgUpload) {

		String fileList = fileUpload.substring(1, fileUpload.length() - 1);
		String imgList = imgUpload.substring(1, imgUpload.length() - 1);

		articleService.articleWrite(title, body, boardId, rq.getLoginMemberNumber(), fileList, imgList);

		int id = articleService.getLastInsertId();

		return Util.jsReplace(String.format("%d번 게시글을 작성했습니다.", id), "/user/article/detail?id=" + id);
	}

	@GetMapping("/user/article/list")
	public String list(Model model, int boardId, @RequestParam(defaultValue = "0") int searchType,
			@RequestParam(defaultValue = "") String searchText, @RequestParam(defaultValue = "10") int itemsInPage,
			@RequestParam(defaultValue = "1") int cPage) {

		searchText = searchText.trim();
		int from = ((cPage - 1) * itemsInPage);

		int articleCount = articleService.articleCount(boardId, searchType, searchText);

		int tPage = articleCount % itemsInPage == 0 ? articleCount / itemsInPage : articleCount / itemsInPage + 1;

		List<Article> articles = articleService.articleList(from, itemsInPage, boardId, searchType, searchText);

		int startPage;
		int endPage;

		if (tPage <= 9) {
			startPage = 1;
			endPage = tPage;
		} else if (cPage < 5) {
			startPage = 1;
			endPage = 9;
		} else if (cPage > tPage - 4) {
			startPage = tPage - 8;
			endPage = tPage;
		} else {
			startPage = cPage - 4;
			endPage = cPage + 4;
		}

		model.addAttribute("articleCount", articleCount);
		model.addAttribute("articles", articles);
		model.addAttribute("cPage", cPage);
		model.addAttribute("tPage", tPage);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("itemsInPage", itemsInPage);
		model.addAttribute("searchText", searchText);
		model.addAttribute("searchType", searchType);

		return "user/article/list";
	}

	@GetMapping("/user/article/detail")
	public String showDetail(HttpServletRequest request, HttpServletResponse response, Model model, int id) {

		boolean isViewed = false;

		if (WebUtils.getCookie(request, "viewedArticle_" + id) != null) {
			isViewed = true;
		}

		if (!isViewed) {
			articleService.viewCountPlus(id);
			Cookie cookie = new Cookie("viewedArticle_" + id, "true");
			cookie.setMaxAge(5);
			response.addCookie(cookie);
		}

		Article article = articleService.forPrintArticle(id);

		model.addAttribute("article", article);

		return "user/article/detail";
	}

	@GetMapping("/user/article/modify")
	public String modify(Model model, int id) {

		Article article = articleService.forPrintArticle(id);

		model.addAttribute("article", article);

		return "user/article/modify";
	}

	@GetMapping("/user/article/doModify")
	@ResponseBody
	public String modify(int id, String title, String body, String fileUpload, String imgUpload) {
		
		String fileList = fileUpload.substring(1, fileUpload.length() - 1);
		String imageList = imgUpload.substring(1, imgUpload.length() - 1);

		fileList = fileList.replace("\"", "");
		imageList = imageList.replace("\"", "");
		
		articleService.articleModify(id, title, body, fileList, imageList);

		return Util.jsReplace(String.format("%d번 게시글을 수정하였습니다.", id), "/user/article/detail?id=" + id);
	}

	@GetMapping("/user/article/doDelete")
	@ResponseBody
	public String doDelete(int id, int boardId) {

		Article article = articleService.getArticleById(id);

		// 불러온 FileList를 배열로 변환해서 전달
		String[] fileList = article.getFileList().split(",");
		String[] imageList = article.getImageList().split(",");

		fileService.fileAndFileDBDelete(fileList, "file");
		fileService.fileAndFileDBDelete(imageList, "image");

		// 게시글 삭제
		articleService.articleDelete(id);

		return Util.jsReplace(String.format("%d번 게시글을 삭제했습니다.", id),
				String.format("/user/article/list?boardId=%d", boardId));
	}
	
	@PostMapping("/user/article/getFileList")
	@ResponseBody
	public String getFileList(int id) {
		return articleService.getFileListById(id);
	}

}