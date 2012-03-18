package code.googe.com.architect.blog.search;

import java.util.Date;

// 블로그 추상 interface
public interface RetrievedBlogEntry {
	public String getName();
	public String getUrl();
	public Date getLastUpdateTime();
	public Date getCreationTime();
	public String getTitle();
	public String getExcerpt();
	public String getAuthor();
	
	public String toString();
}
