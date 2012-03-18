package code.googe.com.architect.blog.search;

import java.util.List;

/**
 * @author  chois79
 * 			단순히 BlogEntry를 관리하기 위한 Interface
 */
public interface BlogQueryResult {
	
	public Integer getQueryCount();
	public List<RetrievedBlogEntry> getRelevantBlogs();
}
