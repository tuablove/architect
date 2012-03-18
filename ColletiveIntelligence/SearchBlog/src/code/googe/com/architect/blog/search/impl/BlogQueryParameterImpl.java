package code.googe.com.architect.blog.search.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import code.googe.com.architect.blog.search.BlogQueryParameter;

// Query parameter를 위한 추상 클래스 
public abstract class BlogQueryParameterImpl implements BlogQueryParameter {

	private Map<QueryParameter, String> params = null;

	private QueryType queryType = null;

	private String methodUrl = null;
	
	public BlogQueryParameterImpl(QueryType queryType, String methodUrl) {
		this.queryType = queryType;
		this.methodUrl = methodUrl;
		this.params = new HashMap<QueryParameter, String>();
	}
	
	@Override
	public String getParameter(QueryParameter param) {
		return params.get(param);
	}

	@Override
	public void setParameter(QueryParameter param, String value) {
		this.params.put(param, value);
	}

	@Override
	public Collection<QueryParameter> getAllParameters() {
		return params.keySet();
	}

	@Override
	public QueryType getQueryType() {
		return queryType;
	}

	@Override
	public String getMethodUrl() {
		return this.methodUrl;
	}

}
