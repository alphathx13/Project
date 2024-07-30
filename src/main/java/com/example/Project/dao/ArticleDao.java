package com.example.Project.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.Project.vo.Article;
import com.example.Project.vo.Reply;

@Mapper
public interface ArticleDao {

	@Insert("""
			insert into article
				set regDate = now()
					, updateDate = now()
					, title = #{title}
					, body = #{body}
					, memberNumber = #{memberNumber}
					, boardId = #{boardId}
					, fileList = #{fileList}
					, imageList = #{imageList}
			""")
	public void articleWrite(String title, String body, int boardId, int memberNumber, String fileList, String imageList);
	
	@Select("""
			<script>
			SELECT a.*, m.nickname `writerName`, IFNULL(SUM(l.point), 0) `likePoint`
				FROM article a
				INNER JOIN `member` m
					ON a.memberNumber = m.id
				LEFT OUTER JOIN likePoint l
					ON a.id = l.relId AND l.relTypeCode = 'article'
				WHERE a.boardId = #{boardId}
				<if test="searchText != ''">
					<choose>
						<when test="searchType == 1">
							and title like CONCAT('%', #{searchText}, '%')
						</when>
						<when test="searchType == 2">
							and body like CONCAT('%', #{searchText}, '%')
						</when>
						<otherwise>
							and (title like CONCAT('%', #{searchText}, '%') or body like CONCAT('%', #{searchText}, '%'))
						</otherwise>
					</choose>
				</if>
				GROUP BY a.id
				ORDER BY a.id desc
				LIMIT #{from}, #{itemsInPage};
			</script>
			""")
	public List<Article> articleList(int from, int itemsInPage, int boardId, int searchType, String searchText);
	
	@Select("""
			SELECT a.*, m.nickname `writerName`, IFNULL(SUM(l.point), 0) `likePoint`
				FROM article a
				INNER JOIN `member` m
					ON a.memberNumber = m.id
				LEFT OUTER JOIN likePoint l
					ON a.id = l.relId AND l.relTypeCode = 'article'
				WHERE a.id = #{id}
				GROUP BY a.id
			""")
	public Article forPrintArticle(int id);
	
	@Select("""
			select * 
				from article 
				where id = #{id}
			""")
	public Article getArticleById(int id);
	
	@Update("""
			<script>
			update article 
				set updateDate = NOW()
					, title = #{title}
					, body = #{body}
					, fileList = #{fileList}
					, imageList = #{imageList}
					where id = #{id}
			</script>
			""")
	public void articleModify(int id, String title, String body, String fileList, String imageList);
	
	@Delete("""
			delete from article 
				where id = #{id}
			""")
	public void articleDelete(int id);

	@Select("SELECT last_insert_id()")
	public int getLastInsertId();

	@Select("""
			<script>
			SELECT count(id)
				FROM article
				WHERE boardId = #{boardId}
				<if test="searchText != ''">
					<choose>
						<when test="searchType == 1">
							and title like CONCAT('%', #{searchText}, '%')
						</when>
						<when test="searchType == 2">
							and body like CONCAT('%', #{searchText}, '%')
						</when>
						<otherwise>
							and (title like CONCAT('%', #{searchText}, '%') or body like CONCAT('%', #{searchText}, '%'))
						</otherwise>
					</choose>
				</if>
			</script>
			""")
	public int articleCount(int boardId, int searchType, String searchText);

	@Update("""
			update article
				set viewCount = viewCount+1
				where id = #{id};
			""")
	public void viewCountPlus(int id);

	@Select("""
			Select FileList
				FROM article
				WHERE id = #{id}
			""")
	public String getFileListById(int id);
	
}