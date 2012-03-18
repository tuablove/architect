package code.googe.com.architect.blog.search.impl.daum;

import code.googe.com.architect.blog.search.BlogQueryParameter;
import code.googe.com.architect.blog.search.BlogQueryParameter.QueryParameter;
import code.googe.com.architect.blog.search.BlogQueryResult;
import code.googe.com.architect.blog.search.BlogSearcherException;
import code.googe.com.architect.blog.search.RetrievedBlogEntry;
import code.googe.com.architect.blog.search.impl.BlogSearcherImpl;

public class Main {
	public static void main(String args[]) throws BlogSearcherException {
		BlogSearcherImpl searcher = new DaumBlogSearcherImpl();
		BlogQueryParameter query = searcher.getBlogQueryParameter();
		
		
		query.setParameter(QueryParameter.KEY, "95ede5d72bcf95f1da41860aec39be7e2e4fa7ba");
		query.setParameter(QueryParameter.QUERY, "위대한탄생2");
		
		BlogQueryResult result = searcher.getRelevantBlogs(query);
	
		for(RetrievedBlogEntry entry:result.getRelevantBlogs()) {
			System.out.println(entry);
		}
	}
}
