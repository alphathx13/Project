package com.example.Project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Project.dao.ArticleDao;
import com.example.Project.vo.Article;

@Service
public class ArticleService {

	private ArticleDao articleDao;

	public ArticleService(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}

	public void articleWrite(String title, String body, int boardId, int memberNumber, String fileList, String imageList) {
		articleDao.articleWrite(title, body, boardId, memberNumber, fileList, imageList);
	}

	public List<Article> articleList(int from, int itemsInPage, int boardId, int searchType, String searchText) {
		return articleDao.articleList(from, itemsInPage, boardId, searchType, searchText);
	}
	
	public Article getArticleById(int id) {
		return articleDao.getArticleById(id);
	}
	
	public void articleModify(int id, String title, String body, String fileList, String imgList) {
		articleDao.articleModify(id, title, body, fileList, imgList);
	}
	
	public void articleDelete(int id) {
		articleDao.articleDelete(id);
	}

	public int getLastInsertId() {
		return articleDao.getLastInsertId();
	}

	public Article forPrintArticle(int id) {
		return articleDao.forPrintArticle(id);
	}

	public int articleCount(int boardId, int searchType, String searchText) {
		return articleDao.articleCount(boardId, searchType, searchText);
	}

	public void viewCountPlus(int id) {
		articleDao.viewCountPlus(id);
	}

	public String getFileListById(int id) {
		return articleDao.getFileListById(id);
	}

}