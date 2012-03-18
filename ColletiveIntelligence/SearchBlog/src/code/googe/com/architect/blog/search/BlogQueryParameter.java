package code.googe.com.architect.blog.search;

import java.util.Collection;

/**
 * @author  chois79
 */
public interface BlogQueryParameter {
	/**
	 * @author   chois79
	 */
	public enum QueryType {
	SEARCH,
	TAG};
	public enum QueryParameter {
	KEY, 
	APIUSER,
	START_INDEX,
	LIMIT, 
	QUERY, 
	TAG, 
	SORTBY, 
	LANGUAGE};
	
	public String getParameter(QueryParameter param);
	public void setParameter(QueryParameter param, String value);
	public Collection<QueryParameter> getAllParameters();
	/**
	 * @uml.property  name="queryType"
	 * @uml.associationEnd  
	 */
	public QueryType getQueryType();
	/**
	 * @uml.property  name="methodUrl"
	 */
	public String getMethodUrl();
}
