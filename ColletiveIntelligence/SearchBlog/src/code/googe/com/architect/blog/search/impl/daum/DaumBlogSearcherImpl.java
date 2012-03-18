package code.googe.com.architect.blog.search.impl.daum;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import code.googe.com.architect.blog.search.BlogQueryParameter;
import code.googe.com.architect.blog.search.BlogQueryParameter.QueryParameter;
import code.googe.com.architect.blog.search.BlogSearchResponseHandler;
import code.googe.com.architect.blog.search.BlogSearcherException;
import code.googe.com.architect.blog.search.impl.BlogSearcherImpl;

public class DaumBlogSearcherImpl extends BlogSearcherImpl {

	protected DaumBlogSearcherImpl() throws BlogSearcherException {
		super();
	}

	// daum에 맞는 request 생
	@Override
	protected HttpMethod getMethod(BlogQueryParameter param) {
		String url = param.getMethodUrl() + "?"
				+ "q=" + urlEncode(param.getParameter(QueryParameter.QUERY))
//				+ "&result=" + param.getParameter(QueryParameter.LIMIT) 
//				+ "&sort=" + param.getParameter(QueryParameter.SORTBY)
				+ "&output=xml"
				+ "&apikey=" + param.getParameter(QueryParameter.KEY);
		
		System.out.println(url);
		GetMethod method = new GetMethod(url);
		return method;
	}

	@Override
	public BlogQueryParameter getBlogQueryParameter() {
		return new DaumBlogQueryParameterImpl();
	}

	

	@Override
	protected BlogSearchResponseHandler getBlogSearchResponseHandler() {
		return new DaumResponseHandler();
	}

}
