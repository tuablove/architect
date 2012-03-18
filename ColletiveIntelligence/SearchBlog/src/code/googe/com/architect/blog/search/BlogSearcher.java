package code.googe.com.architect.blog.search;



// 검색 interface
public interface BlogSearcher {

	public BlogQueryResult getRelevantBlogs(BlogQueryParameter param)
		throws BlogSearcherException;

}
