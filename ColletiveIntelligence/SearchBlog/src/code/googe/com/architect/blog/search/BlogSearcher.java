package code.googe.com.architect.blog.search;

public interface BlogSearcher {
	public BlogQueryResult getRelevantBlogs(BlogQueryParameter param)
		throws BlogSearcherException;
}
