package code.googe.com.architect.blog.search.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import code.googe.com.architect.blog.search.BlogQueryResult;
import code.googe.com.architect.blog.search.BlogSearchResponseHandler;
import code.googe.com.architect.blog.search.RetrievedBlogEntry;


// xml 파싱 추상화  
public abstract class BlogSearchResponseHandlerImpl extends DefaultHandler
	implements BlogSearchResponseHandler {


	private BlogQueryResultImpl result = null;
	private List<RetrievedBlogEntry> entries = null;
	private RetrievedBlogEntryImpl item = null;
	private XmlToken whichToken = null;
	private Map<String, XmlToken> tagMap = null;

	private String charString = null;
	private DateFormat dateFormat = null;
	private DateFormat timeZoneDateFormat = null;
	private DateFormat timeZoneDayDateFormat = null;
	private DateFormat timeZoneYearDateFormat = null;
	
	public BlogSearchResponseHandlerImpl() {
		this.result = new BlogQueryResultImpl();
		this.entries = new ArrayList<RetrievedBlogEntry>();
		this.result.setResults(this.entries);
		this.tagMap = new HashMap<String, XmlToken>();
		
		for(XmlToken t: getXmlTokens()) {
			this.tagMap.put(t.getTag(), t);
		}
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.timeZoneDayDateFormat = new SimpleDateFormat("EEE, dd MMM yy HH:mm:ss zzz");
		this.timeZoneYearDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
		this.timeZoneDateFormat = new SimpleDateFormat("dd MMM yy HH:mm:ss zzz");
	}
	
	// Concrete Class 에서 각각에 맞는 xml token 생성 
	protected abstract XmlToken [] getXmlTokens();
	// 블로그 시작 태그인지 검사 
	protected abstract boolean isBlogEntryToken(XmlToken t);
	
	
	public BlogQueryResult getBlogQueryResult() {
		return result;
	}
	
	public BlogQueryResultImpl getResult() {
		return result;
	}

	public List<RetrievedBlogEntry> getEntries() {
		return entries;
	}

	public RetrievedBlogEntryImpl getItem() {
		return item;
	}

	public XmlToken getWhichToken() {
		return whichToken;
	}

	public Map<String, XmlToken> getTagMap() {
		return tagMap;
	}

	public String getCharString() {
		return charString;
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public DateFormat getTimeZoneDateFormat() {
		return timeZoneDateFormat;
	}

	public DateFormat getTimeZoneDayDateFormat() {
		return timeZoneDayDateFormat;
	}

	public DateFormat getTimeZoneYearDateFormat() {
		return timeZoneYearDateFormat;
	}


	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		this.whichToken = null;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		XmlToken t = this.tagMap.get(qName);
		if(t != null) {
			this.whichToken = t;
			if(isBlogEntryToken(t)) {
				this.item = new RetrievedBlogEntryImpl();
				this.entries.add(this.item);
			}
		}
		this.charString = "";
	}

	public void setResult(BlogQueryResultImpl result) {
		this.result = result;
	}

	public void setEntries(List<RetrievedBlogEntry> entries) {
		this.entries = entries;
	}

	public void setItem(RetrievedBlogEntryImpl item) {
		this.item = item;
	}

	public void setWhichToken(XmlToken whichToken) {
		this.whichToken = whichToken;
	}

	public void setTagMap(Map<String, XmlToken> tagMap) {
		this.tagMap = tagMap;
	}

	public void setCharString(String charString) {
		this.charString = charString;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void setTimeZoneDateFormat(DateFormat timeZoneDateFormat) {
		this.timeZoneDateFormat = timeZoneDateFormat;
	}

	public void setTimeZoneDayDateFormat(DateFormat timeZoneDayDateFormat) {
		this.timeZoneDayDateFormat = timeZoneDayDateFormat;
	}

	public void setTimeZoneYearDateFormat(DateFormat timeZoneYearDateFormat) {
		this.timeZoneYearDateFormat = timeZoneYearDateFormat;
	}
	
	
}
