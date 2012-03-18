package code.googe.com.architect.blog.search.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import code.googe.com.architect.blog.search.BlogQueryParameter;
import code.googe.com.architect.blog.search.BlogQueryParameter.QueryParameter;
import code.googe.com.architect.blog.search.BlogQueryResult;
import code.googe.com.architect.blog.search.BlogSearchResponseHandler;
import code.googe.com.architect.blog.search.BlogSearcher;
import code.googe.com.architect.blog.search.BlogSearcherException;

public abstract class BlogSearcherImpl implements BlogSearcher {

	// apache xerces parser 사용 
	private static final String JAXP_PROPERTY_NAME = 
			"javax.xml.parsers.SAXParserFactory";
	private static final String APACHE_XERCES_SAX_PARSER =
			"org.apache.xerces.jaxp.SAXParserFactoryImpl";

	private SAXParser parser = null;
	private Map<QueryParameter, String> paramStringMap = null;
	
	protected BlogSearcherImpl() throws BlogSearcherException {
		createSAXParser();
		initializeParamStringMap();
	}
	
	private void createSAXParser() throws BlogSearcherException {
		if(System.getProperty(JAXP_PROPERTY_NAME) == null) {
			System.setProperty(JAXP_PROPERTY_NAME, APACHE_XERCES_SAX_PARSER);
		}
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			this.parser = factory.newSAXParser();
		} catch (ParserConfigurationException e) {
			throw new BlogSearcherException("SAX parser not found", e);
		} catch (SAXException e) {
			throw new BlogSearcherException("SAX exception", e);
		}
	}
	
	protected void initializeParamStringMap() {
		paramStringMap = new HashMap<QueryParameter, String>();
	}
	
	protected Map<QueryParameter, String> getParamStringMap() {
		return paramStringMap;
	}
	
	protected SAXParser getSAXParser() {
		return this.parser;
	}
	
	@Override
	public BlogQueryResult getRelevantBlogs(BlogQueryParameter param)
			throws BlogSearcherException {
		
		BlogQueryResult result = new NullBlogQueryResultImpl(); // dump
		HttpClient client = new HttpClient();
		
		HttpMethod method = getMethod(param);
		
		// 재시도 핸들로 등록 
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
				new DefaultHttpMethodRetryHandler(3, false));
		
		try{
			int statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {
				InputStream is = method.getResponseBodyAsStream();
				result = parseContent(is);
				is.close();
			}
		} catch(HttpException e) {
			throw new BlogSearcherException("HTTP exception", e);
		} catch(IOException e) {
			throw new BlogSearcherException("IOException while getting response body", e);
		} finally{
			method.releaseConnection();
		}
		
		return result;
	}
	
	// blog별 특화된 Request 생성. 
	protected abstract HttpMethod getMethod(BlogQueryParameter param);

	// 아래는 factory method: parameter, handler
	public abstract BlogQueryParameter getBlogQueryParameter();
	protected abstract BlogSearchResponseHandler getBlogSearchResponseHandler() ;
	
	
	private BlogQueryResult parseContent(InputStream is) 
		throws BlogSearcherException {
		try {
			BlogSearchResponseHandler h = getBlogSearchResponseHandler();
			this.getSAXParser().parse(is, (DefaultHandler) h);
			return h.getBlogQueryResult();
		} catch (SAXException e) {
			throw new BlogSearcherException("Error parsing Response XML", e);
		} catch (IOException e) {
			throw new BlogSearcherException("IOException while parsing XML", e);
		}
	}
	
	public static String urlEncode(String s) {
		String result = s;
		
		try {
			result = URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("Unsupported encoding exception thrown");
		}
		
		return result;
	}
}
