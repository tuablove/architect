package code.googe.com.architect.blog.search.impl.daum;

import code.googe.com.architect.blog.search.impl.BlogQueryParameterImpl;

public class DaumBlogQueryParameterImpl extends BlogQueryParameterImpl {

	private static final String DAUM_SEARCH_API_URL = 
			"http://apis.daum.net/search/blog";
	public DaumBlogQueryParameterImpl() {
		super(QueryType.SEARCH, DAUM_SEARCH_API_URL);
	}

}
