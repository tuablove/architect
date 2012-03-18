package code.googe.com.architect.blog.search.impl;

import java.util.Date;

import code.googe.com.architect.blog.search.RetrievedBlogEntry;

// 블로그 추상화
public class RetrievedBlogEntryImpl implements RetrievedBlogEntry {
	private String name;
	private String url;
	private Date lastUpdateTime;
	private Date creationTime;
	private String title;
	private String excerpt;
	private String author;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getExcerpt() {
		return excerpt;
	}
	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	@Override
	public String toString() {
		return "RetrievedBlogEntryImpl [title=" + title + ", url=" + url
				+ ", lastUpdateTime=" + lastUpdateTime + ", creationTime="
				+ creationTime + ", name=" + name + ", excerpt=" + excerpt
				+ ", author=" + author + "]";
	}	
	
	

}
