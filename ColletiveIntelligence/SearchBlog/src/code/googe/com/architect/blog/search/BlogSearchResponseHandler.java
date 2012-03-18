package code.googe.com.architect.blog.search;

/**
 * @author  chois79
 * 			응단 결과 처리 interface
 * 			상속받은 클래스는 결과 파서 핸들러 구현 
 */
public interface BlogSearchResponseHandler {

	public BlogQueryResult getBlogQueryResult();		
}
