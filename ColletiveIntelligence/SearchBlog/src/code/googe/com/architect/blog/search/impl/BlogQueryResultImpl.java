package code.googe.com.architect.blog.search.impl;

import java.util.List;

import code.googe.com.architect.blog.search.BlogQueryResult;
import code.googe.com.architect.blog.search.RetrievedBlogEntry;


// Query 결과인 Blog list 관
public class BlogQueryResultImpl implements BlogQueryResult {

	private List<RetrievedBlogEntry> blogs;
	private Integer count;
	
	public void setQueryCount(Integer counter) {
		this.count = counter;
	}
	
	@Override
	public Integer getQueryCount() {
		if(count > 0) {
			return count;
		}
		
		return blogs.size();
	}

	@Override
	public List<RetrievedBlogEntry> getRelevantBlogs() {
		return this.blogs;
	}

	public void setResults(List<RetrievedBlogEntry> entries) {
		this.blogs = entries;
	}

}
