package code.googe.com.architect.blog.search.impl.daum;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.xml.sax.SAXException;

import code.googe.com.architect.blog.search.impl.BlogSearchResponseHandlerImpl;
import code.googe.com.architect.blog.search.impl.RetrievedBlogEntryImpl;
import code.googe.com.architect.blog.search.impl.XmlToken;

public class DaumResponseHandler extends BlogSearchResponseHandlerImpl {
	
	// 다음의 응답 결과 xml tag
	public enum DaumXmlToken implements XmlToken {
		ITEM("item"), 
		TITLE("title"), 
		LINK("link"), 
		DESC("description"),
		BLOGGERNAME("author"), 
		PUBDATE("pubDate"), 
		TOTAL("totalCount"),
		CHANNEL("channel"), 
		COMMENT("comment");
		
		private String tag = null;
		
		DaumXmlToken(String tag) {
			this.tag = tag;
		}

		@Override
		public String getTag() {
			return this.tag;
		}
		
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String s = this.getCharString() + new String(ch, start, length);
		this.setCharString(s);
		
		DaumXmlToken token = (DaumXmlToken)this.getWhichToken();
		
		if(token == DaumXmlToken.TOTAL) {
			this.getResult().setQueryCount(new Integer(s));
			return;
		}
		
		RetrievedBlogEntryImpl item = getItem();
		if( (token != null) && (item != null) ) {
			switch(token) {
				case COMMENT:
					item.setName(s);
					break;
				case LINK:
					item.setUrl(s);
					break;
				case TITLE:
					item.setTitle(s);
					break;
				case BLOGGERNAME:
					item.setAuthor(s);
					break;
				case PUBDATE:
					SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
					try {
						item.setCreationTime(formater.parse(s));
					} catch (ParseException e) {
						System.err.println(e);
					}
					break;
				case DESC:
					item.setExcerpt(s);
					break;
			}
		}
	}

	@Override
	protected XmlToken[] getXmlTokens() {
		return DaumXmlToken.values();
	}

	@Override
	protected boolean isBlogEntryToken(XmlToken t) {
		return (DaumXmlToken.ITEM.compareTo((DaumXmlToken)t) == 0);
	}

}
